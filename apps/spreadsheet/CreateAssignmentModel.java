package spreadsheet;

import pdstore.GUID;
import pdstore.PDStore;
import pdstore.dal.PDGen;
import pdstore.dal.PDSimpleWorkingCopy;
import pdstore.dal.PDWorkingCopy;

public class CreateAssignmentModel {

	public final static GUID DATA_MODELID = new GUID(
	        "2e2920c0a25611e19ed778e4009ed4de");
	public final static GUID DATASET_TYPEID = new GUID(
	        "2e2b6ab0a25611e19ed778e4009ed4de");
	public final static GUID DATARECORD_TYPEID = new GUID(
	        "2e2b6ab1a25611e19ed778e4009ed4de");
	
	public final static GUID CONTAINS_ROLE = new GUID(
			"2e2b6ab7a25611e19ed778e4009ed4de");
	public final static GUID AGE_ROLE = new GUID(
			"b6cac050a25b11e196b978e4009ed4de");
	public final static GUID TASK_NO_ROLE = new GUID(
			"b6cd0a40a25b11e196b978e4009ed4de");
	public final static GUID PARTICIPANT_NO_ROLE =  new GUID(
			"b6cd0a41a25b11e196b978e4009ed4de");
	public final static GUID LEADER_ROLE = new GUID(
			"b6cd0a42a25b11e196b978e4009ed4de");
	
	public final static GUID dataRecord1 = new GUID(
	        "2196c134a39811e1951078e4009ed4de");
	public final static GUID dataSet1 = new GUID(
	        "2196c135a39811e1951078e4009ed4de");


	public final static GUID dataRecord2 = new GUID(
	        "a14e0e34a3b911e1ab9d78e4009ed4de");
	public final static GUID dataSet2 = new GUID(
	        "a14e0e35a3b911e1ab9d78e4009ed4de");

	
	private void CreateAssignmentModel(PDStore store) {
		GUID transaction = store.begin();
		
		// Create central Data Model
		store.createModel(transaction, DATA_MODELID, "Data");
		
		// Create types
		store.createType(transaction, DATA_MODELID, DATASET_TYPEID, "DataSet");
		store.createType(transaction,DATA_MODELID, DATARECORD_TYPEID, "DataRecord");
		
		//Roles for type Dataset.
		store.createRelation(transaction, CreateAssignmentModel.DATASET_TYPEID, "is in",
				"Records", CreateAssignmentModel.CONTAINS_ROLE,
				CreateAssignmentModel.DATARECORD_TYPEID);
		
		// Roles for type DataRecord.
		store.createRelation(transaction,DATARECORD_TYPEID, null,
				"age", AGE_ROLE, PDStore.INTEGER_TYPEID);
		store.createRelation(transaction,DATARECORD_TYPEID, null,
				"taskNo", TASK_NO_ROLE, PDStore.INTEGER_TYPEID);
		store.createRelation(transaction, DATARECORD_TYPEID, null,
				"participantNo",PARTICIPANT_NO_ROLE, PDStore.INTEGER_TYPEID);
		store.createRelation(transaction,DATARECORD_TYPEID, null,
				"leader", LEADER_ROLE, PDStore.STRING_TYPEID);

		store.commit(transaction);
	}

	public void createDALClasses(PDStore store) {
		PDGen.generateModel(store, "Data", "apps", "assignment.dal");
	}

	public void addData(PDStore store){
		//add data into the database.
		GUID transaction = store.begin();
		store.setName(transaction, dataSet1, "data set 1");
		store.setType(transaction, dataSet1, DATASET_TYPEID);
		store.setName(transaction, dataRecord1, "data record 1");
		store.setType(transaction, dataRecord1,DATARECORD_TYPEID);
		
		store.addLink(transaction, dataRecord1, AGE_ROLE, 18);
		store.addLink(transaction, dataRecord1,PARTICIPANT_NO_ROLE, 2);
		store.addLink(transaction, dataRecord1,LEADER_ROLE, "Tony");
		store.addLink(transaction, dataRecord1, TASK_NO_ROLE, 3);
		store.addLink(transaction, dataSet1, CONTAINS_ROLE, dataRecord1);
		
		store.setName(transaction, dataSet2, "data set 2");
		store.setType(transaction, dataSet2, DATASET_TYPEID);
		store.setName(transaction, dataRecord2, "data record 2");
		store.setType(transaction, dataRecord2,DATARECORD_TYPEID);
		
		store.addLink(transaction, dataRecord2, AGE_ROLE, 21);
		store.addLink(transaction, dataRecord2,PARTICIPANT_NO_ROLE, 35);
		store.addLink(transaction, dataRecord2,LEADER_ROLE, "Simon");
		store.addLink(transaction, dataRecord2, TASK_NO_ROLE, 52);
		//change to dataset1/2 to test.
		store.addLink(transaction, dataSet1, CONTAINS_ROLE, dataRecord2);
		
		store.commit(transaction);
		
	}
	
	public static void main(String[] args) {
		CreateAssignmentModel m = new CreateAssignmentModel();
		PDStore store = new PDStore("AssignmentDatabase");
		m.CreateAssignmentModel(store);
		
		m.addData(store);
		m.createDALClasses(store);
	}
}

