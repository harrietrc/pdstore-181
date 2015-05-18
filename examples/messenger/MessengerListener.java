package messenger;

import java.util.List;

import pdstore.GUID;
import pdstore.PDChange;
import pdstore.generic.PDCoreI;
import pdstore.notify.PDListenerAdapter;

public class MessengerListener extends
		PDListenerAdapter<GUID, Object, GUID> {
	/* Stephen Hood 13/08/12: This class is based on RepaintListener in the
	 * "diagrameditor" package. Whenever change to the content of one of the three
	 * messaging windows is detected, the message tree displayed in each window is updated
	 * (unfortunately collapsed down to its root node) due to messenger.doRefresh()
	 * being called here. There is also scope to update other components when
	 * content change in a messaging window is detected.
	 */
	
	private Messenger messenger;
	
	public MessengerListener(Messenger messenger) {
		super();
		this.messenger = messenger;
	}
	
	@Override
	public void transactionCommitted(
			List<PDChange<GUID, Object, GUID>> transaction,
			List<PDChange<GUID, Object, GUID>> matchedChanges, PDCoreI<GUID, Object, GUID> core) {
		messenger.doRefresh();
	}
}