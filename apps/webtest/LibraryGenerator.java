package webtest;

import pdstore.scripting.Interpreter;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;
import java.lang.Boolean;
import pdstore.GUID;
import pdstore.PDStore;

public class LibraryGenerator {
	PDStore store;
	StringBuffer out = new StringBuffer();
	HashSet<GUID> drawnIDs = new HashSet<GUID>();
	HashSet<String> GUIActions = new HashSet<String>();
	
	public LibraryGenerator(PDStore store) {
		this.store = store;
	}

	public void visualizeAction(GUID action) {
		// make sure not to visualize the action twice
		if (drawnIDs.contains(action))
			return;
		drawnIDs.add(action);
		
		GUIActions.add("VerifyPage");
		
		visualizeFields(action);
		for (Object page : store.getInstances(action, store.getId("page"))) {
			visualizePage((GUID) page);
		}
	}
	
public void visualizeFields(GUID action)
{	Collection<Object> fields = store.getInstances(action,
		store.getId("field"));

	if (fields.size() > 0) {
		
		for (Object field : fields) {
			String fieldType = (String) store.getName(store.getInstance(field,
					store.getId("fieldType")));
			if (!GUIActions.contains(fieldType))
				GUIActions.add(fieldType);
		}
	}	
}	
	
	public void visualizePage(GUID page) {
		// make sure not to visualize the page twice
		if (drawnIDs.contains(page))
			return;
		drawnIDs.add(page);

		// visualize action
		//we need navigate to initial page
		if (!GUIActions.contains(page))
			GUIActions.add("Navigate");
	
		visualizeFields(page);
		
		for (Object action : store.getInstances(page, store.getId("action"))) {
			visualizeAction((GUID) action);
		}
	}

	public static void main(String args[]) {
		String storeName = "shoutwiki";
		String startActionName = "StartAction";
		
		
		PDStore store = new PDStore(storeName);
		LibraryGenerator l = new LibraryGenerator(store);
		l.visualizeAction(store.getId(startActionName));
		l.generateLibrary();
		System.out.println(l.out);
		
	}
	
	
	public void generateLibrary ()//generate library of functions to use
	{		
			out.append("using System;\nusing System.Text;\nusing System.Threading;\nusing NUnit.Framework;\nusing OpenQA.Selenium;\nusing OpenQA.Selenium.Remote;\nusing OpenQA.Selenium.Support.UI;\n");
			out.append("namespace SeleniumSteps\n{\n\tpublic class AutomationSteps\n\t{\n\t\t");
			
		for (String item : GUIActions)
			{	
				if (item.contains("Button"))
				{	//	append this code.
					out.append("public void ClickButton(RemoteWebDriver driver, string ButtonXpath)\n\t\t{\n\t\t\tIWebElement button = driver.FindElement(By.XPath(ButtonXpath));\n\t\tbutton.Click();\n\t\t}\n\t\t");
				}	
				if (item.contains("Navigate"))
				{//append this code.
					out.append("public void NavigatetoLink(RemoteWebDriver driver, string Url)\n\t\t{\n\t\tdriver.Navigate().GoToUrl(Url);\n\t\t}\n\t\t");
				}			
				if (item.contains("String"))
				{	// append this code
					
					out.append("public void  EnterText (RemoteWebDriver driver, string TextXpath, string Text)\n\t\t{\tIWebElement login = driver.FindElement(By.XPath(TextXpath));\n\t\t\tlogin.SendKeys(Text);\n\t\t}\n\t\t");
				}
				if (item.contains("Link"))
				{	// append this code
					
					out.append("public void  ClickLink (RemoteWebDriver driver, string LinkXpath)\n\t\t{\tIWebElement Link = driver.FindElement(By.XPath(LinkXpath));\n\t\t\tLink.Click();\n\t\t}\n\t\t");
				}
				if (item.contains("label"))
				{	// append this code
					out.append("public void  VerifyLabel (RemoteWebDriver driver, string LinkXpath, string Message)\n\t\t{\tIWebElement Link = driver.FindElement(By.XPath(LinkXpath));\n\t\t\t" +"if (!Link.Text.Contains(Message))\n\t\t\tAssert.Fail();\n\t\t}\n\t\t");
				}
				if (item.contains("VerifyPage"))
				{	// append this code
					out.append("public void  VerifyPage (RemoteWebDriver driver, string PageTitle)\n\t\t{\t if (!driver.Title.Contains(PageTitle))\n\t\t\tAssert.Fail();\n\t\t}\n\t\t");
				}
						
			}
			
		out.append("}\n}");
			
		}
}