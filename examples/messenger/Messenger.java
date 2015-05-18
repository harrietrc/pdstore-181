package messenger;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import pdstore.GUID;
import pdstore.PDChange;
import pdstore.PDStore;

import pdstore.notify.PDListener;
import pdstore.ui.treeview.PDTreeView;

public class Messenger extends JFrame implements ActionListener {
	/* Stephen Hood 05/09/12: Class ExampleTreeViewer from the package "book" was the starting
	 * point for this tree viewer/annotator for the Conversation Repository data model created
	 * by the MessengerInit class. MessengerInit must be run once before this class for
	 * the data model it creates to be viewable via this class.
	 * 
	 * Two pairs of messaging and awareness windows are displayed: A detected change
	 * to the content in any messaging window causes the message tree contained in all
	 * messaging windows to be updated - unfortunately collapsed down to its root node.
	 * If necessary, the awareness window's content is also refreshed.
	 * 
	 * This is currently a very rough prototype - among other things, PDTreeView doesn't
	 * seem to be the ideal way of viewing message tree data. */
	
	private class Pair {
		// Designed to enable the return of two objects from a method
		public final List<Object> list;
		public final Object[] array;

		public Pair(List<Object> l, Object[] a) {
			list = l;
			array = a;
		}
	}
	
	private class Three {
		public final JComboBox comboBox;
		public final JButton button;
		public final GUID availabilityRole;
		
		public Three(JComboBox c, JButton b, GUID a) {
			comboBox = c;
			button = b;
			availabilityRole = a;
		}
	}
	
	private static final long serialVersionUID = 2180772173827561959L;
	private static final PDStore store = new PDStore("MessengerDatabase");
	// Prepare Conversation Repositories to be the root nodes in messaging windows
	private static final Object rootNode = MessengerInit.conversationRepository;
	
	/* Changes in the Conversation Repository are detected by listening for alterations
	 * to the links between the instantiated types contained within it.
	 * Note that for two-way relationships (e.g. "has replies" - "is reply to"),
	 * only changes made in the second direction are detected e.g. "is reply to"
	 * but not "has replies". */
	private static final GUID[] listenTo = {MessengerInit.conversationRole,
		MessengerInit.userRole, MessengerInit.participantRole, MessengerInit.messageRole,
		MessengerInit.messageSendTimeRole, MessengerInit.replyRole};
		// Use PDStore.NAME_ROLEID to keep track of name changes -
		// or are names immutable once set?
	// Listen to type(instance2) instead of role2
	private static final GUID[] listenToStatus = {MessengerInit.statusType};
	private static final GUID[] listenToUser = {
		MessengerInit.friendAvailabilityRole, MessengerInit.relativeAvailabilityRole,
		MessengerInit.acquaintanceAvailabilityRole, MessengerInit.workmateAvailabilityRole,
		MessengerInit.classmateAvailabilityRole, MessengerInit.annoyingAvailabilityRole,
		MessengerInit.friendRole, MessengerInit.relativeRole,
		MessengerInit.acquaintanceRole, MessengerInit.workmateRole,
		MessengerInit.classmateRole, MessengerInit.annoyingRole};
	
	// All of the different roles through which two users can be contacts
	private static final GUID[] contactsForUserList = {MessengerInit.friendRole,
		MessengerInit.relativeRole, MessengerInit.acquaintanceRole,
		MessengerInit.workmateRole, MessengerInit.classmateRole,
		MessengerInit.annoyingRole};
	// All of the different statuses a user can display to different categories of contact
	private static final GUID[] statusesForUserList = {MessengerInit.friendAvailabilityRole,
		MessengerInit.relativeAvailabilityRole, MessengerInit.acquaintanceAvailabilityRole,
		MessengerInit.workmateAvailabilityRole, MessengerInit.classmateAvailabilityRole,
		MessengerInit.annoyingAvailabilityRole};
	// Names for the different categories of contact
	private static final String[] namesForUserList = {"Friends",
		"Relatives", "Acquaintances",
		"Workmates", "Classmates",
		"Annoying Non-friends"};
	
