package messenger;

import java.util.Collection;
import java.sql.Timestamp;

import pdstore.GUID;
import pdstore.PDStore;

public class MessengerInit {
	/* Stephen Hood 13/08/12: Class BookExample in the package "book" was the starting
	 * point for the creation of this messaging framework - based around Conversations
	 * between Users which contain Messages, and are stored in a central Conversation
	 * Repository. The Status which the contact of a User sees depends on how the User
	 * has categorised the contact. This class must be run once, so that
	 * the Messenger class can then use the data framework it sets up. */

	// the GUID that identifies the conversation repository model
	private static final GUID repositoryModel = new GUID("e82730207e3e11e1a4740018dea49e3e");

	// the GUIDs for the types and roles within the conversation repository model
		// This GUID is public in order to make it accessible to the Messenger class
	public static final GUID statusType = new GUID("9dcd9ddde07111e1a01a001e101f3534");
	private static final GUID conversationType = new GUID("e82e5c117e3e11e1a4740018dea49e3e");
		// This GUID is public in order to make it accessible to the Messenger class
	public static final GUID userType = new GUID("e82e5c107e3e11e1a4740018dea49e3e");
	private static final GUID messageType = new GUID("e82e5c147e3e11e1a4740018dea49e3e");
		// This GUID is public in order to make it accessible to the Messenger class
	public static final GUID conversationRepositoryType = new GUID("9dcd9dd9e07111e1a01a001e101f3534");
		/* These GUIDs are public to enable them to be listened
		 * to by an instantiation of the MessengerListener class. */
	public static final GUID statusRole = new GUID("9dcd9ddee07111e1a01a001e101f3534");
	public static final GUID conversationRole = new GUID("9dcd9ddae07111e1a01a001e101f3534");
	public static final GUID userRole = new GUID("9dcd9dd9e07111e1a01a001e101f3534");
	public static final GUID participantRole = new GUID("e82e5c177e3e11e1a4740018dea49e3e");
	public static final GUID friendAvailabilityRole = new GUID("9dcd9de2e07111e1a01a001e101f3534");
	public static final GUID relativeAvailabilityRole = new GUID("9dcd9debe07111e1a01a001e101f3534");
	public static final GUID acquaintanceAvailabilityRole = new GUID("9dcd9dece07111e1a01a001e101f3534");
	public static final GUID workmateAvailabilityRole = new GUID("9dcd9dede07111e1a01a001e101f3534");
	public static final GUID classmateAvailabilityRole = new GUID("9dcd9deee07111e1a01a001e101f3534");
	public static final GUID annoyingAvailabilityRole = new GUID("9dcd9defe07111e1a01a001e101f3534");
	public static final GUID friendRole = new GUID("9dcd9de3e07111e1a01a001e101f3534");
	public static final GUID relativeRole = new GUID("9dcd9de6e07111e1a01a001e101f3534");
	public static final GUID acquaintanceRole = new GUID("9dcd9de7e07111e1a01a001e101f3534");
	public static final GUID workmateRole = new GUID("9dcd9de8e07111e1a01a001e101f3534");
	public static final GUID classmateRole = new GUID("9dcd9de9e07111e1a01a001e101f3534");
	public static final GUID annoyingRole = new GUID("9dcd9deae07111e1a01a001e101f3534");
	public static final GUID messageRole = new GUID("e82e5c187e3e11e1a4740018dea49e3e");
	public static final GUID messageSendTimeRole = new GUID("9dcd9dd1e07111e1a01a001e101f3534");
	public static final GUID replyRole = new GUID("9dcd9dd6e07111e1a01a001e101f3534");
		/* These GUIDs aren't listened to, because alterations regarding this role
		 * will only occur on the creation or deletion of a Conversation/Message,
		 * which is already being listened for. */
	private static final GUID conversationStartTimeRole = new GUID("e82e5c137e3e11e1a4740018dea49e3e");
	private static final GUID senderRole = new GUID("e82e5c167e3e11e1a4740018dea49e3e");
	
