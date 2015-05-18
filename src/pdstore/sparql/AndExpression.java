package pdstore.sparql;

import java.util.HashSet;
import java.util.Set;

public class AndExpression implements FilterExpression {
	
	public FilterExpression[] args;
	boolean notNegated = true; 
	
	public AndExpression(FilterExpression... args) {
		super();
		this.args = args;
	}
	
	public AndExpression(boolean notNegated, FilterExpression... args) {
		super();
		this.args = args;
		this.notNegated = notNegated;
	}

	
	@Override
	public boolean evaluate(Assignment assignment) {
		for (FilterExpression expr : args)
			if(!expr.evaluate(assignment))
				return !notNegated;
		return notNegated; 
	}
	
	@Override
	public String toString(){
		String andString;
		andString = "( ";
		for (int i =0; i< args.length; i++){
			andString+= args[i].toString();
			if (i == args.length-1)
				break;
			andString+= " && ";
		}
		andString += " ) ";
		return andString;
	}

	@Override
	public Set<Variable> getVariables() {
		Set<Variable> variables = new HashSet<Variable>();
		for (FilterExpression expr : args){
			variables.addAll(expr.getVariables());
		}
		return variables;
	}
	
}
