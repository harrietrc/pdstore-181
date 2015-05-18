package spreadsheet.dal;

import java.util.Collection;
import pdstore.GUID;

/**
 *Interface for the Data access class of type "DataRecord" in memory.
 * @author PDGen
 */
public interface PDDataRecordI{

	/**
	 * Returns the instance connected to this instance through the role leader.
	 * @return the connected instance
	 */
	public String getLeader();

	/**
	 * Returns the instance(s) connected to this instance through the role leader.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getLeaders() ;

   /**
	 * Connects this instance to the given instance using role "leader".
	 * If the given instance is null, nothing happens.
	 * @param leader the instance to connect
	 */
	public void addLeader(String leader);
	/**
	 * Connects this instance to the given instances using role "leader".
	 * If the given collection of instances is null, nothing happens.
	 * @param leader the Collection of instances to connect
	 */
	public void addLeaders(Collection<String> leaders);

	/**
	 * Removes the link from this instance through role "leader".
	 */
	public void removeLeader() ;

	/**
	 * Removes the link from this instance through role "leader" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeLeader(String leader);

	/**
	 * Connects this instance to the given instance using role "leader".
	 * If there is already an instance connected to this instance through role "leader", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param leader the instance to connect
	 */
	public void setLeader(String leader);
	/**
	 * Returns the instance connected to this instance through the role participantNo.
	 * @return the connected instance
	 */
	public Long getParticipantNo();

	/**
	 * Returns the instance(s) connected to this instance through the role participantNo.
	 * @return the connected instance(s)
	 */
	 public Collection<Long> getParticipantNos() ;

   /**
	 * Connects this instance to the given instance using role "participantNo".
	 * If the given instance is null, nothing happens.
	 * @param participantNo the instance to connect
	 */
	public void addParticipantNo(Long participantNo);
	/**
	 * Connects this instance to the given instances using role "participantNo".
	 * If the given collection of instances is null, nothing happens.
	 * @param participantNo the Collection of instances to connect
	 */
	public void addParticipantNos(Collection<Long> participantNos);

	/**
	 * Removes the link from this instance through role "participantNo".
	 */
	public void removeParticipantNo() ;

	/**
	 * Removes the link from this instance through role "participantNo" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeParticipantNo(Long participantNo);

	/**
	 * Connects this instance to the given instance using role "participantNo".
	 * If there is already an instance connected to this instance through role "participantNo", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param participantNo the instance to connect
	 */
	public void setParticipantNo(Long participantNo);
	/**
	 * Returns the instance connected to this instance through the role taskNo.
	 * @return the connected instance
	 */
	public Long getTaskNo();

	/**
	 * Returns the instance(s) connected to this instance through the role taskNo.
	 * @return the connected instance(s)
	 */
	 public Collection<Long> getTaskNos() ;

   /**
	 * Connects this instance to the given instance using role "taskNo".
	 * If the given instance is null, nothing happens.
	 * @param taskNo the instance to connect
	 */
	public void addTaskNo(Long taskNo);
	/**
	 * Connects this instance to the given instances using role "taskNo".
	 * If the given collection of instances is null, nothing happens.
	 * @param taskNo the Collection of instances to connect
	 */
	public void addTaskNos(Collection<Long> taskNos);

	/**
	 * Removes the link from this instance through role "taskNo".
	 */
	public void removeTaskNo() ;

	/**
	 * Removes the link from this instance through role "taskNo" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeTaskNo(Long taskNo);

	/**
	 * Connects this instance to the given instance using role "taskNo".
	 * If there is already an instance connected to this instance through role "taskNo", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param taskNo the instance to connect
	 */
	public void setTaskNo(Long taskNo);
	/**
	 * Returns the instance connected to this instance through the role age.
	 * @return the connected instance
	 */
	public Long getAge();

	/**
	 * Returns the instance(s) connected to this instance through the role age.
	 * @return the connected instance(s)
	 */
	 public Collection<Long> getAges() ;

   /**
	 * Connects this instance to the given instance using role "age".
	 * If the given instance is null, nothing happens.
	 * @param age the instance to connect
	 */
	public void addAge(Long age);
	/**
	 * Connects this instance to the given instances using role "age".
	 * If the given collection of instances is null, nothing happens.
	 * @param age the Collection of instances to connect
	 */
	public void addAges(Collection<Long> ages);

	/**
	 * Removes the link from this instance through role "age".
	 */
	public void removeAge() ;

	/**
	 * Removes the link from this instance through role "age" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeAge(Long age);

	/**
	 * Connects this instance to the given instance using role "age".
	 * If there is already an instance connected to this instance through role "age", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param age the instance to connect
	 */
	public void setAge(Long age);
	/**
	 * Returns the instance connected to this instance through the role is in.
	 * @return the connected instance
	 */
	public PDDataSet getIsIn();

	/**
	 * Returns the instance(s) connected to this instance through the role is in.
	 * @return the connected instance(s)
	 */
	 public Collection<PDDataSet> getIsIns() ;

   /**
	 * Connects this instance to the given instance using role "is in".
	 * If the given instance is null, nothing happens.
	 * @param isIn the instance to connect
	 */
	public void addIsIn(GUID isIn);
	/**
	 * Connects this instance to the given instance using role "is in".
	 * If the given instance is null, nothing happens.
	 * @param isIn the instance to connect
	 */
	public void addIsIn(PDDataSet isIn);

	/**
	 * Connects this instance to the given instance using role "is in".
	 * If the given collection of instances is null, nothing happens.
	 * @param isIn the Collection of instances to connect
	 */
	public void addIsIns(Collection<PDDataSet> isIns) ;

	/**
	 * Removes the link from this instance through role "is in".
	 */
	public void removeIsIn() ;

	/**
	 * Removes the link from this instance through role "is in" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeIsIn(PDDataSet isIn);

	/**
	 * Removes the links from this instance through role "is in" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeIsIns(Collection<PDDataSet> isIns) ;

	/**
	 * Connects this instance to the given instance using role "is in".
	 * If there is already an instance connected to this instance through role "is in", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param isIn the instance to connect
	 */
	public void setIsIn(GUID isIn);
	/**
	 * Connects this instance to the given instance using role "is in".
	 * If there is already an instance connected to this instance through role "is in", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param isIn the instance to connect
	 */
	public void setIsIn(PDDataSet isIn) ;
}
