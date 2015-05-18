package angil.test;

import java.util.ArrayList;
import java.util.List;

import angil.LogMaker;
import angil.dal.PDPage;
import angil.dal.PDAction;
import pdstore.GUID;
import pdstore.PDStore;
import pdstore.dal.PDSimpleWorkingCopy;
import pdstore.dal.PDWorkingCopy;

public class TestStore {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PDStore store = new PDStore("Loadtest");
		ArrayList<String> nameList = new ArrayList<String>();
		nameList.add("login");
		nameList.add("menu");
		nameList.add("transfer");
		nameList.add("status");
		nameList.add("depot");
		for (String name : nameList) {
			GUID pageID = store.getId(name);
			PDPage page = PDPage.load(store, pageID);
			LogMaker.logToConsole(page.getName());
			PDAction.typeId.toString();

			List<PDAction> actions = new ArrayList<PDAction>(page.getNextActions());
			for (PDAction action : actions) {
				LogMaker.logToConsole(action.getName() + " "
						+ action.getRequestURL());
			}
			LogMaker.logToConsole("\n");
		}
		LogMaker.logToConsole("=========================================================\n");
		ArrayList<String> nameList1 = new ArrayList<String>();
		nameList1.add("logout");
		nameList1.add("verify");
		nameList1.add("confirmtransfer");
		nameList1.add("maketransfer");
		nameList1.add("showstatus");
		nameList1.add("invest");
		nameList1.add("sellbond");
		nameList1.add("buybond");
		nameList1.add("canceltransfer");
		nameList1.add("cancelstatus");
		nameList1.add("canceldepot");

		for (String name : nameList1) {
			GUID actionID = store.getId(name);
			PDAction action = PDAction.load(store, actionID);
			LogMaker.logToConsole(action.getName());
			PDAction.typeId.toString();

			List<PDPage> pages = new ArrayList<PDPage>(action.getNextPages());
			for (PDPage page : pages) {
				LogMaker.logToConsole(page.getName());
			}
			LogMaker.logToConsole("\n");
		}
	}

}
