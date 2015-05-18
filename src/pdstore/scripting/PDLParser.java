// $ANTLR 3.4 PDL.g 2013-08-21 18:21:32

	package pdstore.scripting;
	
	import java.util.HashMap;
	import java.util.List;
	import java.util.Iterator;
	
	import pdstore.*;
	import pdstore.sparql.*;
	import pdstore.rules.*;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class PDLParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ACTION", "CHARACTER_LITERAL", "COMMENT", "DECIMAL_LITERAL", "ESCAPE_SEQUENCE", "EXPONENT", "FLOATING_POINT_LITERAL", "FLOAT_TYPE_SUFFIX", "GUID_LITERAL", "HEX_DIGIT", "HEX_LITERAL", "IDENT", "INTEGER_TYPE_SUFFIX", "JAVA_ID_PART", "JAVA_ID_START", "LINE_COMMENT", "OCTAL_ESCAPE", "OCTAL_LITERAL", "STRING_LITERAL", "UNICODE_ESCAPE", "VARIABLE", "WS", "','", "'->'", "';'", "'?'", "'commit'", "'false'", "'open'", "'run'", "'true'"
    };

    public static final int EOF=-1;
    public static final int T__26=26;
    public static final int T__27=27;
    public static final int T__28=28;
    public static final int T__29=29;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int T__33=33;
    public static final int T__34=34;
    public static final int ACTION=4;
    public static final int CHARACTER_LITERAL=5;
    public static final int COMMENT=6;
    public static final int DECIMAL_LITERAL=7;
    public static final int ESCAPE_SEQUENCE=8;
    public static final int EXPONENT=9;
    public static final int FLOATING_POINT_LITERAL=10;
    public static final int FLOAT_TYPE_SUFFIX=11;
    public static final int GUID_LITERAL=12;
    public static final int HEX_DIGIT=13;
    public static final int HEX_LITERAL=14;
    public static final int IDENT=15;
    public static final int INTEGER_TYPE_SUFFIX=16;
    public static final int JAVA_ID_PART=17;
    public static final int JAVA_ID_START=18;
    public static final int LINE_COMMENT=19;
    public static final int OCTAL_ESCAPE=20;
    public static final int OCTAL_LITERAL=21;
    public static final int STRING_LITERAL=22;
    public static final int UNICODE_ESCAPE=23;
    public static final int VARIABLE=24;
    public static final int WS=25;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public PDLParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public PDLParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
        this.state.ruleMemo = new HashMap[39+1];
         

    }

    public String[] getTokenNames() { return PDLParser.tokenNames; }
    public String getGrammarFileName() { return "PDL.g"; }


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



    // $ANTLR start "pdlScript"
    // PDL.g:65:1: pdlScript returns [List<Object> value] : (s= statement )+ EOF ;
    public final List<Object> pdlScript() throws RecognitionException {
        List<Object> value = null;

        int pdlScript_StartIndex = input.index();

        Object s =null;


        	value = new ArrayList<Object>(); 
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 1) ) { return value; }

            // PDL.g:67:5: ( (s= statement )+ EOF )
            // PDL.g:67:9: (s= statement )+ EOF
            {
            // PDL.g:67:9: (s= statement )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==CHARACTER_LITERAL||LA1_0==DECIMAL_LITERAL||LA1_0==FLOATING_POINT_LITERAL||LA1_0==GUID_LITERAL||(LA1_0 >= HEX_LITERAL && LA1_0 <= IDENT)||(LA1_0 >= OCTAL_LITERAL && LA1_0 <= STRING_LITERAL)||LA1_0==VARIABLE||(LA1_0 >= 30 && LA1_0 <= 34)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // PDL.g:67:10: s= statement
            	    {
            	    pushFollow(FOLLOW_statement_in_pdlScript85);
            	    s=statement();

            	    state._fsp--;
            	    if (state.failed) return value;

            	    if ( state.backtracking==0 ) {	value.add(s); }

            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
            	    if (state.backtracking>0) {state.failed=true; return value;}
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            match(input,EOF,FOLLOW_EOF_in_pdlScript107); if (state.failed) return value;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 1, pdlScript_StartIndex); }

        }
        return value;
    }
    // $ANTLR end "pdlScript"



    // $ANTLR start "statement"
    // PDL.g:73:1: statement returns [Object value] : (t= tuple ';' |q= query '?' |r= rule ';' | 'commit' ';' | 'open' s= IDENT ';' | 'run' f= IDENT ';' );
    public final Object statement() throws RecognitionException {
        Object value = null;

        int statement_StartIndex = input.index();

        Token s=null;
        Token f=null;
        PDChange<GUID, Object, GUID> t =null;

        Query q =null;

        Rule r =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 2) ) { return value; }

            // PDL.g:74:2: (t= tuple ';' |q= query '?' |r= rule ';' | 'commit' ';' | 'open' s= IDENT ';' | 'run' f= IDENT ';' )
            int alt2=6;
            alt2 = dfa2.predict(input);
            switch (alt2) {
                case 1 :
                    // PDL.g:74:4: t= tuple ';'
                    {
                    pushFollow(FOLLOW_tuple_in_statement129);
                    t=tuple();

                    state._fsp--;
                    if (state.failed) return value;

                    match(input,28,FOLLOW_28_in_statement131); if (state.failed) return value;

                    if ( state.backtracking==0 ) {	
                    			PDChange<GUID, Object, GUID> change = t;
                    			interpreter.store.ensureLink(change.getInstance1(), change.getRole2(), change.getInstance2());
                    		}

                    }
                    break;
                case 2 :
                    // PDL.g:79:4: q= query '?'
                    {
                    pushFollow(FOLLOW_query_in_statement142);
                    q=query();

                    state._fsp--;
                    if (state.failed) return value;

                    match(input,29,FOLLOW_29_in_statement144); if (state.failed) return value;

                    if ( state.backtracking==0 ) {	
                    			Iterator<Assignment<GUID, Object, GUID>> result = q
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

                    }
                    break;
                case 3 :
                    // PDL.g:93:7: r= rule ';'
                    {
                    pushFollow(FOLLOW_rule_in_statement158);
                    r=rule();

                    state._fsp--;
                    if (state.failed) return value;

                    match(input,28,FOLLOW_28_in_statement160); if (state.failed) return value;

                    if ( state.backtracking==0 ) {	
                        		r.apply(interpreter.store);
                    		}

                    }
                    break;
                case 4 :
                    // PDL.g:97:7: 'commit' ';'
                    {
                    match(input,30,FOLLOW_30_in_statement175); if (state.failed) return value;

                    match(input,28,FOLLOW_28_in_statement177); if (state.failed) return value;

                    if ( state.backtracking==0 ) {
                        		interpreter.store.commit();
                        	}

                    }
                    break;
                case 5 :
                    // PDL.g:101:7: 'open' s= IDENT ';'
                    {
                    match(input,32,FOLLOW_32_in_statement192); if (state.failed) return value;

                    s=(Token)match(input,IDENT,FOLLOW_IDENT_in_statement196); if (state.failed) return value;

                    match(input,28,FOLLOW_28_in_statement198); if (state.failed) return value;

                    if ( state.backtracking==0 ) {	
                    			interpreter.store = new PDStore((s!=null?s.getText():null));
                    		}

                    }
                    break;
                case 6 :
                    // PDL.g:105:7: 'run' f= IDENT ';'
                    {
                    match(input,33,FOLLOW_33_in_statement210); if (state.failed) return value;

                    f=(Token)match(input,IDENT,FOLLOW_IDENT_in_statement214); if (state.failed) return value;

                    match(input,28,FOLLOW_28_in_statement216); if (state.failed) return value;

                    if ( state.backtracking==0 ) {	
                        		new Interpreter(interpreter.store).executeScript((f!=null?f.getText():null)); 
                        	}

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 2, statement_StartIndex); }

        }
        return value;
    }
    // $ANTLR end "statement"



    // $ANTLR start "tuple"
    // PDL.g:111:1: tuple returns [PDChange<GUID, Object, GUID> value] : l1= instance r= role l2= instance ;
    public final PDChange<GUID, Object, GUID> tuple() throws RecognitionException {
        PDChange<GUID, Object, GUID> value = null;

        int tuple_StartIndex = input.index();

        Object l1 =null;

        GUID r =null;

        Object l2 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 3) ) { return value; }

            // PDL.g:112:5: (l1= instance r= role l2= instance )
            // PDL.g:112:9: l1= instance r= role l2= instance
            {
            pushFollow(FOLLOW_instance_in_tuple248);
            l1=instance();

            state._fsp--;
            if (state.failed) return value;

            pushFollow(FOLLOW_role_in_tuple252);
            r=role();

            state._fsp--;
            if (state.failed) return value;

            pushFollow(FOLLOW_instance_in_tuple256);
            l2=instance();

            state._fsp--;
            if (state.failed) return value;

            if ( state.backtracking==0 ) {	
                		value = new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED, null, l1, r, l2);
                	}

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 3, tuple_StartIndex); }

        }
        return value;
    }
    // $ANTLR end "tuple"



    // $ANTLR start "rule"
    // PDL.g:118:1: rule returns [Rule value] : pre= query '->' post= postcondition ;
    public final Rule rule() throws RecognitionException {
        Rule value = null;

        int rule_StartIndex = input.index();

        Query pre =null;

        List<PDChange<GUID, Object, GUID>> post =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 4) ) { return value; }

            // PDL.g:119:2: (pre= query '->' post= postcondition )
            // PDL.g:119:4: pre= query '->' post= postcondition
            {
            pushFollow(FOLLOW_query_in_rule287);
            pre=query();

            state._fsp--;
            if (state.failed) return value;

            match(input,27,FOLLOW_27_in_rule289); if (state.failed) return value;

            pushFollow(FOLLOW_postcondition_in_rule293);
            post=postcondition();

            state._fsp--;
            if (state.failed) return value;

            if ( state.backtracking==0 ) {	
            			value = new Rule(pre, post); 
            		}

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 4, rule_StartIndex); }

        }
        return value;
    }
    // $ANTLR end "rule"



    // $ANTLR start "query"
    // PDL.g:125:1: query returns [Query value] : t1= queryTuple ( ',' t2= queryTuple )* ;
    public final Query query() throws RecognitionException {
        Query value = null;

        int query_StartIndex = input.index();

        PDChange<GUID, Object, GUID> t1 =null;

        PDChange<GUID, Object, GUID> t2 =null;


        	List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>(); 
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 5) ) { return value; }

            // PDL.g:127:2: (t1= queryTuple ( ',' t2= queryTuple )* )
            // PDL.g:127:4: t1= queryTuple ( ',' t2= queryTuple )*
            {
            pushFollow(FOLLOW_queryTuple_in_query322);
            t1=queryTuple();

            state._fsp--;
            if (state.failed) return value;

            if ( state.backtracking==0 ) {	where.add(t1); }

            // PDL.g:129:3: ( ',' t2= queryTuple )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==26) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // PDL.g:129:5: ',' t2= queryTuple
            	    {
            	    match(input,26,FOLLOW_26_in_query333); if (state.failed) return value;

            	    pushFollow(FOLLOW_queryTuple_in_query337);
            	    t2=queryTuple();

            	    state._fsp--;
            	    if (state.failed) return value;

            	    if ( state.backtracking==0 ) {	where.add(t2); }

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);


            if ( state.backtracking==0 ) {	
            			value = new Query(where, interpreter.store);
            		}

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 5, query_StartIndex); }

        }
        return value;
    }
    // $ANTLR end "query"



    // $ANTLR start "postcondition"
    // PDL.g:137:1: postcondition returns [List<PDChange<GUID, Object, GUID>> value] : t1= actionTuple ( ',' t2= actionTuple )* ;
    public final List<PDChange<GUID, Object, GUID>> postcondition() throws RecognitionException {
        List<PDChange<GUID, Object, GUID>> value = null;

        int postcondition_StartIndex = input.index();

        PDChange<GUID, Object, GUID> t1 =null;

        PDChange<GUID, Object, GUID> t2 =null;


        	value = new ArrayList<PDChange<GUID, Object, GUID>>(); 
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 6) ) { return value; }

            // PDL.g:139:2: (t1= actionTuple ( ',' t2= actionTuple )* )
            // PDL.g:139:4: t1= actionTuple ( ',' t2= actionTuple )*
            {
            pushFollow(FOLLOW_actionTuple_in_postcondition373);
            t1=actionTuple();

            state._fsp--;
            if (state.failed) return value;

            if ( state.backtracking==0 ) {	value.add(t1); }

            // PDL.g:141:3: ( ',' t2= actionTuple )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==26) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // PDL.g:141:5: ',' t2= actionTuple
            	    {
            	    match(input,26,FOLLOW_26_in_postcondition383); if (state.failed) return value;

            	    pushFollow(FOLLOW_actionTuple_in_postcondition387);
            	    t2=actionTuple();

            	    state._fsp--;
            	    if (state.failed) return value;

            	    if ( state.backtracking==0 ) {	value.add(t2); }

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 6, postcondition_StartIndex); }

        }
        return value;
    }
    // $ANTLR end "postcondition"



    // $ANTLR start "queryTuple"
    // PDL.g:146:1: queryTuple returns [PDChange<GUID, Object, GUID> value] : i1= queryInstance r2= queryRole i2= queryInstance ;
    public final PDChange<GUID, Object, GUID> queryTuple() throws RecognitionException {
        PDChange<GUID, Object, GUID> value = null;

        int queryTuple_StartIndex = input.index();

        Object i1 =null;

        GUID r2 =null;

        Object i2 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 7) ) { return value; }

            // PDL.g:147:5: (i1= queryInstance r2= queryRole i2= queryInstance )
            // PDL.g:147:9: i1= queryInstance r2= queryRole i2= queryInstance
            {
            pushFollow(FOLLOW_queryInstance_in_queryTuple419);
            i1=queryInstance();

            state._fsp--;
            if (state.failed) return value;

            pushFollow(FOLLOW_queryRole_in_queryTuple429);
            r2=queryRole();

            state._fsp--;
            if (state.failed) return value;

            pushFollow(FOLLOW_queryInstance_in_queryTuple439);
            i2=queryInstance();

            state._fsp--;
            if (state.failed) return value;

            if ( state.backtracking==0 ) {	value = new PDChange<GUID, Object, GUID>(null, null, i1, r2, i2); }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 7, queryTuple_StartIndex); }

        }
        return value;
    }
    // $ANTLR end "queryTuple"



    // $ANTLR start "actionTuple"
    // PDL.g:153:1: actionTuple returns [PDChange<GUID, Object, GUID> value] : i1= actionInstance r2= actionRole i2= actionInstance ;
    public final PDChange<GUID, Object, GUID> actionTuple() throws RecognitionException {
        PDChange<GUID, Object, GUID> value = null;

        int actionTuple_StartIndex = input.index();

        Object i1 =null;

        GUID r2 =null;

        Object i2 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 8) ) { return value; }

            // PDL.g:154:5: (i1= actionInstance r2= actionRole i2= actionInstance )
            // PDL.g:154:9: i1= actionInstance r2= actionRole i2= actionInstance
            {
            pushFollow(FOLLOW_actionInstance_in_actionTuple472);
            i1=actionInstance();

            state._fsp--;
            if (state.failed) return value;

            pushFollow(FOLLOW_actionRole_in_actionTuple482);
            r2=actionRole();

            state._fsp--;
            if (state.failed) return value;

            pushFollow(FOLLOW_actionInstance_in_actionTuple491);
            i2=actionInstance();

            state._fsp--;
            if (state.failed) return value;

            if ( state.backtracking==0 ) {	value = new PDChange<GUID, Object, GUID>(null, null, i1, r2, i2); }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 8, actionTuple_StartIndex); }

        }
        return value;
    }
    // $ANTLR end "actionTuple"



    // $ANTLR start "instance"
    // PDL.g:160:1: instance returns [Object value] : (l= literal |i= IDENT );
    public final Object instance() throws RecognitionException {
        Object value = null;

        int instance_StartIndex = input.index();

        Token i=null;
        Object l =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 9) ) { return value; }

            // PDL.g:161:2: (l= literal |i= IDENT )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==CHARACTER_LITERAL||LA5_0==DECIMAL_LITERAL||LA5_0==FLOATING_POINT_LITERAL||LA5_0==GUID_LITERAL||LA5_0==HEX_LITERAL||(LA5_0 >= OCTAL_LITERAL && LA5_0 <= STRING_LITERAL)||LA5_0==31||LA5_0==34) ) {
                alt5=1;
            }
            else if ( (LA5_0==IDENT) ) {
                alt5=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;

            }
            switch (alt5) {
                case 1 :
                    // PDL.g:161:4: l= literal
                    {
                    pushFollow(FOLLOW_literal_in_instance519);
                    l=literal();

                    state._fsp--;
                    if (state.failed) return value;

                    if ( state.backtracking==0 ) {	value = l; }

                    }
                    break;
                case 2 :
                    // PDL.g:163:5: i= IDENT
                    {
                    i=(Token)match(input,IDENT,FOLLOW_IDENT_in_instance532); if (state.failed) return value;

                    if ( state.backtracking==0 ) {
                    			value = interpreter.store.getId((i!=null?i.getText():null));
                    			if (value == null) {
                    				value = new GUID();
                    				System.out.println("Creating instance " + value + " with name "+ (i!=null?i.getText():null) + " in line " + (i!=null?i.getLine():0) + ".");
                    				interpreter.store.setName(value, (i!=null?i.getText():null));
                    			}
                    		}

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 9, instance_StartIndex); }

        }
        return value;
    }
    // $ANTLR end "instance"



    // $ANTLR start "queryInstance"
    // PDL.g:174:1: queryInstance returns [Object value] : (i= instance |v= variable );
    public final Object queryInstance() throws RecognitionException {
        Object value = null;

        int queryInstance_StartIndex = input.index();

        Object i =null;

        Variable v =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 10) ) { return value; }

            // PDL.g:175:2: (i= instance |v= variable )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==CHARACTER_LITERAL||LA6_0==DECIMAL_LITERAL||LA6_0==FLOATING_POINT_LITERAL||LA6_0==GUID_LITERAL||(LA6_0 >= HEX_LITERAL && LA6_0 <= IDENT)||(LA6_0 >= OCTAL_LITERAL && LA6_0 <= STRING_LITERAL)||LA6_0==31||LA6_0==34) ) {
                alt6=1;
            }
            else if ( (LA6_0==VARIABLE) ) {
                alt6=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;

            }
            switch (alt6) {
                case 1 :
                    // PDL.g:175:4: i= instance
                    {
                    pushFollow(FOLLOW_instance_in_queryInstance554);
                    i=instance();

                    state._fsp--;
                    if (state.failed) return value;

                    if ( state.backtracking==0 ) {	value = i; }

                    }
                    break;
                case 2 :
                    // PDL.g:177:5: v= variable
                    {
                    pushFollow(FOLLOW_variable_in_queryInstance567);
                    v=variable();

                    state._fsp--;
                    if (state.failed) return value;

                    if ( state.backtracking==0 ) {	value = v; }

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 10, queryInstance_StartIndex); }

        }
        return value;
    }
    // $ANTLR end "queryInstance"



    // $ANTLR start "actionInstance"
    // PDL.g:181:1: actionInstance returns [Object value] : (q= queryInstance |a= action );
    public final Object actionInstance() throws RecognitionException {
        Object value = null;

        int actionInstance_StartIndex = input.index();

        Object q =null;

        Action a =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 11) ) { return value; }

            // PDL.g:182:2: (q= queryInstance |a= action )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==CHARACTER_LITERAL||LA7_0==DECIMAL_LITERAL||LA7_0==FLOATING_POINT_LITERAL||LA7_0==GUID_LITERAL||(LA7_0 >= HEX_LITERAL && LA7_0 <= IDENT)||(LA7_0 >= OCTAL_LITERAL && LA7_0 <= STRING_LITERAL)||LA7_0==VARIABLE||LA7_0==31||LA7_0==34) ) {
                alt7=1;
            }
            else if ( (LA7_0==ACTION) ) {
                alt7=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;

            }
            switch (alt7) {
                case 1 :
                    // PDL.g:182:4: q= queryInstance
                    {
                    pushFollow(FOLLOW_queryInstance_in_actionInstance590);
                    q=queryInstance();

                    state._fsp--;
                    if (state.failed) return value;

                    if ( state.backtracking==0 ) {	value = q; }

                    }
                    break;
                case 2 :
                    // PDL.g:184:5: a= action
                    {
                    pushFollow(FOLLOW_action_in_actionInstance602);
                    a=action();

                    state._fsp--;
                    if (state.failed) return value;

                    if ( state.backtracking==0 ) {	value = a; }

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 11, actionInstance_StartIndex); }

        }
        return value;
    }
    // $ANTLR end "actionInstance"



    // $ANTLR start "variable"
    // PDL.g:188:1: variable returns [Variable value] : v= VARIABLE ;
    public final Variable variable() throws RecognitionException {
        Variable value = null;

        int variable_StartIndex = input.index();

        Token v=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 12) ) { return value; }

            // PDL.g:189:2: (v= VARIABLE )
            // PDL.g:189:4: v= VARIABLE
            {
            v=(Token)match(input,VARIABLE,FOLLOW_VARIABLE_in_variable624); if (state.failed) return value;

            if ( state.backtracking==0 ) {	
            			String name = (v!=null?v.getText():null).substring(1);
            			value = (Variable) variables.get(name); 
            			if (value == null) {
            				value = new Variable(name);
            				variables.put(name, value);
            			}
            		}

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 12, variable_StartIndex); }

        }
        return value;
    }
    // $ANTLR end "variable"



    // $ANTLR start "action"
    // PDL.g:200:1: action returns [Action value] : a= ACTION ;
    public final Action action() throws RecognitionException {
        Action value = null;

        int action_StartIndex = input.index();

        Token a=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 13) ) { return value; }

            // PDL.g:201:2: (a= ACTION )
            // PDL.g:201:4: a= ACTION
            {
            a=(Token)match(input,ACTION,FOLLOW_ACTION_in_action646); if (state.failed) return value;

            if ( state.backtracking==0 ) {	
            			String name = (a!=null?a.getText():null).substring(1);
            			value = (Action) variables.get(name); 
            			if (value == null) {
            				value = new Action(name);
            				variables.put(name, value);
            			}
            		}

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 13, action_StartIndex); }

        }
        return value;
    }
    // $ANTLR end "action"



    // $ANTLR start "role"
    // PDL.g:212:1: role returns [GUID value] : (g= GUID_LITERAL |i= IDENT );
    public final GUID role() throws RecognitionException {
        GUID value = null;

        int role_StartIndex = input.index();

        Token g=null;
        Token i=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 14) ) { return value; }

            // PDL.g:213:2: (g= GUID_LITERAL |i= IDENT )
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==GUID_LITERAL) ) {
                alt8=1;
            }
            else if ( (LA8_0==IDENT) ) {
                alt8=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;

            }
            switch (alt8) {
                case 1 :
                    // PDL.g:213:4: g= GUID_LITERAL
                    {
                    g=(Token)match(input,GUID_LITERAL,FOLLOW_GUID_LITERAL_in_role668); if (state.failed) return value;

                    if ( state.backtracking==0 ) {	value = new GUID((g!=null?g.getText():null).substring(1)); }

                    }
                    break;
                case 2 :
                    // PDL.g:215:4: i= IDENT
                    {
                    i=(Token)match(input,IDENT,FOLLOW_IDENT_in_role679); if (state.failed) return value;

                    if ( state.backtracking==0 ) {
                    			value = interpreter.store.getId((i!=null?i.getText():null));
                    			if (value == null) {
                    				value = new GUID();
                    				System.out.println("Creating instance " + value + " with name "+ (i!=null?i.getText():null) + " in line " + (i!=null?i.getLine():0) + ".");
                    				interpreter.store.setName(value, (i!=null?i.getText():null));
                    			}
                    		}

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 14, role_StartIndex); }

        }
        return value;
    }
    // $ANTLR end "role"



    // $ANTLR start "queryRole"
    // PDL.g:226:1: queryRole returns [GUID value] : (r= role |v= VARIABLE );
    public final GUID queryRole() throws RecognitionException {
        GUID value = null;

        int queryRole_StartIndex = input.index();

        Token v=null;
        GUID r =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 15) ) { return value; }

            // PDL.g:227:2: (r= role |v= VARIABLE )
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==GUID_LITERAL||LA9_0==IDENT) ) {
                alt9=1;
            }
            else if ( (LA9_0==VARIABLE) ) {
                alt9=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;

            }
            switch (alt9) {
                case 1 :
                    // PDL.g:227:4: r= role
                    {
                    pushFollow(FOLLOW_role_in_queryRole700);
                    r=role();

                    state._fsp--;
                    if (state.failed) return value;

                    if ( state.backtracking==0 ) {	value = r; }

                    }
                    break;
                case 2 :
                    // PDL.g:229:4: v= VARIABLE
                    {
                    v=(Token)match(input,VARIABLE,FOLLOW_VARIABLE_in_queryRole711); if (state.failed) return value;

                    if ( state.backtracking==0 ) {	
                    			String name = (v!=null?v.getText():null).substring(1);
                    			value = variables.get(name); 
                    			if (value == null) {
                    				value = new Variable(name);
                    				variables.put(name, value);
                    			}
                    		}

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 15, queryRole_StartIndex); }

        }
        return value;
    }
    // $ANTLR end "queryRole"



    // $ANTLR start "actionRole"
    // PDL.g:240:1: actionRole returns [GUID value] : (r= queryRole |a= ACTION );
    public final GUID actionRole() throws RecognitionException {
        GUID value = null;

        int actionRole_StartIndex = input.index();

        Token a=null;
        GUID r =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 16) ) { return value; }

            // PDL.g:241:2: (r= queryRole |a= ACTION )
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==GUID_LITERAL||LA10_0==IDENT||LA10_0==VARIABLE) ) {
                alt10=1;
            }
            else if ( (LA10_0==ACTION) ) {
                alt10=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;

            }
            switch (alt10) {
                case 1 :
                    // PDL.g:241:4: r= queryRole
                    {
                    pushFollow(FOLLOW_queryRole_in_actionRole734);
                    r=queryRole();

                    state._fsp--;
                    if (state.failed) return value;

                    if ( state.backtracking==0 ) {	value = r; }

                    }
                    break;
                case 2 :
                    // PDL.g:243:5: a= ACTION
                    {
                    a=(Token)match(input,ACTION,FOLLOW_ACTION_in_actionRole747); if (state.failed) return value;

                    if ( state.backtracking==0 ) {	
                    			value = variables.get((a!=null?a.getText():null)); 
                    			if (value == null) {
                    				value = new Action((a!=null?a.getText():null));
                    				variables.put((a!=null?a.getText():null), value);
                    			}
                    		}

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 16, actionRole_StartIndex); }

        }
        return value;
    }
    // $ANTLR end "actionRole"



    // $ANTLR start "literal"
    // PDL.g:253:1: literal returns [Object value] : (g= GUID_LITERAL | HEX_LITERAL | OCTAL_LITERAL |d= DECIMAL_LITERAL |f= FLOATING_POINT_LITERAL |c= CHARACTER_LITERAL |s= STRING_LITERAL | 'true' | 'false' );
    public final Object literal() throws RecognitionException {
        Object value = null;

        int literal_StartIndex = input.index();

        Token g=null;
        Token d=null;
        Token f=null;
        Token c=null;
        Token s=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 17) ) { return value; }

            // PDL.g:254:5: (g= GUID_LITERAL | HEX_LITERAL | OCTAL_LITERAL |d= DECIMAL_LITERAL |f= FLOATING_POINT_LITERAL |c= CHARACTER_LITERAL |s= STRING_LITERAL | 'true' | 'false' )
            int alt11=9;
            switch ( input.LA(1) ) {
            case GUID_LITERAL:
                {
                alt11=1;
                }
                break;
            case HEX_LITERAL:
                {
                alt11=2;
                }
                break;
            case OCTAL_LITERAL:
                {
                alt11=3;
                }
                break;
            case DECIMAL_LITERAL:
                {
                alt11=4;
                }
                break;
            case FLOATING_POINT_LITERAL:
                {
                alt11=5;
                }
                break;
            case CHARACTER_LITERAL:
                {
                alt11=6;
                }
                break;
            case STRING_LITERAL:
                {
                alt11=7;
                }
                break;
            case 34:
                {
                alt11=8;
                }
                break;
            case 31:
                {
                alt11=9;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;

            }

            switch (alt11) {
                case 1 :
                    // PDL.g:254:9: g= GUID_LITERAL
                    {
                    g=(Token)match(input,GUID_LITERAL,FOLLOW_GUID_LITERAL_in_literal773); if (state.failed) return value;

                    if ( state.backtracking==0 ) {	value = new GUID((g!=null?g.getText():null).substring(1)); }

                    }
                    break;
                case 2 :
                    // PDL.g:256:7: HEX_LITERAL
                    {
                    match(input,HEX_LITERAL,FOLLOW_HEX_LITERAL_in_literal788); if (state.failed) return value;

                    }
                    break;
                case 3 :
                    // PDL.g:257:9: OCTAL_LITERAL
                    {
                    match(input,OCTAL_LITERAL,FOLLOW_OCTAL_LITERAL_in_literal798); if (state.failed) return value;

                    }
                    break;
                case 4 :
                    // PDL.g:258:9: d= DECIMAL_LITERAL
                    {
                    d=(Token)match(input,DECIMAL_LITERAL,FOLLOW_DECIMAL_LITERAL_in_literal810); if (state.failed) return value;

                    if ( state.backtracking==0 ) {	value = Long.parseLong((d!=null?d.getText():null)); }

                    }
                    break;
                case 5 :
                    // PDL.g:260:9: f= FLOATING_POINT_LITERAL
                    {
                    f=(Token)match(input,FLOATING_POINT_LITERAL,FOLLOW_FLOATING_POINT_LITERAL_in_literal829); if (state.failed) return value;

                    if ( state.backtracking==0 ) {	value = Double.parseDouble((f!=null?f.getText():null)); }

                    }
                    break;
                case 6 :
                    // PDL.g:262:9: c= CHARACTER_LITERAL
                    {
                    c=(Token)match(input,CHARACTER_LITERAL,FOLLOW_CHARACTER_LITERAL_in_literal848); if (state.failed) return value;

                    if ( state.backtracking==0 ) {	value = new Character((c!=null?c.getText():null).charAt(0)); }

                    }
                    break;
                case 7 :
                    // PDL.g:264:9: s= STRING_LITERAL
                    {
                    s=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_literal867); if (state.failed) return value;

                    if ( state.backtracking==0 ) {	value = (s!=null?s.getText():null).substring(1, (s!=null?s.getText():null).length()-1); }

                    }
                    break;
                case 8 :
                    // PDL.g:266:9: 'true'
                    {
                    match(input,34,FOLLOW_34_in_literal884); if (state.failed) return value;

                    if ( state.backtracking==0 ) {	value = true; }

                    }
                    break;
                case 9 :
                    // PDL.g:268:7: 'false'
                    {
                    match(input,31,FOLLOW_31_in_literal900); if (state.failed) return value;

                    if ( state.backtracking==0 ) {	value = false; }

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 17, literal_StartIndex); }

        }
        return value;
    }
    // $ANTLR end "literal"

    // Delegated rules


    protected DFA2 dfa2 = new DFA2(this);
    static final String DFA2_eotS =
        "\106\uffff";
    static final String DFA2_eofS =
        "\106\uffff";
    static final String DFA2_minS =
        "\1\5\13\14\3\uffff\5\5\25\32\1\uffff\1\5\2\uffff\13\14\3\5\13\32";
    static final String DFA2_maxS =
        "\1\42\13\30\3\uffff\5\42\25\35\1\uffff\1\42\2\uffff\13\30\3\42\13"+
        "\35";
    static final String DFA2_acceptS =
        "\14\uffff\1\4\1\5\1\6\32\uffff\1\1\1\uffff\1\2\1\3\31\uffff";
    static final String DFA2_specialS =
        "\106\uffff}>";
    static final String[] DFA2_transitionS = {
            "\1\6\1\uffff\1\4\2\uffff\1\5\1\uffff\1\1\1\uffff\1\2\1\12\5"+
            "\uffff\1\3\1\7\1\uffff\1\13\5\uffff\1\14\1\11\1\15\1\16\1\10",
            "\1\17\2\uffff\1\20\10\uffff\1\21",
            "\1\17\2\uffff\1\20\10\uffff\1\21",
            "\1\17\2\uffff\1\20\10\uffff\1\21",
            "\1\17\2\uffff\1\20\10\uffff\1\21",
            "\1\17\2\uffff\1\20\10\uffff\1\21",
            "\1\17\2\uffff\1\20\10\uffff\1\21",
            "\1\17\2\uffff\1\20\10\uffff\1\21",
            "\1\17\2\uffff\1\20\10\uffff\1\21",
            "\1\17\2\uffff\1\20\10\uffff\1\21",
            "\1\17\2\uffff\1\20\10\uffff\1\21",
            "\1\22\2\uffff\1\23\10\uffff\1\21",
            "",
            "",
            "",
            "\1\31\1\uffff\1\27\2\uffff\1\30\1\uffff\1\24\1\uffff\1\25\1"+
            "\35\5\uffff\1\26\1\32\1\uffff\1\36\6\uffff\1\34\2\uffff\1\33",
            "\1\31\1\uffff\1\27\2\uffff\1\30\1\uffff\1\24\1\uffff\1\25\1"+
            "\35\5\uffff\1\26\1\32\1\uffff\1\36\6\uffff\1\34\2\uffff\1\33",
            "\1\44\1\uffff\1\42\2\uffff\1\43\1\uffff\1\37\1\uffff\1\40\1"+
            "\50\5\uffff\1\41\1\45\1\uffff\1\36\6\uffff\1\47\2\uffff\1\46",
            "\1\44\1\uffff\1\42\2\uffff\1\43\1\uffff\1\37\1\uffff\1\40\1"+
            "\50\5\uffff\1\41\1\45\1\uffff\1\36\6\uffff\1\47\2\uffff\1\46",
            "\1\44\1\uffff\1\42\2\uffff\1\43\1\uffff\1\37\1\uffff\1\40\1"+
            "\50\5\uffff\1\41\1\45\1\uffff\1\36\6\uffff\1\47\2\uffff\1\46",
            "\1\52\1\54\1\51\1\53",
            "\1\52\1\54\1\51\1\53",
            "\1\52\1\54\1\51\1\53",
            "\1\52\1\54\1\51\1\53",
            "\1\52\1\54\1\51\1\53",
            "\1\52\1\54\1\51\1\53",
            "\1\52\1\54\1\51\1\53",
            "\1\52\1\54\1\51\1\53",
            "\1\52\1\54\1\51\1\53",
            "\1\52\1\54\1\51\1\53",
            "\1\52\1\54\1\uffff\1\53",
            "\1\52\1\54\1\uffff\1\53",
            "\1\52\1\54\1\uffff\1\53",
            "\1\52\1\54\1\uffff\1\53",
            "\1\52\1\54\1\uffff\1\53",
            "\1\52\1\54\1\uffff\1\53",
            "\1\52\1\54\1\uffff\1\53",
            "\1\52\1\54\1\uffff\1\53",
            "\1\52\1\54\1\uffff\1\53",
            "\1\52\1\54\1\uffff\1\53",
            "\1\52\1\54\1\uffff\1\53",
            "",
            "\1\62\1\uffff\1\60\2\uffff\1\61\1\uffff\1\55\1\uffff\1\56\1"+
            "\66\5\uffff\1\57\1\63\1\uffff\1\67\6\uffff\1\65\2\uffff\1\64",
            "",
            "",
            "\1\70\2\uffff\1\71\10\uffff\1\72",
            "\1\70\2\uffff\1\71\10\uffff\1\72",
            "\1\70\2\uffff\1\71\10\uffff\1\72",
            "\1\70\2\uffff\1\71\10\uffff\1\72",
            "\1\70\2\uffff\1\71\10\uffff\1\72",
            "\1\70\2\uffff\1\71\10\uffff\1\72",
            "\1\70\2\uffff\1\71\10\uffff\1\72",
            "\1\70\2\uffff\1\71\10\uffff\1\72",
            "\1\70\2\uffff\1\71\10\uffff\1\72",
            "\1\70\2\uffff\1\71\10\uffff\1\72",
            "\1\70\2\uffff\1\71\10\uffff\1\72",
            "\1\100\1\uffff\1\76\2\uffff\1\77\1\uffff\1\73\1\uffff\1\74"+
            "\1\104\5\uffff\1\75\1\101\1\uffff\1\105\6\uffff\1\103\2\uffff"+
            "\1\102",
            "\1\100\1\uffff\1\76\2\uffff\1\77\1\uffff\1\73\1\uffff\1\74"+
            "\1\104\5\uffff\1\75\1\101\1\uffff\1\105\6\uffff\1\103\2\uffff"+
            "\1\102",
            "\1\100\1\uffff\1\76\2\uffff\1\77\1\uffff\1\73\1\uffff\1\74"+
            "\1\104\5\uffff\1\75\1\101\1\uffff\1\105\6\uffff\1\103\2\uffff"+
            "\1\102",
            "\1\52\1\54\1\uffff\1\53",
            "\1\52\1\54\1\uffff\1\53",
            "\1\52\1\54\1\uffff\1\53",
            "\1\52\1\54\1\uffff\1\53",
            "\1\52\1\54\1\uffff\1\53",
            "\1\52\1\54\1\uffff\1\53",
            "\1\52\1\54\1\uffff\1\53",
            "\1\52\1\54\1\uffff\1\53",
            "\1\52\1\54\1\uffff\1\53",
            "\1\52\1\54\1\uffff\1\53",
            "\1\52\1\54\1\uffff\1\53"
    };

    static final short[] DFA2_eot = DFA.unpackEncodedString(DFA2_eotS);
    static final short[] DFA2_eof = DFA.unpackEncodedString(DFA2_eofS);
    static final char[] DFA2_min = DFA.unpackEncodedStringToUnsignedChars(DFA2_minS);
    static final char[] DFA2_max = DFA.unpackEncodedStringToUnsignedChars(DFA2_maxS);
    static final short[] DFA2_accept = DFA.unpackEncodedString(DFA2_acceptS);
    static final short[] DFA2_special = DFA.unpackEncodedString(DFA2_specialS);
    static final short[][] DFA2_transition;

    static {
        int numStates = DFA2_transitionS.length;
        DFA2_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA2_transition[i] = DFA.unpackEncodedString(DFA2_transitionS[i]);
        }
    }

    class DFA2 extends DFA {

        public DFA2(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 2;
            this.eot = DFA2_eot;
            this.eof = DFA2_eof;
            this.min = DFA2_min;
            this.max = DFA2_max;
            this.accept = DFA2_accept;
            this.special = DFA2_special;
            this.transition = DFA2_transition;
        }
        public String getDescription() {
            return "73:1: statement returns [Object value] : (t= tuple ';' |q= query '?' |r= rule ';' | 'commit' ';' | 'open' s= IDENT ';' | 'run' f= IDENT ';' );";
        }
    }
 

    public static final BitSet FOLLOW_statement_in_pdlScript85 = new BitSet(new long[]{0x00000007C160D4A0L});
    public static final BitSet FOLLOW_EOF_in_pdlScript107 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_tuple_in_statement129 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_28_in_statement131 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_query_in_statement142 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_29_in_statement144 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule_in_statement158 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_28_in_statement160 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_30_in_statement175 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_28_in_statement177 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_32_in_statement192 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_IDENT_in_statement196 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_28_in_statement198 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_statement210 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_IDENT_in_statement214 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_28_in_statement216 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_instance_in_tuple248 = new BitSet(new long[]{0x0000000000009000L});
    public static final BitSet FOLLOW_role_in_tuple252 = new BitSet(new long[]{0x000000048060D4A0L});
    public static final BitSet FOLLOW_instance_in_tuple256 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_query_in_rule287 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_rule289 = new BitSet(new long[]{0x000000048160D4B0L});
    public static final BitSet FOLLOW_postcondition_in_rule293 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_queryTuple_in_query322 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_26_in_query333 = new BitSet(new long[]{0x000000048160D4A0L});
    public static final BitSet FOLLOW_queryTuple_in_query337 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_actionTuple_in_postcondition373 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_26_in_postcondition383 = new BitSet(new long[]{0x000000048160D4B0L});
    public static final BitSet FOLLOW_actionTuple_in_postcondition387 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_queryInstance_in_queryTuple419 = new BitSet(new long[]{0x0000000001009000L});
    public static final BitSet FOLLOW_queryRole_in_queryTuple429 = new BitSet(new long[]{0x000000048160D4A0L});
    public static final BitSet FOLLOW_queryInstance_in_queryTuple439 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_actionInstance_in_actionTuple472 = new BitSet(new long[]{0x0000000001009010L});
    public static final BitSet FOLLOW_actionRole_in_actionTuple482 = new BitSet(new long[]{0x000000048160D4B0L});
    public static final BitSet FOLLOW_actionInstance_in_actionTuple491 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_instance519 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENT_in_instance532 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_instance_in_queryInstance554 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variable_in_queryInstance567 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_queryInstance_in_actionInstance590 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_action_in_actionInstance602 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VARIABLE_in_variable624 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ACTION_in_action646 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GUID_LITERAL_in_role668 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENT_in_role679 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_role_in_queryRole700 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VARIABLE_in_queryRole711 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_queryRole_in_actionRole734 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ACTION_in_actionRole747 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GUID_LITERAL_in_literal773 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_HEX_LITERAL_in_literal788 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OCTAL_LITERAL_in_literal798 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DECIMAL_LITERAL_in_literal810 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOATING_POINT_LITERAL_in_literal829 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CHARACTER_LITERAL_in_literal848 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_literal867 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_34_in_literal884 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_31_in_literal900 = new BitSet(new long[]{0x0000000000000002L});

}