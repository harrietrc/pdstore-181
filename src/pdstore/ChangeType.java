/**
 * 
 */
package pdstore;

public enum ChangeType {
	LINK_ADDED, LINK_REMOVED, 

	/**
	 * Used only in Changes that are Query templates. Means that only 
	 * the last change object is returned that determines the status of the link.
	 * This means, the resulting changes can have ChangeType LINK_ADDED or LINK_REMOVED.
	 * 
	 */
	LINK_EFFECTIVE,
	
	/**
	 * Used only in Changes that are Query templates. 
	 * Returns a subset of the changes returned by LINK_EFFECTIVE, namely only resulting 
	 * changes with ChangeType LINK_ADDED
     * are returned.
	 * 
	 */
	LINK_EFFECTIVELY_ADDED,
	
	/**
	 * Used only in Changes that are Query templates. Means that transactions
	 * have to match exactly.
	 * 
	 */
	
	LINK_NOW_ADDED,
	/**
	 * Used only in Changes that are Query templates. Means that transactions
	 * have to match exactly.
	 * 
	 */
	LINK_NOW_REMOVED,
	
	/**
	 * Used only in Changes that are Query templates. Matches any Change type.
	 * 
	 */
	ANY_CHANGE_TYPE;
	
	public boolean isEffective() {
		return this.equals(LINK_EFFECTIVE) || this.equals(LINK_EFFECTIVELY_ADDED);
	}


	public static String toPrettyString(ChangeType changeType) {
		if (changeType == ChangeType.LINK_ADDED)
			return "+";
		else if (changeType == ChangeType.LINK_REMOVED)
			return "-";
		else if (changeType == ChangeType.LINK_EFFECTIVE)
			return "EFFECTIVE";
		else if (changeType == null)
			return "null";
		else
			return changeType.toString();
	}
}