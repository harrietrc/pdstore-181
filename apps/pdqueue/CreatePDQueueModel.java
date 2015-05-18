package pdqueue;

import java.io.File;

import pdstore.*;
import pdstore.dal.*;

/**
 * This class creates a simple PD Queue model: 
 *                                      +----------------+
 *    +-------+        +-------+  +-----| Time:TimeStamp |
 *    | Queue +--------| Item  |--|     +----------------+
 *    +-------+        +-------+  |     +----------------+
 *                                |-----| Sender:String  |
 *                                |     +----------------+
 *    +-------+                   |     +----------------+
 *    | Reply |                   |-----| Status:Status  |
 *    +-------+                   |     +----------------+
 *                                |     +----------------+
 *                                +-----| Message:String |
 *                                      +----------------+
 * @author mcai023
 *
 */
public class CreatePDQueueModel {
	
	public static void main(String[] args) {
			
		new CreatePDQueueModel();
	}
	
	public CreatePDQueueModel() {
		cleanup();
		create();
	}
	
	public void cleanup() {
		File pdfile = new File("pddata/QueueBase.pds");
		if (pdfile.exists()) {
			System.err.println("PDData - QueueBase.pds exists.");
			while(!(pdfile.delete())) {
				System.err.println("Unable to delete QueueBase.pds ...");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.err.println("Recreating QueueBase.pds ...");
		}
	}
	
	                                               
	public void create() {
		PDStore store = new PDStore("QueueBase");
		GUID transaction = store.begin();

		// create a new model, with a new GUID (generated by GUIDGen)
		GUID model = new GUID("f5c4a920540911e0ab190024e80616c7");
		store.createModel(transaction, model, "QueueModel");

		// create the new complex type Queue
		GUID queueType = new GUID("0774e9a8540f11e0a4530024e80616c7");
		store.createType(transaction, model, queueType, "Queue");
		
		// create the new complex type Item
		GUID itemType = new GUID("b3bba7cbd80911e0b63d0024e80616c7");
		store.createType(transaction, model, itemType, "Item");
		
		
		// create relation from Queue to Item
		GUID queueToItemRole = new GUID("899ecc6cd76111e09b030024e80616c7");
		store.createRelation(transaction, queueType, "Queue", "Item",
				queueToItemRole, itemType);
		
		// create relation from Queue to SerivceType
		GUID queueToServiceType = new GUID("27070a41100111e1aee00024e80616c7");
		store.createRelation(transaction, queueType, "queue", "ServiceType",
				queueToServiceType, PDStore.STRING_TYPEID);
		
		// create relation from Item to TimeStamp
		GUID itemToTimestamp = new GUID("899ecc6ed76111e09b030024e80616c7");
		store.createRelation(transaction, itemType, "item", "Time",
				itemToTimestamp, PDStore.TIMESTAMP_TYPEID);
		
		// create relation from Item to Sender
		GUID itemToSender = new GUID("7e2e063014bf11e18cea0024e80616c7");
		store.createRelation(transaction, itemType, "item", "Sender",
				itemToSender, PDStore.STRING_TYPEID);
		
		// create relation from Item to Receiver
		GUID itemToReceiver = new GUID("899ecc6fd76111e09b030024e80616c7");
		store.createRelation(transaction, itemType, "item", "Receiver",
				itemToReceiver, PDStore.STRING_TYPEID);
		
		// create relation from Item to Message
		GUID itemToMessage = new GUID("899ecc70d76111e09b030024e80616c7");
		store.createRelation(transaction, itemType, "item", "Message",
				itemToMessage, PDStore.STRING_TYPEID);
		
		// create relation from Item to Status
		GUID itemToState = new GUID("b3bba7d2d80911e0b63d0024e80616c7");
		store.createRelation(transaction, itemType, "item", "State",
				itemToState, PDStore.STRING_TYPEID);
		
		store.commit(transaction);
		

		// generate DAL into package "address.dal" in folder "examples"
		PDGen.generateModel(store, "QueueModel", "apps", "pdqueue.dal");	
	}
}