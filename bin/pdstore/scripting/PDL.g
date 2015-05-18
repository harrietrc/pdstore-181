grammar PDL;
/* Grammar for the PDStore scripting language.

execute class org.antlr.Tool
with grammar name as argument
in directory ${workspace_loc:pdstore/src/pdstore/scripting} 

A sample file (test.pdl)

//Adding some triples
1 connectedTo 3;
1 name "testGuid";
commit;  

// Searching for one of the triples just added:
?x connectedTo ?y ?

// Printing all the GUIDs that have names:
?x name, ?y?
commit;

*/

options {
    backtrack = true; 
    memoize = true;
}

@header {
	package pdstore.scripting;
	
	import java.util.HashMap;
	import java.util.List;
	import java.util.Iterator;
	
	import pdstore.*;
	import pdstore.sparql.*;
	import pdstore.rules.*;
}

@lexer::header {
	package pdstore.scripting;
}

@members {
	Interpreter interpreter;
	
	// map for mapping variable/action names to objects
	Map<String, GUID> variables = new HashMap<String, GUID>();
	
	// override error message printing to make sure they are printed on stdout
	// (not stderr, which intermingles in an ugly manner with stderr when getting input from stdin)
    public void displayRecognitionError(String[] tokenNames,
                                        RecognitionException e) {
        String hdr = getErrorHeader(e);
        String msg = getErrorMessage(e, tokenNames);
        System.out.println("Error in " + hdr + " " + msg);
    }
}


/******************* Parser ******************/

// Start rule for parsing a PDL script.
pdlScript returns [List<Object> value]
	@init {	$value = new ArrayList<Object>(); }
    :   (s=statement
    	{	$value.add($s.value); }
    	)+
    	EOF  // make sure the whole script is parsed
    ;

statement returns [Object value]
	:	t=tuple ';'
		{	
			PDChange<GUID, Object, GUID> change = $t.value;
			interpreter.store.ensureLink(change.getInstance1(), change.getRole2(), change.getInstance2());
		}
	|	q=query '?'
		{	
			Iterator<Assignment<GUID, Object, GUID>> result = $q.value
				.execute(interpreter.store.getCurrentTransaction());
			if (interpreter.getQuery) {
				// get the query result as object
				interpreter.lastResult = result;
			} else {
				// print the query result
				while (result.hasNext()) {
					System.out.println(result.next().toString(interpreter.store));
				}
			}
		}
    |	r=rule ';'
    	{	
    		$r.value.apply(interpreter.store);
		}
    |	'commit' ';'
    	{
    		interpreter.store.commit();
    	}
    |	'open' s=IDENT ';'
		{	
			interpreter.store = new PDStore($s.text);
		}
    |	'run' f=IDENT ';'
    	{	
    		new Interpreter(interpreter.store).executeScript($f.text); 
    	}
    ;

tuple returns [PDChange<GUID, Object, GUID> value]
    :   l1=instance r=role l2=instance
    	{	
    		$value = new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED, null, $l1.value, $r.value, $l2.value);
    	}
    ;
    
rule returns [Rule value]
	:	pre=query '->' post=postcondition 
		{	
			$value = new Rule($pre.value, $post.value); 
		}
	;
	
query returns [Query value]
	@init {	List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>(); }
	:	t1=queryTuple
		{	where.add($t1.value); }	
		( ',' t2=queryTuple
		{	where.add($t2.value); }
		)*
		{	
			$value = new Query(where, interpreter.store);
		}
	;

postcondition returns [List<PDChange<GUID, Object, GUID>> value]
	@init {	$value = new ArrayList<PDChange<GUID, Object, GUID>>(); }
	:	t1=actionTuple
		{	$value.add($t1.value); }
		( ',' t2=actionTuple
		{	$value.add($t2.value); }
		)*
	;
	
queryTuple returns [PDChange<GUID, Object, GUID> value]
    :   i1=queryInstance 
    	r2=queryRole 
    	i2=queryInstance 
    	{	$value = new PDChange<GUID, Object, GUID>(null, null, $i1.value, $r2.value, $i2.value); }
    ;

actionTuple returns [PDChange<GUID, Object, GUID> value]
    :   i1=actionInstance 
    	r2=actionRole
    	i2=actionInstance 
    	{	$value = new PDChange<GUID, Object, GUID>(null, null, $i1.value, $r2.value, $i2.value); }
    ;

instance returns [Object value]
	:	l=literal
		{	$value = $l.value; } 
	| 	i=IDENT 
		{
			$value = interpreter.store.getId($i.text);
			if ($value == null) {
				$value = new GUID();
				System.out.println("Creating instance " + $value + " with name "+ $i.text + " in line " + $i.line + ".");
				interpreter.store.setName($value, $i.text);
			}
		}
	;

queryInstance returns [Object value]
	:	i=instance
		{	$value = $i.value; } 
	| 	v=variable 
		{	$value = $v.value; }
	;
	
actionInstance returns [Object value]
	:	q=queryInstance
		{	$value = $q.value; }
	| 	a=action
		{	$value = $a.value; }
	;
	
variable returns [Variable value]
	:	v=VARIABLE 
		{	
			String name = $v.text.substring(1);
			$value = (Variable) variables.get(name); 
			if ($value == null) {
				$value = new Variable(name);
				variables.put(name, $value);
			}
		}
	;

