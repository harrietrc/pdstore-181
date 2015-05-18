package pdstore;

public interface OperationI {

	/**
	 * This method implements an operation, i.e. it is executed whenever the
	 * operation is applied. Usually implementations of this are not called
	 * directly, but through PDStore.applyMethod().
	 * 
	 * @param store
	 *            The store to read/write from when applying the operation.
	 * @param transaction
	 *            The transaction the operation application should work on.
	 * @param parameter
	 *            The superparameter that should be used for the operation
	 *            application. This needs to be the parameter the operation
	 *            expects.
	 * @return The return value of an operation is operation-specific.
	 */
	public Object apply(PDStore store, GUID transaction, Object parameter);
}