	private static void createRepositoryModel(PDStore store) {
		// Begin a new database transaction
		GUID transaction = store.begin();

		// Create the Repository Model
		store.createModel(transaction, repositoryModel, "Repository Model");
		
		// Create the new complex type Conversation Repository
		store.createType(transaction, repositoryModel, conversationRepositoryType, "Conversation Repository");
		
		// Create the new complex type Status
		store.createType(transaction, repositoryModel, statusType, "Status");
		
		// Create the new complex type Conversation
		store.createType(transaction, repositoryModel, conversationType, "Conversation");
		
		/* Create a relation between Conversation Repository and Conversation with the roles
		 * "in repositories" and "contains conversations".
		 * Note this relation has names for both roles, so it can be navigated in
		 * both directions i.e. you can get the Conversations in a Conversation
		 * Repository, and the Conversation Repositories a Conversation is contained in. */
		store.createRelation(transaction, conversationRepositoryType, "in repositories",
				"contains conversations", conversationRole, conversationType);
				
		/* Create a relation from Conversation to Timestamp with the role "Start Time"
		 * The role on the other end is unnamed, hence the 3rd parameter null. */
		store.createRelation(transaction, conversationType, null, "Start Time",
				conversationStartTimeRole, PDStore.TIMESTAMP_TYPEID);
		
		// Create the new complex type User
		store.createType(transaction, repositoryModel, userType, "User");		
		
		// Create a relation between Conversation Repository and User
		store.createRelation(transaction, conversationRepositoryType, "in repositories",
				"has users", userRole, userType);
		
		// Create a relation between User and Conversation
		store.createRelation(transaction, userType, "has participants",
				"participant in", participantRole, conversationType);
		
		/* Create a relation between User and Status with the roles
		 * "users with status for friends" and "has status for friends". */
		store.createRelation(transaction, userType, "users with status for friends",
				"has status for friends", friendAvailabilityRole, statusType);
		
		/* Create a relation between User and Status with the roles
		 * "users with status for relatives" and "has status for relatives". */
		store.createRelation(transaction, userType, "users with status for relatives",
				"has status for relatives", relativeAvailabilityRole, statusType);
		
		/* Create a relation between User and Status with the roles
		 * "users with status for acquaintances" and "has status for acquaintances". */
		store.createRelation(transaction, userType, "users with status for acquaintances",
				"has status for acquaintances", acquaintanceAvailabilityRole, statusType);
		
		/* Create a relation between User and Status with the roles
		 * "users with status for workmates" and "has status for workmates". */
		store.createRelation(transaction, userType, "users with status for workmates",
				"has status for workmates", workmateAvailabilityRole, statusType);
		
		/* Create a relation between User and Status with the roles
		 * "users with status for classmates" and "has status for classmates". */
		store.createRelation(transaction, userType, "users with status for classmates",
				"has status for classmates", classmateAvailabilityRole, statusType);
		
		/* Create a relation between User and Status with the roles
		 * "users with status for annoying non-friends" and "has status for annoying non-friends". */
		store.createRelation(transaction, userType, "users with status for annoying non-friends",
				"has status for annoying non-friends", annoyingAvailabilityRole, statusType);
		
		/* Create a relation from one User to another with the roles
		 * "has contact" and "friend". */
		store.createRelation(transaction, userType, "has contact",
				"friend", friendRole, userType);
		
		/* Create a relation from one User to another with the roles
		 * "has contact" and "relative". */
		store.createRelation(transaction, userType, "has contact",
				"relative", relativeRole, userType);
		
		/* Create a relation from one User to another with the roles
		 * "has contact" and "acquaintance". */
		store.createRelation(transaction, userType, "has contact",
				"acquaintance", acquaintanceRole, userType);
		
		/* Create a relation from one User to another with the roles
		 * "has contact" and "workmate". */
		store.createRelation(transaction, userType, "has contact",
				"workmate", workmateRole, userType);
		
		/* Create a relation from one User to another with the roles
		 * "has contact" and "classmate". */
		store.createRelation(transaction, userType, "has contact",
				"classmate", classmateRole, userType);
		
		/* Create a relation from one User to another with the roles
		 * "has contact" and "annoying non-friend". */
		store.createRelation(transaction, userType, "has contact",
				"annoying non-friend", annoyingRole, userType);
		
		// Create the new complex type Message
		store.createType(transaction, repositoryModel, messageType, "Message");
		
		// Create a relation between Conversation and Message
		store.createRelation(transaction, conversationType, "in conversations",
				"has messages", messageRole, messageType);
		
		// Create a relation between User and Message
		store.createRelation(transaction, userType, "senders",
				"has sent", senderRole, messageType);

		// Create a relation from Message to Timestamp
		store.createRelation(transaction, messageType, null, "Send Time",
				messageSendTimeRole, PDStore.TIMESTAMP_TYPEID);
				
		// Create a relation from one Message to another
		store.createRelation(transaction, messageType, "has replies",
				"is reply to", replyRole, messageType);

		// Commit the transaction
		store.commit(transaction);
	}