action returns [Action value]
	:	a=ACTION
		{	
			String name = $a.text.substring(1);
			$value = (Action) variables.get(name); 
			if ($value == null) {
				$value = new Action(name);
				variables.put(name, $value);
			}
		}
	;
	
role returns [GUID value]
	:	g=GUID_LITERAL
		{	$value = new GUID($g.text.substring(1)); }
	|	i=IDENT
		{
			$value = interpreter.store.getId($i.text);
			if (value == null) {
				$value = new GUID();
				System.out.println("Creating instance " + $value + " with name "+ $i.text + " in line " + $i.line + ".");
				interpreter.store.setName($value, $i.text);
			}
		}
	;

queryRole returns [GUID value]
	:	r=role
		{	$value = $r.value; }
	|	v=VARIABLE 
		{	
			String name = $v.text.substring(1);
			$value = variables.get(name); 
			if ($value == null) {
				$value = new Variable(name);
				variables.put(name, $value);
			}
		}
	;
	
actionRole returns [GUID value]
	:	r=queryRole 
		{	$value = $r.value; }
	| 	a=ACTION
		{	
			$value = variables.get($a.text); 
			if ($value == null) {
				$value = new Action($a.text);
				variables.put($a.text, $value);
			}
		}
	;

literal returns [Object value]
    :   g=GUID_LITERAL
    	{	$value = new GUID($g.text.substring(1)); }
    |	HEX_LITERAL
    |   OCTAL_LITERAL
    |   d=DECIMAL_LITERAL
    	{	$value = Long.parseLong($d.text); }
    |   f=FLOATING_POINT_LITERAL
    	{	$value = Double.parseDouble($f.text); }
    |   c=CHARACTER_LITERAL
    	{	$value = new Character($c.text.charAt(0)); }
    |   s=STRING_LITERAL
    	{	$value = $s.text.substring(1, $s.text.length()-1); }
    |   'true'
    	{	$value = true; } 
    |	'false'
    	{	$value = false; }
    ;



/******************* Lexer ******************/
    
GUID_LITERAL
    :  '$' 
    	HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    	HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    	HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    	HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;
    
VARIABLE
	:	'?' IDENT
	;
	
ACTION
	:	'!' IDENT
	;   

HEX_LITERAL : '0' ('x'|'X') HEX_DIGIT+ INTEGER_TYPE_SUFFIX? ;

DECIMAL_LITERAL : ('0' | '1'..'9' '0'..'9'*) INTEGER_TYPE_SUFFIX? ;

OCTAL_LITERAL : '0' ('0'..'7')+ INTEGER_TYPE_SUFFIX? ;

fragment
HEX_DIGIT : ('0'..'9'|'a'..'f'|'A'..'F') ;

fragment
INTEGER_TYPE_SUFFIX : ('l'|'L') ;

FLOATING_POINT_LITERAL
    :   ('0'..'9')+ 
        (
            '.' ('0'..'9')* EXPONENT? FLOAT_TYPE_SUFFIX?
        |   EXPONENT FLOAT_TYPE_SUFFIX?
        |   FLOAT_TYPE_SUFFIX
        )
    |   '.' ('0'..'9')+ EXPONENT? FLOAT_TYPE_SUFFIX?
    ;

fragment
EXPONENT : ('e'|'E') ('+'|'-')? ('0'..'9')+ ;

fragment
FLOAT_TYPE_SUFFIX : ('f'|'F'|'d'|'D') ;

CHARACTER_LITERAL
    :   '\'' ( ESCAPE_SEQUENCE | ~('\''|'\\') ) '\''
    ;

STRING_LITERAL
    :  '"' ( ESCAPE_SEQUENCE | ~('\\'|'"') )* '"'
    ;

fragment
ESCAPE_SEQUENCE
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    |   UNICODE_ESCAPE
    |   OCTAL_ESCAPE
    ;

fragment
OCTAL_ESCAPE
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

fragment
UNICODE_ESCAPE
    :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;

IDENT
    :   JAVA_ID_START (JAVA_ID_PART)*
    ;

fragment
JAVA_ID_START
    :  '\u0024'
    |  '\u0041'..'\u005a'
    |  '\u005f'
    |  '\u0061'..'\u007a'
    |  '\u00c0'..'\u00d6'
    |  '\u00d8'..'\u00f6'
    |  '\u00f8'..'\u00ff'
    |  '\u0100'..'\u1fff'
    |  '\u3040'..'\u318f'
    |  '\u3300'..'\u337f'
    |  '\u3400'..'\u3d2d'
    |  '\u4e00'..'\u9fff'
    |  '\uf900'..'\ufaff'
    ;

fragment
JAVA_ID_PART
    :  JAVA_ID_START
    |  '\u0030'..'\u0039'
    ;

WS  :  (' '|'\r'|'\t'|'\u000C'|'\n') 
    {	$channel = HIDDEN; }
    ;

COMMENT
    :   '/*' ( options {greedy=false;} : . )* '*/'
    {	$channel = HIDDEN; }
    ;

LINE_COMMENT
    : '//' ~('\n'|'\r')* '\r'? '\n'
    {	$channel = HIDDEN; }
    ;

