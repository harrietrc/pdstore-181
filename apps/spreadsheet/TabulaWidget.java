package spreadsheet;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import pdstore.GUID;
import pdstore.PDChange;
import pdstore.PDStore;
import pdstore.dal.PDSimpleWorkingCopy;
import pdstore.dal.PDWorkingCopy;
import pdstore.generic.PDCoreI;
import pdstore.notify.PDListener;
import pdstore.notify.PDListenerAdapter;
import spreadsheet.dal.PDDataRecord;
import spreadsheet.dal.PDDataSet;

public class TabulaWidget extends JFrame{

	private PDStore mainStore;
	private TabulaTable table;
	private GUID datasetId;
	private GUID type;
	private GUID recordType;
	private Collection<Object> records;
	private List<Object> sortedRecords;
	
	public TabulaWidget(PDStore pdstore, Object instance) {

		//Set variables.
		sortedRecords = new ArrayList<Object>();
		mainStore = pdstore;
		datasetId = (GUID)instance;
		
		//Setup basic GUI.
		this.setTitle("Database Model");
		this.setLayout(new BorderLayout());
		this.setSize(500, 550);
		this.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Start transaction.
		GUID transaction = mainStore.begin();
		
		//Query for records.
		type = mainStore.getType(transaction, datasetId);
		Collection<GUID> roles = mainStore.getAccessibleRoles(transaction, type);
		records = mainStore.getInstances(transaction, instance, roles.iterator().next());
		recordType = mainStore.getType(transaction, records.iterator().next());
		
		//Setup table.
		table = new TabulaTable(new DefaultTableModel());
		JScrollPane scrollPane = new JScrollPane(table);
		this.add(scrollPane, BorderLayout.PAGE_START);
		//Add rows and columns.
		table.addColumns(mainStore, recordType);
		for(Object record : records) {
			sortedRecords.add(record);
			table.addRow(mainStore, (GUID)record);
		}
		//Add stats to table row.
		table.addStats();
		
		//Button to insert row.
		JButton button = new JButton("Insert Row");
		button.addActionListener(new ActionListener() { 
            public synchronized void actionPerformed(ActionEvent e)
            {
            	//Create new record.
        		PDDataSet dset = PDDataSet.load(mainStore, datasetId);
        		PDDataRecord rec = PDDataRecord.load(mainStore, (GUID)records.iterator().next());
        		rec = new PDDataRecord(mainStore);
        		//Set default values for the new record.
        		rec.setAge((long)0);
        		rec.setParticipantNo((long)0);
        		rec.setTaskNo((long)0);
        		rec.setLeader("");
        		//Add record to the dataSet.
        		dset.addRecords(rec);
        		mainStore.commit();
            }
        });
		this.add(button, BorderLayout.PAGE_END);
		
		// Add detached listener.
		List<PDListener<GUID,Object,GUID>> listeners = mainStore.getDetachedListenerList();
		listeners.add(new PDListenerAdapter<GUID,Object,GUID>(){
			public synchronized void transactionCommitted(
					List<PDChange<GUID, Object, GUID>> transaction,
					List<PDChange<GUID, Object, GUID>> matchedChanges, PDCoreI<GUID, Object, GUID> core){
				for (PDChange<GUID, Object, GUID> change : transaction) {
					recordChanged(change);	
				}
			}
		});
		
		//Add table model listener.
		table.model.addTableModelListener(new TableModelListener() {
			public synchronized void tableChanged(TableModelEvent e) {
				if(e.getType() == TableModelEvent.UPDATE){
					updateRecordModel(e.getFirstRow());
				}
			}
		});
		
		this.setVisible(true);
	}
	
	//Called when any records are changed.
	private void recordChanged(PDChange<GUID, Object, GUID> change) {
		//Check to see if records is in the existing list.
		if(records.contains(change.getInstance1())) {
			//Update rows if in existing records.
			int row = sortedRecords.indexOf(change.getInstance1());
			table.updateRow(mainStore, (GUID)change.getInstance1(), row);	
		} else if(datasetId.equals((GUID) change.getInstance1())) {
			//If new record, add to dataSet and update table.
			records.add(change.getInstance2());
			sortedRecords.add(change.getInstance2());
			table.clearStats();
			table.addRow(mainStore, (GUID)change.getInstance2());
			table.addStats();
		}
	}
	
	//Called when the table is changed.
	private void updateRecordModel(int row) {
		//Load the record that was changed for editing.
		PDDataRecord rec = PDDataRecord.load(mainStore, (GUID)sortedRecords.get(row));
		
		//Type check for the numeric fields
		long age;
		long taskNo;
		long participantNo;
		try{
			age = (Long.parseLong(table.model.getValueAt(row, 0).toString()));
			taskNo = (Long.parseLong(table.model.getValueAt(row, 1).toString()));
			participantNo = (Long.parseLong(table.model.getValueAt(row, 2).toString()));
			//Set new record values.
			rec.setAge(age);
			rec.setTaskNo(taskNo);
			rec.setParticipantNo(participantNo);
			rec.setLeader((String)table.model.getValueAt(row, 3));
		} catch (NumberFormatException e){
			System.out.println("num error");
			return;
		} catch (Exception e){
			System.out.println("casting error");
			e.printStackTrace();
		}
		
		mainStore.commit();
		
	}
	
	public static void main(String[] args) {
		
		try {
			Class.forName("assignment.dal.PDDataRecord");
			Class.forName("assignment.dal.PDDataSet");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		//Get model of interest and set up store.
		PDStore store = new PDStore("AssignmentDatabase");
		PDWorkingCopy copy = new PDSimpleWorkingCopy(store);	
		GUID id = CreateAssignmentModel.dataSet1;

		//Set up two widgets.
		TabulaWidget table = new TabulaWidget(store, id);
		TabulaWidget table2 = new TabulaWidget(store, id);
	}

}

