package webtest;

import pdstore.scripting.Interpreter;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.lang.Boolean;
import pdstore.GUID;
import pdstore.PDStore;

public class RandomTestCaseGenerator {
	PDStore store;
	StringBuffer out = new StringBuffer();

	int maxActionInvocations = 20;
	int actionInvocations = 0;
	Random generator = new Random( 39580427 );
	
	GUID pageref=null;//needs to be global as used in execute action and visit page
	
	
	public RandomTestCaseGenerator(PDStore store) {
		this.store = store;
	}

	void executeAction(GUID action) {
		String name = store.getName(action);
		Collection<Object> fields = store.getInstances(action, store.getId("field"));
		
		//if we are on Start Action, then set the start page to Main Page
		if (pageref==null)
		{  	pageref = (GUID) store.getInstance(action, store.getId("page"));
		}
		
		out.append("\t\t//Action Number: "+actionInvocations+"\n");
		out.append("\t\t//On page: "+store.getName(pageref)+"\n");
		out.append("\t\t//Choose action: "+name+"\n");
			
		//if the action has any fields
		if (fields.size()>0)
		{	
			//pick a random row of data from the rows that belong to the page we are currently on
			Collection<Object> rows = store.getInstances(store.getId(store.getName(pageref)+"Data"), store.getId("row"));
			//int randomIndex = generator.nextInt( rows.size() );
			//out.append(store.getName(rows.toArray()[randomIndex]));
			
			GUID row = (GUID) pickwieghtedprobabilitydataset(rows);
		
			out.append("\t\t//Picking Table:" + store.getName(pageref)+"Data"+"\n");
			out.append("\t\t//Picking Row:"+store.getName(row)+"\n");	
			
			//populate inputs and validate outputs
			//Collection<Object> cells=store.getInstances((GUID) rows.toArray()[randomIndex], store.getId("contains"));
			
			Collection<Object> cells=store.getInstances(row, store.getId("contains"));
			EnterInputs(cells, action);
			ValidateOutputsAndReturnOutputPage(cells, action);
			
			//we have completed one action
			actionInvocations++;
		}
		
		//move to the next page now that the action is complete
		visitPage(pageref);
	}
	
	void EnterInputs(Collection<Object> cells, GUID action)
	{	//for each cell in the row of data, if the cell has any input or output data
		//enter an input if that cell happens to be a field belonging to the action at hand
		for (Object cell : cells)
		{	String cellfield = store.getName(store.getInstance((GUID) cell, store.getId("data")));
			String celldata = store.getName(store.getInstance((GUID) cell, store.getId("data")));
			if (!celldata.contains("NA"))
			{	Collection<Object> actionfields=store.getInstances(action, store.getId("field"));
				EnterInputIfInputField(actionfields, action, (GUID) cell);					
			}
		}
		
	}
	
	void EnterInputIfInputField(Collection<Object> actionfields, GUID action, GUID cell)
	{	
		//given all the fields that belong to the action at hand
		//see if the current cell is one of them
		GUID cellfieldGUID = (GUID) store.getInstance((GUID) cell, store.getId("field"));
		String cellfield = store.getName(cellfieldGUID);
		String cellfieldtype = store.getName(store.getInstance(cellfieldGUID, store.getId("fieldType")));
		String celldata = store.getName(store.getInstance((GUID) cell, store.getId("data")));
		
		/*	for (Object field : fields) {
			Collection<Object> cells = store.getInstances(action, store.getId("field").getPartner());
		}*/
		
		for (Object actionfield : actionfields)
		{ 	//if the cell field is also an action field, it is an input field
			//so enter the input
			if (store.getName((GUID)actionfield)==(cellfield))
			{   out.append("\t\t//Enter Input: " +cellfield+"\n");
			
				String cellxpath = store.getName(store.getInstance((GUID) cell, store.getId("xpath")));
				if(cellfieldtype.contains("Button"))
					out.append("\t\t Library.ClickButton(driver,\""+ cellxpath+"\");\n");
				if(cellfieldtype.contains("Navigate"))
					out.append("\t\t Library.NavigatetoLink(driver,\""+ celldata + "\",\""+cellxpath+"\");\n");
				if(cellfieldtype.contains("String"))
					out.append("\t\t Library.EnterText(driver,\""+ celldata + "\",\""+cellxpath+"\");\n");
				if(cellfieldtype.contains("Link"))
					out.append("\t\t Library.ClickLink(driver,\""+ celldata + "\",\""+cellxpath+"\");\n");						
			}
		}
		
	}
	