	/* the GUIDs to identify a Conversation Repository, User, Status,
	 * Conversation and Message in the database. */
		// This GUID is public in order to make it accessible to the Messenger class
	public static final GUID conversationRepository = new GUID("9dcd9ddbe07111e1a01a001e101f3534");
	private static final GUID Available = new GUID("9dcd9de0e07111e1a01a001e101f3534");
	private static final GUID Busy = new GUID("9dcd9de4e07111e1a01a001e101f3534");
	private static final GUID Offline = new GUID("9dcd9de1e07111e1a01a001e101f3534");
	private static final GUID Bob = new GUID("e82e5c197e3e11e1a4740018dea49e3e");
	private static final GUID Ann = new GUID("9dcd9dd7e07111e1a01a001e101f3534");
	private static final GUID Nic = new GUID("9dcd9dd8e07111e1a01a001e101f3534");
	private static final GUID conversationBobAnn = new GUID("e82e5c1a7e3e11e1a4740018dea49e3e");
	private static final GUID conversationAnnNic = new GUID("e82e5c1b7e3e11e1a4740018dea49e3e");
	private static final GUID messageAnnOne = new GUID("e82e5c1c7e3e11e1a4740018dea49e3e");
	private static final GUID messageBobOne = new GUID("e82e5c1d7e3e11e1a4740018dea49e3e");
	private static final GUID messageNicOne = new GUID("e82e5c1e7e3e11e1a4740018dea49e3e");
	private static final GUID messageAnnTwo = new GUID("e82e5c1f7e3e11e1a4740018dea49e3e");
	
