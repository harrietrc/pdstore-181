package diagrameditor.filters;

import javax.swing.DefaultListModel;

import diagrameditor.OperationList;
import diagrameditor.dal.PDOperationApplication;

/**
 * An interface defining method which are required to be implemented
 * by each filter class.
 *
 */
public interface Filter{
	
	void setSelectedOp(PDOperationApplication operation);
	void display(OperationList operations, DefaultListModel listModel);
	
}