	void ValidateOutputsAndReturnOutputPage (Collection<Object> cells, GUID action)
	{	
		//for each cell in the row
		//if the cell is of type label
		//it is an output field
		//so validate the output
		//along with the page it displays on 
		for (Object cell : cells)
		{	
			GUID cellfieldGUID = (GUID) store.getInstance((GUID) cell, store.getId("field"));
			String cellfieldtype = store.getName(store.getInstance(cellfieldGUID, store.getId("fieldType")));
			String celldata = store.getName(store.getInstance((GUID) cell, store.getId("data")));
	
			if (cellfieldtype.contains("label"))
			{	//verify output expected is displayed
				out.append("\n\t\t//Verify Output: "+store.getName(cellfieldGUID)+"\n");
				
				String cellxpath = store.getName(store.getInstance((GUID) cell, store.getId("xpath")));
				out.append("\t\t Library.VerifyLabel (driver, \""+ celldata + "\",\""+cellxpath+"\");\n");
				
				
				//update the page to the page on which the output field displays
				//pageref = (GUID) store.getInstance(cellfieldGUID, store.getId("page"));
				//verify we are now on this page
				String page = store.getName(store.getInstance(cellfieldGUID, store.getId("page")));
				out.append("\n\t\t//Verify Output Page we are on is: "+page+"\n");
				out.append("\t\t Library.VerifyPage(driver,\""+page+"\");\n");
				
			}
			
		}
		
	}
	
	
	void visitPage(GUID page) {
		// read information about page
		String pagename = store.getName(page);
		
		// stop if desired test case length reached
		if (actionInvocations >= maxActionInvocations)
		{	out.append("}\n}");
		
			return;
		}
		// select a random action to execute out of the actions that belong to the page we are currently on
		Collection<Object> actions = store.getInstances(page, store.getId("action"));
		int randomIndex = generator.nextInt( actions.size() );
		
		GUID action = pickwieghtedprobabilityaction(pageref);
		executeAction(action);
		
		//String action = store.getName((GUID) actions.toArray()[randomIndex]);
		//executeAction((GUID) actions.toArray()[randomIndex]);
}

	
	public GUID pickwieghtedprobabilityaction (GUID page)
	{
		Random rand = new Random(); 
		Double pickedNumber = rand.nextDouble();
		Double highestweight = 0.0;
		GUID nextaction = null;
	
		Collection<Object> pagetransitions = store.getInstances(page, store.getId("transition"));
		for (Object pagetransition : pagetransitions)
		{	String probabilitytext = (String) store.getInstance((GUID) pagetransition, store.getId("prob"));
			Double probabilitynumber = Double.parseDouble(probabilitytext);
			
			Double weight = Math.abs(pickedNumber - probabilitynumber)*probabilitynumber;
			if (weight > highestweight)
			{	nextaction = (GUID) store.getInstance((GUID) pagetransition, store.getId("action"));
				highestweight = weight;
			}
		}
		
		return nextaction;
	}
	
	public Object pickwieghtedprobabilitydataset (Collection<Object> rows)
	{		Random rand = new Random(); 
			Double pickedNumber = rand.nextDouble();
			Double highestweight = 0.0;
			Object nextrow = null;
			
		for (Object row : rows)
		{
			GUID transition = (GUID) store.getInstance((GUID) row, store.getId("transition"));
			//get prob of transition
			String probabilitytext = (String) store.getInstance((GUID) transition, store.getId("prob"));
			Double probabilitynumber = Double.parseDouble(probabilitytext);
			Double weight = Math.abs(pickedNumber - probabilitynumber)*probabilitynumber;
			
			if (weight > highestweight)
			{	nextrow = row;
				highestweight = weight;	
				pageref = (GUID) store.getInstance((GUID) transition, store.getId("page"));
				//System.out.println("//"+store.getName(row)+":"+store.getName((GUID) store.getInstance((GUID) row, store.getId("transition")))+":"+store.getName((GUID) store.getInstance((GUID) transition, store.getId("page"))));
			}
		}
		return nextrow;
	}
	
	public static void main(String args[]) {
		String storeName = "shoutwiki";
		String startActionName = "StartAction";

		PDStore store = new PDStore(storeName);
		RandomTestCaseGenerator r = new RandomTestCaseGenerator(store);
		GUID startAction = store.getId(startActionName);
		r.out.append("using System;\n" +
				"using System.Text;\n" +
				"using System.Threading;\n" +
				"using NUnit.Framework;\n" +
				"using OpenQA.Selenium;\n" +
				"using OpenQA.Selenium.Remote;\n" +
				"using OpenQA.Selenium.Support.UI;\n"+		
				"namespace SeleniumSteps\n"+
				"{\n"+
				"\t[Microsoft.VisualStudio.TestTools.UnitTesting.TestClassAttribute()]\n"+
				"\tpublic class Tests\n"+
				"\t{\n"+
				"\tAutomationSteps  Library = new AutomationSteps();\n"+
				"\tstatic Platform platform = new Platform(PlatformType.Windows);\n"+
				"\tstatic TimeSpan t = new TimeSpan(3, 0, 0);\n"+
				"\tstatic Uri uri1 = new Uri(\"http://localhost:4444/wd/hub\");\n"+
				"\tstatic DesiredCapabilities refDesiredCapabilities = new DesiredCapabilities(\"firefox\",\"21\", platform);\n"+
				"\tRemoteWebDriver driver = new RemoteWebDriver(uri1, refDesiredCapabilities, t);\n"+
				"\n"+
				"\t[Microsoft.VisualStudio.TestTools.UnitTesting.TestMethodAttribute()]\n"+
				"\tpublic virtual void Test1()\n"+
				"\t{\n");	
		
		r.executeAction(startAction);
		r.out.append("\n}");
		System.out.println(r.out);
	}
	
}	