	private static void createExistingRepository(PDStore store) {
		// record the current time
		Timestamp currentTime = new java.sql.Timestamp(System.currentTimeMillis());
		
		// begin a new database transaction
		GUID transaction = store.begin();
		
		// put a Conversation Repository into the database
		store.setType(transaction, conversationRepository, conversationRepositoryType);
		store.setName(transaction, conversationRepository, "Conversation Repository");
		
		// Put the Statuses "Available", "Busy" and "Offline" into the database
		store.setType(transaction, Available, statusType);
		store.setName(transaction, Available, "Available");
		store.addLink(transaction, conversationRepository, statusRole, Available);
		
		store.setType(transaction, Busy, statusType);
		store.setName(transaction, Busy, "Busy");
		store.addLink(transaction, conversationRepository, statusRole, Busy);
		
		store.setType(transaction, Offline, statusType);
		store.setName(transaction, Offline, "Offline");
		store.addLink(transaction, conversationRepository, statusRole, Offline);
		
		// put the Users "Bob", "Ann" and "Nic" into the database
		store.setType(transaction, Bob, userType);
		store.setName(transaction, Bob, "Bob");
		store.addLink(transaction, conversationRepository, userRole, Bob);

		store.setType(transaction, Ann, userType);
		store.setName(transaction, Ann, "Ann");
		store.addLink(transaction, conversationRepository, userRole, Ann);

		store.setType(transaction, Nic, userType);
		store.setName(transaction, Nic, "Nic");
		store.addLink(transaction, conversationRepository, userRole, Nic);

		// Make Ann and Nic friends
		store.addLink(transaction, Ann, friendRole, Nic);
		store.addLink(transaction, Nic, friendRole, Ann);
		// Make Ann and Bob relatives
		store.addLink(transaction, Ann, relativeRole, Bob);
		store.addLink(transaction, Bob, relativeRole, Ann);
		// Make Bob Nic's classmate, and Nic Bob's annoying non-friend
		store.addLink(transaction, Nic, classmateRole, Bob);
		store.addLink(transaction, Bob, annoyingRole, Nic);
		
		/* Bob's friends and relatives will see his status as Available,
		 * his acquaintances, workmates and classmates will see it as Busy,
		 * and his annoying non-friends will see it as Offline. */
		store.addLink(transaction, Bob, friendAvailabilityRole, Available);
		store.addLink(transaction, Bob, relativeAvailabilityRole, Available);
		store.addLink(transaction, Bob, acquaintanceAvailabilityRole, Busy);
		store.addLink(transaction, Bob, workmateAvailabilityRole, Busy);
		store.addLink(transaction, Bob, classmateAvailabilityRole, Busy);
		store.addLink(transaction, Bob, annoyingAvailabilityRole, Offline);
		
		// Same for Ann
		store.addLink(transaction, Ann, friendAvailabilityRole, Available);
		store.addLink(transaction, Ann, relativeAvailabilityRole, Available);
		store.addLink(transaction, Ann, acquaintanceAvailabilityRole, Busy);
		store.addLink(transaction, Ann, workmateAvailabilityRole, Busy);
		store.addLink(transaction, Ann, classmateAvailabilityRole, Busy);
		store.addLink(transaction, Ann, annoyingAvailabilityRole, Offline);
		
		// Same for Nic
		store.addLink(transaction, Nic, friendAvailabilityRole, Available);
		store.addLink(transaction, Nic, relativeAvailabilityRole, Available);
		store.addLink(transaction, Nic, acquaintanceAvailabilityRole, Busy);
		store.addLink(transaction, Nic, workmateAvailabilityRole, Busy);
		store.addLink(transaction, Nic, classmateAvailabilityRole, Busy);
		store.addLink(transaction, Nic, annoyingAvailabilityRole, Offline);
		
		// let's put a Conversation between Bob and Ann into our database
		store.setType(transaction, conversationBobAnn, conversationType);
		store.setName(transaction, conversationBobAnn, "Important things get discussed");
		store.addLink(transaction, conversationRepository, conversationRole, conversationBobAnn);

		// add links for our conversationBobAnn instance
		store.addLink(transaction, conversationBobAnn, conversationStartTimeRole, currentTime);
			// ^ This will be automatically set on the instantiation of a Conversation
		// The Users participating in the Conversation are linked to it
		store.addLink(transaction, Bob, participantRole, conversationBobAnn);
		store.addLink(transaction, Ann, participantRole, conversationBobAnn);

		// and we add a Message to the database
		store.setType(transaction, messageAnnOne, messageType);
		store.setName(transaction, messageAnnOne, "Hi there Bob");
			// ^ The name of the message is used to store the message content
		store.addLink(transaction, Ann, senderRole, messageAnnOne);
			// ^ This will be automatically set on the instantiation of a Message
		store.addLink(transaction, messageAnnOne, messageSendTimeRole, currentTime);

		// we link the Message to conversationBobAnn
		store.addLink(transaction, conversationBobAnn, messageRole, messageAnnOne);
		
		// Put a second Conversation into the database:
		store.setType(transaction, conversationAnnNic, conversationType);
		store.setName(transaction, conversationAnnNic, "Girl Power");
		store.addLink(transaction, conversationRepository, conversationRole, conversationAnnNic);
		store.addLink(transaction, conversationAnnNic, conversationStartTimeRole, currentTime);
		store.addLink(transaction, Ann, participantRole, conversationAnnNic);
		store.addLink(transaction, Nic, participantRole, conversationAnnNic);
		
		// new Message for conversationBobAnn...
		store.setType(transaction, messageBobOne, messageType);
		store.setName(transaction, messageBobOne, "Hello yourself!");
		store.addLink(transaction, Bob, senderRole, messageBobOne);
		store.addLink(transaction, messageBobOne, messageSendTimeRole, currentTime);
			// messageBobOne is made a reply to messageAnnOne. The inverse relationship -
			// that messageAnnOne has the reply messageBobOne - is also created.
		store.addLink(transaction, messageBobOne, replyRole, messageAnnOne);
		store.addLink(transaction, conversationBobAnn, messageRole, messageBobOne);
		
		// new Message for conversationAnnNic...
		store.setType(transaction, messageNicOne, messageType);
		store.setName(transaction, messageNicOne, "Hear me roar!");
		store.addLink(transaction, Nic, senderRole, messageNicOne);
		store.addLink(transaction, messageNicOne, messageSendTimeRole, currentTime);
		store.addLink(transaction, conversationAnnNic, messageRole, messageNicOne);

		// new Message for conversationBobAnn...
		store.setType(transaction, messageAnnTwo, messageType);
		store.setName(transaction, messageAnnTwo, "No need to be rude...");
		store.addLink(transaction, Ann, senderRole, messageAnnTwo);
		store.addLink(transaction, messageAnnTwo, messageSendTimeRole, currentTime);
		store.addLink(transaction, messageAnnTwo, replyRole, messageBobOne);
		store.addLink(transaction, conversationBobAnn, messageRole, messageAnnTwo);

		// commit the transaction
		store.commit(transaction);
	}
	
