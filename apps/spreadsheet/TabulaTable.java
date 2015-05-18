package spreadsheet;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.*;
import pdstore.GUID;
import pdstore.PDStore;

public class TabulaTable extends JTable {
	
	public DefaultTableModel model;
	public Collection<GUID> recordRoles;
	
	public TabulaTable(TableModel tableModel) {
		//Construct table and set up TableModel.
		super(tableModel);
		model = (DefaultTableModel) tableModel;
	}
	
	//Add rows to the table. Initially called to populate table.
	public void addRow(PDStore store, GUID recordId) {
		//Begin transaction.
		GUID transaction = store.begin();
		List<String> rowValues = new ArrayList<String>();
		//Check that the role is not the one set between the dataSet and dataRecord as this 
		//should not be put into the table.
		for(GUID role : recordRoles) {
			if(store.getNameOrValue(transaction, role).toString().equals("is in")) {
				continue;
			}
			//Get the role value and add it to the ArrayList.
			Collection<Object> values = store.getInstances(transaction, recordId, role);
			rowValues.add(store.getNameOrValue(transaction, values.iterator().next()).toString());
		}
		//Add the ArrayList to the table model.
		model.addRow(rowValues.toArray());
	}
	
	//Add column names to the table model.
	public void addColumns(PDStore store, GUID recordType) {
		//Start a transaction.
		GUID transaction = store.begin();
		recordRoles = store.getAccessibleRoles(transaction, recordType);
		//Check that the role is not the one set between the dataSet and dataRecord as this 
		//should not be put into the table.
		for (GUID role : recordRoles) {
			if(store.getNameOrValue(transaction, role).toString().equals("is in")) {
				continue;
			}
			//Get column names and add them to the table model.
			String columnNames = store.getNameOrValue(transaction, role).toString();
			model.addColumn(columnNames);
		}
	}
	
	//Add the stats row to the table model.
	public void addStats() {
		List<String> rowValues = new ArrayList<String>();
		
		//Loop through all columns.
		for(int i = 0; i < model.getColumnCount(); i++) {	
			//If value is a number then insert and average and standard deviation, else insert the mode.
			if(isNumber(model.getValueAt(0, i))) {
				rowValues.add("Average: " + (float)getAverage(i) + "     SD: " + (float)getSD(i));
			} else {
				rowValues.add("Mode: " + getMode(i));
			}
		}
		//Add the new row to the table model.
		model.addRow(rowValues.toArray());
	}
	
	//Remove the stats table when an update is made so new stats can be calculated.
	public void clearStats(){
		model.removeRow(model.getRowCount()-1);
	}
	
	//Update a row when a user changes the input.
	public void updateRow(PDStore store, GUID recordId, int row) {
		//Start new transaction.
		GUID transaction = store.begin();
		//Remove the stats row so new stats can be calculated.
		clearStats();
		
		int column = 0;
		for(GUID role : recordRoles) {
			//Check that the role is not the one set between the dataSet and dataRecord as this 
			//should not be put into the table.
			if(store.getNameOrValue(transaction, role).toString().equals("is in")) {
				continue;
			}
			
			//Get row values from record and then set them in the table..
			Collection<Object> values = store.getInstances(transaction, recordId, role);
			if(!model.getValueAt(row, column).equals(store.getNameOrValue(transaction, values.iterator().next()).toString())) {
				model.setValueAt(store.getNameOrValue(transaction, values.iterator().next()).toString(), row, column);
			}
			column++;
		}
		//Add the new stats row so columns can be re-calculated.
		addStats();
	}

	//Get the mode of columns with String types.
	private String getMode(int columnIndex) {
		//Set default mode.
		String mode = model.getValueAt(0, columnIndex).toString();
		int mostFreq = 0;
		
		//Loop through rows and check mode of current string.
		for(int i = 0; i < model.getRowCount(); i++) {
			int currentFreq = 0;
			String currentString = model.getValueAt(i, columnIndex).toString();
			//Check rows and increment frequency of currentString if other rows exhibit the same String.
			for(int j = 0; j < model.getRowCount(); j++) {
				if(model.getValueAt(j, columnIndex).toString().equals(currentString)) {
					currentFreq++;
				}
			}
			
			//Replace the most frequent String if needed.
			if(currentFreq > mostFreq) {
				mostFreq = currentFreq;
				mode = currentString;
			}
		}
		return mode;
	}
	
	//Check to see if the row value is a number.
	private boolean isNumber(Object object) {
		try {
			Double.parseDouble(object.toString());
			return true;
		} catch(Exception e) {
			return false;
		}
	}

	//Calculate the average of a column.
	private double getAverage(int columnIndex) {
		double total = 0;
		int rowCount = model.getRowCount();
		//Loop through to get total of column.
		for(int i = 0; i < rowCount; i++) {
			total += Double.parseDouble(model.getValueAt(i, columnIndex).toString());
		}
		//Return average.
		return total / (double)rowCount;
	}
	
	//Calculates the standard deviation of a column.
	private double getSD(int columnIndex) {
		double average = getAverage(columnIndex);
		double total = 0;
		int rowCount = model.getRowCount();
		
		// Calculate total of (value-average)^2.
		for(int i = 0; i < rowCount; i++) {
			double temp = Double.parseDouble(model.getValueAt(i, columnIndex).toString());
			temp -= average;
			temp *= temp;
			total += temp;
		}
		
		// Return standard deviation.
		return Math.sqrt(total / (double)rowCount);
	}
}


