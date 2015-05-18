package pdstore.notify;

import java.util.Collection;
import java.util.List;

import pdstore.GUID;
import pdstore.PDChange;
import pdstore.generic.PDCoreI;
import pdstore.generic.Pairable;

/**
 * This is the interface used for the Listeners as well as for the Interceptors.
 * More detailed comments on the mechanisms can be found in the comments to the
 * methods getDetachedListenerList() and getInterceptorList()
 * 
 * The callback method of listeners is transactionCommitted()
 * Listeners must be registered with PDStore in order to receive calls.
 * 
 * Listeners are typically registered in ListenerDispatchers in 
 * order to use indexing. 
 * If this is the case, the listeners react to certain change templates.
 * 
 * 
 * 
 * @author gweb017
 * 
 * @param <TransactionType>
 * @param <InstanceType>
 * @param <RoleType>
 */
public interface PDListener<TransactionType extends Comparable<TransactionType>, InstanceType, RoleType extends Pairable<RoleType>> {

	/**
	 * 
	 * 
	 * TODO should we move from core to store in the parameters?
	 * @param transaction All changes in the transaction on which this listener was called.
	 * @param matchedChanges  The changes that have been matched.
	 * @param core The PDCoreI object on which the change was done.
	 */
	void transactionCommitted(
			List<PDChange<TransactionType, InstanceType, RoleType>> transaction,
			List<PDChange<TransactionType, InstanceType, RoleType>> matchedChanges,
			PDCoreI<TransactionType, InstanceType, RoleType> core);


	/**
	 * The Listener objects are often used in the context of the ListenerDispatcher.
	 * The ListenerDispatcher needs to know a changeTemplate in order to 
	 * dispatch events efficiently and not to call objects to frequently.
	 * 
	 * There are now two ways to provide these templates:
	 * when the listener is added to the ListenerIndex with the add(listener, template)
	 * method, or already in the listener code.
	 * The listenerDispatcher will take care that both ways are compatible.
	 * 
	 * @return
	 */
	Collection<PDChange<TransactionType, InstanceType, RoleType>> getMatchingTemplates();
}



   
