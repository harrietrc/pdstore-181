package pdstore.sparql;


public class EqualExpression extends BinaryFilterOperator {

	public EqualExpression(Object arg1, Object arg2) {
		super();
		this.arg1 = arg1;
		this.arg2 = arg2;
	}
	
	
	@Override
	protected boolean binaryEvaluate(Object first, Object second) {
		return first.equals(second);
	}


}
