package address;

import java.util.Collection;
import java.util.List;

import pdstore.*;
import pdstore.PDChange;
import pdstore.dal.*;
import pdstore.generic.PDCoreI;
import pdstore.notify.PDListener;
import pdstore.notify.PDListenerAdapter;
import address.dal.*;

public class AddressExample {

	public static void main(String[] args) {
		PDStore store = new PDStore("MyDatabaseFile");

		PDCustomer customer = new PDCustomer(store);
		
		// add detached listener
		List<PDListener<GUID,Object,GUID>> listeners = store.getDetachedListenerList();
		listeners.add(new PDListenerAdapter<GUID,Object,GUID>(){
			public void transactionCommitted(
					List<PDChange<GUID, Object, GUID>> transaction,
					List<PDChange<GUID, Object, GUID>> matchedChanges, PDCoreI<GUID, Object, GUID> core){
				for (PDChange<GUID, Object, GUID> change : transaction)
					System.out.println(change);
			}
		});

		PDAddress address = new PDAddress(store);
		customer.addAddress(address);
		address.addStreetName("Queen Street");
		address.addHouseNo(100L);
		store.commit();

		address.setStreetName("Symond Street");
		store.commit();

		System.out.println(address.getStreetName());
		Collection<String> streetNames = address.getStreetNames();

		// other operations
		address.removeStreetName("Symond Street");
		address = PDAddress.load(store, new GUID(
				"3fd075fb43b611e0b98f842b2b9af4fd"));

		// all data instances have a GUID
		GUID addressId = address.getId();

		// loading existing data instances by their GUID
		PDAddress address2 = PDAddress.load(store, addressId);

		// data instances can be given a name
		address2.setName("My Address");
		
		// looking up the GUID of an instance with their name
		GUID address2Id = store.getId("My Address");
		PDAddress address3 = PDAddress.load(store, address2Id);
	}

}