	private static void createNewRepository(PDStore store) {
		// begin a new database transaction
		GUID transaction = store.begin();
		
		// put a Conversation Repository into the database
		store.setType(transaction, conversationRepository, conversationRepositoryType);
		store.setName(transaction, conversationRepository, "Conversation Repository");
		
		// commit the transaction
		store.commit(transaction);
	}

	private static void querySomeData(PDStore store, Object instance) {
		// begin a new database transaction
		GUID transaction = store.begin();

		// let's get the type for the given instance
		GUID type = store.getType(transaction, instance);

		// let's see what accessible roles the type of the instance has
		Collection<GUID> accessibleRoles = store.getAccessibleRoles(
				transaction, type);

		System.out.println("Instance "
				+ store.getNameOrValue(transaction, instance) + " has type "
				+ store.getNameOrValue(transaction, type)
				+ " and accessible roles with values:");
		for (GUID role : accessibleRoles) {
			Collection<Object> values = store.getInstances(transaction,
					instance, role);

			System.out.print(store.getNameOrValue(transaction, role) + " = ");
			for (Object v : values)
				System.out.print(store.getNameOrValue(transaction, v) + ", ");
			System.out.println();
		}
	}

	public static void main(String[] args) {
		// open existing PDStore database,
		// or create it if it doesn't exist
		// note: the default folder is "pddata",
		// and the filename will be "MessengerDatabase.pds"
		PDStore store = new PDStore("MessengerDatabase");
		
		// this method creates the conversation repository model
		createRepositoryModel(store);

		// Create a ready-made, populated Conversation Repository
		//createExistingRepository(store);
		
		// Create an empty, customisable Conversation Repository
		createNewRepository(store);

		// and we query & print out some data about the conversationRepository,
		// Available, Bob, Ann, Nic, conversationBobAnn,
		// messageAnnOne, messageBobOne and messageAnnTwo instances
		querySomeData(store, conversationRepository);
		/*
		System.out.println();
		querySomeData(store, Available);
		System.out.println();
		querySomeData(store, Bob);
		System.out.println();
		querySomeData(store, Ann);
		System.out.println();
		querySomeData(store, Nic);
		System.out.println();
		querySomeData(store, conversationBobAnn);
		System.out.println();
		querySomeData(store, messageAnnOne);
		System.out.println();
		querySomeData(store, messageBobOne);
		System.out.println();
		querySomeData(store, messageAnnTwo);
		*/
	}
}