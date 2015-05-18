package diagrameditor;

import pdstore.GUID;
import pdstore.PDStore;
import pdstore.dal.PDGen;

public class CreateModel {
	PDStore store = new PDStore("DiagramEditor");
	public final static GUID OPERATION_TYPE_ID = new GUID("920645120d6411e0b45a1cc1dec00ed3");
	public final static GUID SHAPE_TYPE_ID = new GUID("920645110d6411e0b45a1cc1dec00ed3");
	public final static GUID DIAGRAM_TYPE_ID = new GUID("920645100d6411e0b45a1cc1dec00ed3");
	public final static GUID HISTORY_MODELID = new GUID("92050c900d6411e0b45a1cc1dec00ed3");
	public final static GUID OPERATION_APPLICATION_TYPE_ID = new GUID("7420c751980111e1b62d1c6f65cd0117");
	public final static GUID DIAGRAMSET_TYPE_ID = new GUID("f119cf11eda611e1b6c60026b97da292");

	// Operational parameter guids
	public static final GUID TARGET_SHAPE_ROLEID = new GUID("b81a82c79b1111e1a6ca002215eb452f");
	public static final GUID COPY_ROLEID = new GUID("b81a82cc9b1111e1a6ca002215eb452f");

	public final static GUID COLOR_PARAM_TYPE_ID = new GUID("b81a82c39b1111e1a6ca002215eb452f");
	public final static GUID MOVE_PARAM_TYPE_ID = new GUID("b81a82c49b1111e1a6ca002215eb452f");
	public final static GUID COPY_PARAM_TYPE_ID = new GUID("b81a82c59b1111e1a6ca002215eb452f");
	public final static GUID DELETE_PARAM_TYPE_ID = new GUID("b81a82c69b1111e1a6ca002215eb452f");
	public final static GUID RESIZE_PARAM_TYPE_ID = new GUID("0148c46faac911e1a00478e4009ed4de");
	public final static GUID NEW_PARAM_TYPE_ID = new GUID("b81a82ce9b1111e1a6ca002215eb452f");
	public final static GUID ORIGINAL_SHAPE_PARAM_TYPE_ID = new GUID("ce35eb73aab811e1a08a78e4009ed4de");

	public final static GUID TARGET_SHAPELIST_ROLEID = new GUID("8b076910c70811e1ae8378e4009ed4de");

	public final static GUID DRAWN_BY_ROLEID = new GUID("89b321a1ca2e11e1990478e4009ed4de");