	// Display settings
	private static final GridBagLayout gridBag = new GridBagLayout();
	private static final GridBagConstraints leftColumn = new GridBagConstraints();
	private static final GridBagConstraints comboBox = new GridBagConstraints();
	private static final GridBagConstraints wholeWidth = new GridBagConstraints();
	private static final GridBagConstraints button = new GridBagConstraints();
	
	// The GUID of the currently instantiated user
	private final GUID user;
	
	// For the messaging window
	private PDTreeView treeView;
	private final JScrollPane messagingScrollPane;
	
	// For the awareness window
	private final JScrollPane statusChangeScrollPane, awarenessScrollPane;
	private final JPanel statusChangePanel;
	private final JTextArea textArea;
	// This field is made accessible to the actionPerformed() function
	private final JTextField newStatus;
	
	// To be used for modifying the current User's contact lists
	private final Collection<Three> addContact = new ArrayList<Three>();
	private final Collection<Three> removeContact = new ArrayList<Three>();
	
	public Messenger (String name) throws
			IllegalArgumentException, SecurityException, InstantiationException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		/* If there is no User instance in the database which has the name provided,
		 * create one - otherwise, use the pre-existing User instance. */
		boolean userNotFound = true;
		GUID temp = new GUID();
		Collection<GUID> instances = store.getIds(name);
		for (GUID instance : instances) {
			if (store.getType(instance).equals(MessengerInit.userType)) {
				temp = instance;
				userNotFound = false;
				break;
			}
		}
		if (userNotFound) {
			// Begin a new database transaction
			GUID transaction = store.begin();
			store.setType(transaction, temp, MessengerInit.userType);
			store.setName(transaction, temp, name);
			store.addLink(transaction, MessengerInit.conversationRepository,
					MessengerInit.userRole, temp);
			// Commit the transaction
			store.commit(transaction);
		}
		user = temp;
		
		// Create a messaging window
		treeView = new PDTreeView(store, rootNode);
		messagingScrollPane = new JScrollPane(treeView);
		JFrame messaging = new JFrame(name + " - Messaging");
		messaging.setLayout(new BorderLayout());
		messaging.add(messagingScrollPane, BorderLayout.CENTER);
		messaging.setSize(400, 600);
		// Eventually pop up messaging windows at will for specific Conversations
		messaging.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// Create an awareness window...
		
		// First create the awareness window's status change controls
		statusChangePanel = new JPanel(gridBag);
		statusChangePanel.setBackground(new Color(255, 255, 255));
		/* Create both a List of all Status objects, and Array of the names of all Status
		 * objects, existing within the system at the time of a user's instantiation. */
		Pair statusNames = newStatusNamesList(store);
		// Create the label/combo box pairs for display
		int categories = namesForUserList.length;
		for (int count = 0; count < categories; ++count) {
			statusChangePanel.add(new JLabel(namesForUserList[count]), leftColumn);
			statusChangePanel.add(newJComboBox(count, statusNames, store), comboBox);
		}
		// Add the New Status Creator
		statusChangePanel.add(new JLabel(""), wholeWidth);
		statusChangePanel.add(new JLabel("Add New Status"), wholeWidth);
		newStatus = new JTextField(10); // The JTextField has a minimum width of 10 columns
		newStatus.addActionListener(this);
		statusChangePanel.add(newStatus, leftColumn);
		JButton create = new JButton("Create");
		create.addActionListener(this);
		statusChangePanel.add(create, button);
		statusChangeScrollPane = new JScrollPane(statusChangePanel);
		
		// Then create the awareness window's contact display
		
