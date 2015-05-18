package messenger;

import java.util.List;

import pdstore.GUID;
import pdstore.PDChange;
import pdstore.generic.PDCoreI;
import pdstore.notify.PDListenerAdapter;

public class MessengerStatusListener extends
		PDListenerAdapter<GUID, Object, GUID> {
	/* Stephen Hood 15/09/12: This class is based on RepaintListener in the
	 * "diagrameditor" package. Whenever change to the content of any messaging
	 * window is detected which also affect the awareness window's content,
	 * two things happen via messenger.doStatusRefresh() being called here.
	 * Firstly, the message tree displayed in the messaging window is updated
	 * (unfortunately collapsed down to its root node), and secondly
	 * the awareness window is refreshed.
	 * 
	 * There is also scope to update other components when
	 * content change in a messaging window is detected.
	 */
	
	private Messenger messenger;
	
	public MessengerStatusListener(Messenger messenger) {
		super();
		this.messenger = messenger;
	}
	
	@Override
	public void transactionCommitted(
			List<PDChange<GUID, Object, GUID>> transaction,
			List<PDChange<GUID, Object, GUID>> matchedChanges, PDCoreI<GUID, Object, GUID> core) {
		messenger.doStatusRefresh();
	}
}