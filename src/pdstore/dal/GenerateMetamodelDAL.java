package pdstore.dal;

import pdstore.*;

public class GenerateMetamodelDAL {

	public static void addExtraCode(PDStore store) {
		String extraCode = "	public Collection<PDRole> getAccessibleRoles() throws PDStoreException {\n"
				+ "		Set<PDRole> result = new HashSet<PDRole>();\n"
				+ "		for (PDRole role1 : getOwnedRoles())\n"
				+ "			result.add(role1.getPartner());\n"
				+ "		return result;\n"
				+ "	}\n";
		store.addLink(PDStore.TYPE_TYPEID, PDGen.EXTRA_DAL_CODE_ROLEID,
				extraCode);
		store.commit();
	}

	public static void main(String[] args) {
		PDStore store = new PDStore("PDStore");
		addExtraCode(store);
		PDGen.generateModel(store, "PD Metamodel", "src", "pdstore.dal");
		store.commit();
	}
}
