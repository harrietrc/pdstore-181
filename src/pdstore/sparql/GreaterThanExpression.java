package pdstore.sparql;

import pdstore.changelog.PrimitiveType;

public class GreaterThanExpression extends BinaryFilterOperator {

	public GreaterThanExpression(Object arg1, Object arg2) {
		super();
		this.arg1 = arg1;
		this.arg2 = arg2;
	}


	@Override
	protected boolean binaryEvaluate(Object first, Object second) {
		return PrimitiveType.compare(first, second) > 0;
	}

}
