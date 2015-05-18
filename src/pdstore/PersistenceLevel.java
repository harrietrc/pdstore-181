package pdstore;

public enum PersistenceLevel {
	TRANSACTION_ONLY,
	INDEX_ONLY,
	LOG_ONLY,
	LOG_AND_INDEX
}