		//
		// The start of developing an interactive contact display
		JComboBox chooseContact;
		JButton actionChoice;
		for (GUID availabilityRole : statusesForUserList) {
			// Add all Users not currently in this particular contact list to the JComboBox
			chooseContact = new JComboBox();
			actionChoice = new JButton("Add");
			addContact.add(new Three(chooseContact, actionChoice, availabilityRole));
			
			// Add all Users currently in this particular contact list to the JComboBox
			chooseContact = new JComboBox();
			actionChoice = new JButton("Remove");
			removeContact.add(new Three(chooseContact, actionChoice, availabilityRole));
		}
		// The start of developing an interactive contact display
		//
		
		String userList = newUserList(store);
		textArea = new JTextArea(userList);
		textArea.setEditable(false);
		awarenessScrollPane = new JScrollPane(textArea);
		
		// And then add them both to the awareness window
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				statusChangeScrollPane, awarenessScrollPane);
		setTitle(name + " - Awareness");
		setLayout(new BorderLayout());
		add(splitPane, BorderLayout.CENTER);
		setSize(400, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create event listeners to detect changes to the content of any messaging window
		// Listen to role2 instead of type(instance2)
		createListeners(MessengerListener.class, listenTo, null);

        /* Create event listeners to detect changes to the content of any messaging
         * window which also affect the awareness window's content. */
		// Listen to type(instance2) instead of role2
		GUID role2[] = {PDStore.HAS_TYPE_ROLEID};
        createListeners(MessengerStatusListener.class, role2, listenToStatus);
        
        /* Create event listeners to detect changes to the content of any messaging
         * window which also affect the awareness window's content. */
        // Listen to role2 instead of type(instance2)
        createListeners(MessengerUserListener.class, listenToUser, null);

        messaging.setVisible(true);
        setVisible(true);
	}
	
	private Pair newStatusNamesList(PDStore store) {
		// Create an up-to-date List of all Status objects existing within the system
		List<Object> statuses = (List<Object>) store.getAllInstancesOfType(
				MessengerInit.statusType);
		Collection<String> statusNames = new ArrayList<String>();
		statusNames.add("");
		for (Object status : statuses) {
			statusNames.add(store.getName(status));
		}
		/* Create an up-to-date Array of the names of all
		 * Status objects existing within the system. */
		Object[] names = statusNames.toArray();
		return new Pair(statuses, names);
	}
	
	private JComboBox newJComboBox(final int count, final Pair pair,
			PDStore store) {
		JComboBox statusList = new JComboBox(pair.array);
		/* Set the JComboBox's index to match the status (if it exists) being
		 * displayed to contacts in the current category. */
		Object status = store.getInstance(user, statusesForUserList[count]);
		if (status != null) {
			statusList.setSelectedIndex(pair.list.indexOf(status) + 1);
		}
		statusList.addActionListener(new ActionListener() {
			// Make action specific to particular Combo box
			public void actionPerformed(ActionEvent event) {
				JComboBox statusList = (JComboBox)event.getSource();
				/* Only assign a new status if one has been chosen from the combo box -
				 * if none has been chosen, make the combo box display the
				 * name of the existing status. */
				PDStore newStore = new PDStore("MessengerDatabase");
				if (statusList.getSelectedIndex() == 0) {
					/* Set the JComboBox's index to match the status being
					 * displayed to contacts in the current category. */
					statusList.setSelectedIndex(pair.list.indexOf(
							newStore.getInstance(user, statusesForUserList[count])) + 1);
				}
				else {
					GUID newStatus = newStore.getId((String)statusList.getSelectedItem());
					// begin a new database transaction
					// "store" is used so that the messaging window will see the change
					GUID transaction = Messenger.store.begin();
					Messenger.store.setLink(transaction, user, statusesForUserList[count], newStatus);
					// commit the transaction
					Messenger.store.commit(transaction);
				}
			}
		});
		return statusList;
	}
	
	private String newUserList(PDStore store) {
		/* Creates an itemised string containing the names and provided statuses of all
		 * users who are contacts of the current user.
		 * TODO: If a user has another in a contact role, an error is produced if the
		 * other user doesn't also have the current user in at least one contact role.
		 * Null userNameRole/statusAvailabilityRole Strings produce an error.
		 * Listen for changes to userType/statusType Name instead of
		 * userNameRole/statusAvailabilityRole, then
		 * remove userNameRole/statusAvailabilityRole from data structure.
		 * Multiple contact roles per contact are supported,
		 * but all corresponding statuses are provided. */
		List<String> statuses;
		Collection<Object> contacts, otherUserContacts;
		int count, otherCount, statusesSize;
		int categories = contactsForUserList.length;
		String userList = "";
		// Iterate through the contact roles for the currently instantiated user
		for (count = 0; count < categories; ++count) {
			userList += namesForUserList[count] + "\n";
			contacts = store.getInstances(user, contactsForUserList[count]);
			// Iterate through each other user in the currently selected contact role with user
			for (Object contact : contacts) {
				/* Add other user to the appropriate contact list, along with the status(es)
				 * they wish to display to the currently instantiated user. */
				
				statuses = new ArrayList<String>();
				// Iterate through the contact roles for the other user
				for (otherCount = 0; otherCount < categories; ++otherCount) {
					otherUserContacts = store.getInstances(contact,
							contactsForUserList[otherCount]);
					/* If the currently instantiated user is found in the current contact
					 * role, store the status which the other user has associated
					 * with this contact role. */
					if (otherUserContacts.contains(user)) {
						statuses.add(store.getName(store.getInstance(contact,
								statusesForUserList[otherCount])));
					}
				}

				userList += "   Name: " + store.getName(contact) +
						", Status: " + statuses.get(0);
				/* If the other user has listed the currently instantiated user in more
				 * than one contact role (meaning that multiple different statuses might
				 * be provided for the other user), list all statuses provided -
				 * separated by commas. */
				statusesSize = statuses.size();
				for (otherCount = 1; otherCount < statusesSize; ++otherCount) {
					userList += ", " + statuses.get(otherCount);
				}
				userList += "\n";
			}
		}
		return userList;
	}
	
	/* Can either listen to role2 or type(instance2).
	 * Note: Only works if more than one role2 is to be listened to. */
	private <listenerType> void createListeners(Class<listenerType> listen, GUID[] role2, GUID[] instance2)
			throws IllegalArgumentException, SecurityException, InstantiationException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		listenerType listener;
		PDChange<GUID, Object, GUID> changeTemplate;
		if (role2.length == 1) {
			for (GUID instance : instance2) {
				listener = listen.getConstructor(Messenger.class).newInstance(this);
	        	changeTemplate = new PDChange<GUID, Object, GUID>(
	    				null, null, null, role2[0], instance);
	        	store.getListenerDispatcher().addListener((PDListener<GUID, Object, GUID>)listener,
	        			changeTemplate);
	        			// Type cast required to achieve compatibility with addListener() method
	        }
		}
		else {
			for (GUID role : role2) {
				listener = listen.getConstructor(Messenger.class).newInstance(this);
	        	changeTemplate = new PDChange<GUID, Object, GUID>(
	    				null, null, null, role, null);
	        	store.getListenerDispatcher().addListener((PDListener<GUID, Object, GUID>)listener,
	        			changeTemplate);
	        			// Type cast required to achieve compatibility with addListener() method
	        }
		}
	}
	
	public void doRefresh() {
		/* Called when a change to the content of any messaging window is
		 * detected. An updated message tree is displayed in the messaging
		 * window - unfortunately collapsed down to its root node.
		 */
		// Update messaging window
		treeView = new PDTreeView(store, rootNode);
		messagingScrollPane.setViewportView(treeView);
	}
	
	public void doStatusRefresh() {
		/* Called when a change to the content of any messaging window -
		 * which also affects the awareness window's content - is
		 * detected. An updated message tree is displayed in the messaging
		 * window - unfortunately collapsed down to its root node.
		 * The awareness window is also refreshed.
		 */
		// Update messaging window
		doRefresh();
		
		// Update awareness window
		PDStore newStore = new PDStore("MessengerDatabase");
		/* Create both a List of all Status objects, and Array of the names of all
		 * Status objects, currently existing within the system. */
		Pair statusNames = newStatusNamesList(newStore);
		/* Replace outdated JComboBoxes with ones containing
		 * an up-to-date selection of Statuses. */
		int count, categories = namesForUserList.length;
		for (count = categories * 2 - 1; count > 0; count -= 2) {
			statusChangePanel.remove(count);
		}
		JComboBox newBox;
		for (count = 0; count < categories; ++count) {
			newBox = newJComboBox(count, statusNames, newStore);
			gridBag.setConstraints(newBox, comboBox);
			statusChangePanel.add(newBox, count * 2 + 1);
		}
		statusChangeScrollPane.setViewportView(statusChangePanel);
	}
	
	public void doUserRefresh() {
		/* Called when a change to the content of any messaging window -
		 * which also affects the awareness window's content - is
		 * detected. An updated message tree is displayed in the messaging
		 * window - unfortunately collapsed down to its root node.
		 * The awareness window is also refreshed.
		 */
		// Update messaging window
		doRefresh();
		
		// Update awareness window
		PDStore newStore = new PDStore("MessengerDatabase");
		String userList = newUserList(newStore);		
		textArea.setText(userList);
		awarenessScrollPane.setViewportView(textArea);
	}
	
	public static void main(String[] args) throws
			IllegalArgumentException, SecurityException, InstantiationException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		// Display settings
		button.anchor = GridBagConstraints.WEST;
		button.fill = GridBagConstraints.VERTICAL;
		leftColumn.fill = comboBox.fill = GridBagConstraints.BOTH;
		button.weightx = leftColumn.weightx = comboBox.weightx = 1.0;
		wholeWidth.weightx = 0.0;
		wholeWidth.ipady = 10;
        comboBox.gridwidth = wholeWidth.gridwidth = button.gridwidth
        		= GridBagConstraints.REMAINDER; // End the row
        
		new Messenger("Bob");
		new Messenger("Ann");
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		boolean statusButton = false;
		boolean addButton = false;
		boolean removeButton = false;
		// If the source of the event is a JButton
		if (event.getSource().getClass() == JButton.class) {
			JButton jButton = (JButton)event.getSource();
			if (jButton.getText().equals("Create")) {
				statusButton = true; // A new Status is being added
			}
			else if (jButton.getText().equals("Add")) {
				addButton = true; // A User is being added to a contact list
			}
			else {
				removeButton = true; // A User is being removed from a contact list
			}
		}
		
		// If a new Status is being added
		if (statusButton || (event.getSource().getClass() == JTextField.class)) {
			/* Create a new Status if a non-empty string has been provided
			 * which doesn't match the name of a pre-existing Status. */
			String statusName = newStatus.getText();
			if (!statusName.isEmpty()) {
				// Remove the content of the JTextField newStatus
				newStatus.setText(null);

				boolean statusNotFound = true;
				PDStore newStore = new PDStore("MessengerDatabase");
				Collection<Object> statuses = newStore.getAllInstancesOfType(MessengerInit.statusType);
				for (Object status : statuses) {
					if (newStore.getName(status).equals(statusName)) {
						statusNotFound = false;
						break;
					}
				}
				if (statusNotFound) {
					GUID newStatus = new GUID();
					// Begin a new database transaction
					// ("store" is used so that the messaging window will see the change)
					GUID transaction = store.begin();
					store.setName(transaction, newStatus, statusName);
					store.setType(transaction, newStatus, MessengerInit.statusType);
					// Commit the transaction
					store.commit(transaction);
				}
			}
		}
		// If the current User is adding another User to one of their contact lists
		else if (addButton) {
			// TODO
			System.out.println("You should never see this");
		}
		// If the current User is removing another User from one of their contact lists
		else if (removeButton) {
			// TODO
			System.out.println("You should never see this");
		}
	}
}
