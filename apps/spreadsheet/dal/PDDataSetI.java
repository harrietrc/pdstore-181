package spreadsheet.dal;

import java.util.Collection;
import pdstore.GUID;

/**
 *Interface for the Data access class of type "DataSet" in memory.
 * @author PDGen
 */
public interface PDDataSetI{

	/**
	 * Returns the instance connected to this instance through the role Records.
	 * @return the connected instance
	 */
	public PDDataRecord getRecords();

	/**
	 * Returns the instance(s) connected to this instance through the role Records.
	 * @return the connected instance(s)
	 */
	 public Collection<PDDataRecord> getRecordss() ;

   /**
	 * Connects this instance to the given instance using role "Records".
	 * If the given instance is null, nothing happens.
	 * @param records the instance to connect
	 */
	public void addRecords(GUID records);
	/**
	 * Connects this instance to the given instance using role "Records".
	 * If the given instance is null, nothing happens.
	 * @param records the instance to connect
	 */
	public void addRecords(PDDataRecord records);

	/**
	 * Connects this instance to the given instance using role "Records".
	 * If the given collection of instances is null, nothing happens.
	 * @param records the Collection of instances to connect
	 */
	public void addRecordss(Collection<PDDataRecord> recordss) ;

	/**
	 * Removes the link from this instance through role "Records".
	 */
	public void removeRecords() ;

	/**
	 * Removes the link from this instance through role "Records" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeRecords(PDDataRecord records);

	/**
	 * Removes the links from this instance through role "Records" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeRecordss(Collection<PDDataRecord> recordss) ;

	/**
	 * Connects this instance to the given instance using role "Records".
	 * If there is already an instance connected to this instance through role "Records", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param records the instance to connect
	 */
	public void setRecords(GUID records);
	/**
	 * Connects this instance to the given instance using role "Records".
	 * If there is already an instance connected to this instance through role "Records", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param records the instance to connect
	 */
	public void setRecords(PDDataRecord records) ;
}
