package pdstore;

import java.util.List;

import pdstore.generic.PDCoreI;
import pdstore.notify.PDListenerAdapter;

/**   
 * A View as Listener that should later implement RFC 4647.
 * It means that for every pair of string and matching language tag, there
 * is a virtual link along the role  LANGUAGE_ROLEID. The string is
 * instance1 and the language Tag is isntance2.
 * It is not necessary that one can query all tags for one text.
 * @author gweb017
 *
 */
public class LanguageViewListener extends PDListenerAdapter<GUID, Object, GUID> {

	public final static GUID LANGUAGE_ROLEID = new GUID(
	        "f4c720c0e27411e1bd72020054554e01");
	public int callCount = 0;

	public int getCallCount() {
		return callCount;
	}

	public void setCallCount(int callCount) {
		this.callCount = callCount;
	}

	public LanguageViewListener() {
		super();
	}

	public void transactionCommitted(
			List<PDChange<GUID, Object, GUID>> transaction,
			List<PDChange<GUID, Object, GUID>> matchedChanges, PDCoreI<GUID, Object, GUID> core) {
		for (PDChange<GUID, Object, GUID> change: matchedChanges) {
			if(languageMatch(change.getInstance1(), change.getInstance2()))
				  transaction.add(change);
		}
	}

	private boolean languageMatch(Object instance1, Object instance2) {
		// TODO here one needs some open source implementation of RFC 4647.
		// for the moment substring seems to be a sensible hack.
		if (!(instance1 instanceof String)) return false;
		if (!(instance2 instanceof String)) return false;
		
		return ((String)(instance1)).contains((String)(instance2));
	}

}