	private void createModel() {
		GUID transaction = store.begin();

		// Create central History Model
		store.createModel(transaction, CreateModel.HISTORY_MODELID, "HistEdit");

		// Create types
		store.createType(transaction, CreateModel.HISTORY_MODELID, CreateModel.DIAGRAMSET_TYPE_ID, "DiagramSet");
		store.createType(transaction, CreateModel.HISTORY_MODELID, CreateModel.DIAGRAM_TYPE_ID, "Diagram");
		store.createType(transaction, CreateModel.HISTORY_MODELID, CreateModel.OPERATION_APPLICATION_TYPE_ID,
				"OperationApplication");
		store.createType(transaction, CreateModel.HISTORY_MODELID, CreateModel.OPERATION_TYPE_ID, "Operation");
		store.createType(transaction, CreateModel.HISTORY_MODELID, CreateModel.SHAPE_TYPE_ID, "Shape");

		// Roles for type DiagramSet
		store.createRelation(transaction, CreateModel.DIAGRAMSET_TYPE_ID, "DiagramSet", "Diagram", new GUID(
				"f119cf12eda611e1b6c60026b97da292"), CreateModel.DIAGRAM_TYPE_ID);

		// Roles for type Diagram
		store.createRelation(transaction, CreateModel.DIAGRAM_TYPE_ID, "Diagram", "Operation Application", new GUID(
				"920645130d6411e0b45a1cc1dec00ed3"), CreateModel.OPERATION_APPLICATION_TYPE_ID);
		store.createRelation(transaction, CreateModel.DIAGRAM_TYPE_ID, "Diagram", "Shape", new GUID(
				"b81a82cd9b1111e1a6ca002215eb452f"), CreateModel.SHAPE_TYPE_ID);
		store.createRelation(transaction, CreateModel.DIAGRAM_TYPE_ID, "previous", "next", new GUID(
				"85d14343edab11e1aef50026b97da292"), CreateModel.DIAGRAM_TYPE_ID);

		// Roles for type Operation Application
		store.createRelation(transaction, CreateModel.OPERATION_APPLICATION_TYPE_ID, "Operation Application",
				"Operation", new GUID("7420c752980111e1b62d1c6f65cd0117"), CreateModel.OPERATION_TYPE_ID);
		store.createRelation(transaction, CreateModel.OPERATION_APPLICATION_TYPE_ID, "previous", "next", new GUID(
				"7e8d67ca4f4c11e0a5d4842b2b9af4fd"), CreateModel.OPERATION_APPLICATION_TYPE_ID);
		store.createRelation(transaction, CreateModel.OPERATION_APPLICATION_TYPE_ID, null, "superParameter", new GUID(
				"920645160d6411e0b45a1cc1dec00ed3"), PDStore.OBJECT_TYPEID);

		// Roles for type Operation
		store.createRelation(transaction, CreateModel.OPERATION_TYPE_ID, null, "parameterType",
				PDStore.HAS_TYPE_ROLEID, PDStore.TYPE_TYPEID);
		store.createRelation(transaction, CreateModel.OPERATION_TYPE_ID, null, "implementation", new GUID(
				"7420c753980111e1b62d1c6f65cd0117"), PDStore.STRING_TYPEID);
		store.createRelation(transaction, CreateModel.OPERATION_TYPE_ID, null, "shapeType", new GUID(
				"7420c754980111e1b62d1c6f65cd0117"), PDStore.TYPE_TYPEID);

		// roles for type Shape
		store.createRelation(transaction, CreateModel.SHAPE_TYPE_ID, null, "type", PDStore.HAS_TYPE_ROLEID,
				PDStore.TYPE_TYPEID);
		store.createRelation(transaction, CreateModel.SHAPE_TYPE_ID, null, "x", new GUID(
				"920645170d6411e0b45a1cc1dec00ed3"), PDStore.INTEGER_TYPEID);
		store.createRelation(transaction, CreateModel.SHAPE_TYPE_ID, null, "y", new GUID(
				"920645180d6411e0b45a1cc1dec00ed3"), PDStore.INTEGER_TYPEID);
		store.createRelation(transaction, CreateModel.SHAPE_TYPE_ID, null, "height", new GUID(
				"920645190d6411e0b45a1cc1dec00ed3"), PDStore.INTEGER_TYPEID);
		store.createRelation(transaction, CreateModel.SHAPE_TYPE_ID, null, "width", new GUID(
				"9206451a0d6411e0b45a1cc1dec00ed3"), PDStore.INTEGER_TYPEID);
		store.createRelation(transaction, CreateModel.SHAPE_TYPE_ID, null, "color", new GUID(
				"9206451b0d6411e0b45a1cc1dec00ed3"), PDStore.STRING_TYPEID);
		store.createRelation(transaction, CreateModel.SHAPE_TYPE_ID, null, "shapeType", new GUID(
				"7420c754980111e1b62d1c6f65cd0117"), PDStore.TYPE_TYPEID);
		// Added so there is a reference between a copied shape.
		store.createRelation(transaction, SHAPE_TYPE_ID, null, "originalShape", ORIGINAL_SHAPE_PARAM_TYPE_ID,
				SHAPE_TYPE_ID);
		//Reference to any data needed for visualizing a widget.
		store.createRelation(transaction, SHAPE_TYPE_ID, null, "visualized data", new GUID(
				"be8ef752eb1011e1860778e4009ed4de"), PDStore.STRING_TYPEID);


		/*
		 * Define the parameters for defined operations here:
		 */
		// ColorParam: has Shape 'targetShape', String 'newColor'. Used by
		// OpChangeColor
		store.createType(transaction, HISTORY_MODELID, COLOR_PARAM_TYPE_ID, "ColorParam");
		store.createRelation(transaction, COLOR_PARAM_TYPE_ID, null, "targetShape", TARGET_SHAPE_ROLEID, SHAPE_TYPE_ID);
		store.createRelation(transaction, COLOR_PARAM_TYPE_ID, null, "newColor", new GUID(
				"b81a82c89b1111e1a6ca002215eb452f"), PDStore.STRING_TYPEID);

		// MoveParam: has Shape 'targetShape', int 'dx', int 'dy'. Used by
		// OpMoveShape
		store.createType(transaction, HISTORY_MODELID, MOVE_PARAM_TYPE_ID, "MoveParam");
		store.createRelation(transaction, MOVE_PARAM_TYPE_ID, null, "targetShape", TARGET_SHAPE_ROLEID, SHAPE_TYPE_ID);
		store.createRelation(transaction, MOVE_PARAM_TYPE_ID, null, "dx", new GUID("b81a82ca9b1111e1a6ca002215eb452f"),
				PDStore.INTEGER_TYPEID);
		store.createRelation(transaction, MOVE_PARAM_TYPE_ID, null, "dy", new GUID("b81a82cb9b1111e1a6ca002215eb452f"),
				PDStore.INTEGER_TYPEID);

		// ResizeParam: has Shape 'targetShape', int 'width' and "height". Used
		// by OpResizeShape.
		store.createType(transaction, HISTORY_MODELID, RESIZE_PARAM_TYPE_ID, "ResizeParam");
		store.createRelation(transaction, RESIZE_PARAM_TYPE_ID, null, "targetShape", TARGET_SHAPE_ROLEID, SHAPE_TYPE_ID);
		store.createRelation(transaction, RESIZE_PARAM_TYPE_ID, null, "width", new GUID(
				"0148c46eaac911e1a00478e4009ed4de"), PDStore.INTEGER_TYPEID);
		store.createRelation(transaction, RESIZE_PARAM_TYPE_ID, null, "height", new GUID(
				"0148c46daac911e1a00478e4009ed4de"), PDStore.INTEGER_TYPEID);

		// NewParam: has Shape 'newShape'. Used by OpNewShape.
		store.createType(transaction, HISTORY_MODELID, NEW_PARAM_TYPE_ID, "NewParam");
		store.createRelation(transaction, NEW_PARAM_TYPE_ID, null, "targetShape", TARGET_SHAPE_ROLEID, SHAPE_TYPE_ID);
		store.createRelation(transaction, NEW_PARAM_TYPE_ID, null, "target shape list", TARGET_SHAPELIST_ROLEID,
				PDStore.ANY_TYPEID);
		store.createRelation(transaction, NEW_PARAM_TYPE_ID, null, "visualized by", new GUID(
				"be8ef753eb1011e1860778e4009ed4de"), PDStore.GUID_TYPEID);
		store.createRelation(transaction, NEW_PARAM_TYPE_ID, null, "data",
				new GUID("8da40203eb1211e1996178e4009ed4de"), PDStore.STRING_TYPEID);


		// DeleteParam: has target shape to delete.
		store.createType(transaction, HISTORY_MODELID, DELETE_PARAM_TYPE_ID, "DeleteParam");
		store.createRelation(transaction, DELETE_PARAM_TYPE_ID, null, "targetShape", TARGET_SHAPE_ROLEID, SHAPE_TYPE_ID);

		// CopyParam: has Shape 'targetShape', Shape 'copy'. Used by
		// OpCopyShape.
		store.createType(transaction, HISTORY_MODELID, COPY_PARAM_TYPE_ID, "CopyParam");
		store.createRelation(transaction, COPY_PARAM_TYPE_ID, null, "copy", COPY_ROLEID, SHAPE_TYPE_ID);
		store.addLink(transaction, COPY_PARAM_TYPE_ID, PDStore.HAS_SUPERTYPE_ROLEID, NEW_PARAM_TYPE_ID);

		store.commit(transaction);
	}

	public void createDALClasses() {
		PDGen.generateModel(store, "HistEdit", "apps", "diagrameditor.dal");
		store.commit();
	}

	public static void main(String[] args) {
		CreateModel m = new CreateModel();
		m.createModel();
		m.createDALClasses();
	}
}
