package angil.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.PDInstance;
import pdstore.dal.PDWorkingCopy;

/**
 * Data access class to represent instances of type "Action" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("angil.dal.PDAction");
 * @author PDGen
 */
public class PDAction implements PDInstance {

	public static final GUID typeId = new GUID("fbc29378374c11e0bfe4001e8c7f9d82"); 

	public static final GUID roleNextPageId = new GUID("005319b136ed11e08144001e8c7f9d82");
	public static final GUID roleProbabilityId = new GUID("005319b336ed11e08144001e8c7f9d82");
	public static final GUID roleRequestURLId = new GUID("005319b236ed11e08144001e8c7f9d82");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDAction.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDAction:" + name;
		else
			return "PDAction:" + id;
	}

	/**
	 * Creates an PDAction object representing a PDStore instance of type Action.
	 * @param store the store the instance should be in
	 */
	public PDAction(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDAction object representing the instance with the given ID. 
	 * The ID must be of an instance of type Action.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDAction(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Action with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDAction load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDAction)instance;
	}

	/**
	 * Gets the PDStore this object is stored in.
	 */
	public PDStore getStore() {
		return store;
	}

	/**
	 * Gets the GUID of the instance represented by this object.
	 */
	public GUID getId() {
		return id;
	}

	/**
	 * Gets the GUID of the type of the instance represented by this object.
	 * This method isn't static so that it can be part of the PDInstance interface.
	 */
	public GUID getTypeId() {
		return typeId;
	}

	/**
	 * Gets a textual label for this instance, for use in UIs.
	 * @return a textual label for the instance
	 */
	public String getLabel() {
		return store.getLabel(id);
	}

	/**
	 * Gets the name of this instance.
	 * In PDStore every instance can be given a name.
	 * @return name the instance name
	 */
	public String getName() {
		return store.getName(id);
	}

	/**
	 * Sets the name of this instance.
	 * In PDStore every instance can be given a name.
	 * If the instance already has a name, the name will be overwritten.
	 * If the given name is null, an existing name will be removed.
	 * @return name the new instance name
	 */
	public void setName(String name) {
		store.setName(id, name);
	}

	/**
	 * Removes the name of this instance.
	 * In PDStore every instance can be given a name.
	 * If the instance does not have a name, nothing happens.
	 */
	public void removeName() {
		store.removeName(id);
	}

	/**
	 * Gets the icon of this instance.
	 * In PDStore every instance can be given an icon.
	 * @return icon the instance icon
	 */
	public Blob getIcon() {
		return store.getIcon(id);
	}

	/**
	 * Sets the icon of this instance.
	 * In PDStore every instance can be given an icon.
	 * If the instance already has an icon, the icon will be overwritten.
	 * If the given icon is null, an existing icon will be removed.
	 * @return icon the new instance icon
	 */
	public void setIcon(Blob icon) {
		store.setIcon(id, icon);
	}

	/**
	 * Removes the icon of this instance.
	 * In PDStore every instance can be given an icon.
	 * If the instance does not have an icon, nothing happens.
	 */
	public void removeIcon() {
		store.removeIcon(id);
	}
	/**
	 * Returns the instance connected to this instance through the role nextPage.
	 * @return the connected instance
	 */
	public PDPage getNextPage() {
		GUID instanceId = (GUID) store.getInstance(this.id, roleNextPageId);
	 	return (PDPage) store.load(PDPage.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role nextPage.
	 * @return the connected instance(s)
	 */
	 public Collection<PDPage> getNextPages() {
		return (Collection<PDPage>)(Object)store.getAndLoadInstances(this.id, roleNextPageId, PDPage.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "nextPage".
	 * If the given instance is null, nothing happens.
	 * @param nextPage the instance to connect
	 */
	public void addNextPage(GUID nextPage) {
		if (nextPage != null) {
			store.addLink(this.id, roleNextPageId, nextPage);
		}
	}
	/**
	 * Connects this instance to the given instance using role "nextPage".
	 * If the given instance is null, nothing happens.
	 * @param nextPage the instance to connect
	 */
	public void addNextPage(PDPage nextPage) {
		if (nextPage != null)
			addNextPage(nextPage.getId());
	}

	/**
	 * Connects this instance to the given instance using role "nextPage".
	 * If the given collection of instances is null, nothing happens.
	 * @param nextPage the Collection of instances to connect
	 */
	public void addNextPages(Collection<PDPage> nextPages) {
		if (nextPages == null)
			return;
		for (PDPage instance : nextPages)
			addNextPage(instance);
	}

	/**
	 * Removes the link from this instance through role "nextPage".
	 */
	public void removeNextPage() {
		store.removeLink(this.id, roleNextPageId,
			store.getInstance(this.id, roleNextPageId));
	}

	/**
	 * Removes the link from this instance through role "nextPage" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeNextPage(Object nextPage) {
		if (nextPage == null)
			return;
		store.removeLink(this.id, roleNextPageId, nextPage);
	}

	/**
	 * Removes the links from this instance through role "nextPage" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeNextPages(Collection<PDPage> nextPages) {
		if (nextPages == null)
			return;
		for (PDPage instance : nextPages)
			store.removeLink(this.id, roleNextPageId, instance);
	}

	/**
	 * Connects this instance to the given instance using role "nextPage".
	 * If there is already an instance connected to this instance through role "nextPage", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param nextPage the instance to connect
	 */
	public void setNextPage(GUID nextPage) {
		store.setLink(this.id,  roleNextPageId, nextPage);
	}
	/**
	 * Connects this instance to the given instance using role "nextPage".
	 * If there is already an instance connected to this instance through role "nextPage", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param nextPage the instance to connect
	 */
	public void setNextPage(PDPage nextPage) {
		setNextPage(nextPage.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role probability.
	 * @return the connected instance
	 */
	public Double getProbability() {
	 	return (Double)store.getInstance(this.id, roleProbabilityId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role probability.
	 * @return the connected instance(s)
	 */
	 public Collection<Double> getProbabilitys() {
		return (Collection<Double>)(Object)store.getInstances(this.id, roleProbabilityId);
	 }

   /**
	 * Connects this instance to the given instance using role "probability".
	 * If the given instance is null, nothing happens.
	 * @param probability the instance to connect
	 */
	public void addProbability(Double probability) {
		if (probability != null) {
			store.addLink(this.id, roleProbabilityId, probability);
		}
	}
	/**
	 * Connects this instance to the given instances using role "probability".
	 * If the given collection of instances is null, nothing happens.
	 * @param probability the Collection of instances to connect
	 */
	public void addProbabilitys(Collection<Double> probabilitys) {
		if (probabilitys == null)
			return;
		for (Double instance : probabilitys)
			addProbability(instance);
	}

	/**
	 * Removes the link from this instance through role "probability".
	 */
	public void removeProbability() {
		store.removeLink(this.id, roleProbabilityId,
			store.getInstance(this.id, roleProbabilityId));
	}

	/**
	 * Removes the link from this instance through role "probability" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeProbability(Object probability) {
		if (probability == null)
			return;
		store.removeLink(this.id, roleProbabilityId, probability);
	}

	/**
	 * Connects this instance to the given instance using role "probability".
	 * If there is already an instance connected to this instance through role "probability", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param probability the instance to connect
	 */
	public void setProbability(Double probability) {
		store.setLink(this.id,  roleProbabilityId, probability);
	}
	/**
	 * Returns the instance connected to this instance through the role requestURL.
	 * @return the connected instance
	 */
	public String getRequestURL() {
	 	return (String)store.getInstance(this.id, roleRequestURLId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role requestURL.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getRequestURLs() {
		return (Collection<String>)(Object)store.getInstances(this.id, roleRequestURLId);
	 }

   /**
	 * Connects this instance to the given instance using role "requestURL".
	 * If the given instance is null, nothing happens.
	 * @param requestURL the instance to connect
	 */
	public void addRequestURL(String requestURL) {
		if (requestURL != null) {
			store.addLink(this.id, roleRequestURLId, requestURL);
		}
	}
	/**
	 * Connects this instance to the given instances using role "requestURL".
	 * If the given collection of instances is null, nothing happens.
	 * @param requestURL the Collection of instances to connect
	 */
	public void addRequestURLs(Collection<String> requestURLs) {
		if (requestURLs == null)
			return;
		for (String instance : requestURLs)
			addRequestURL(instance);
	}

	/**
	 * Removes the link from this instance through role "requestURL".
	 */
	public void removeRequestURL() {
		store.removeLink(this.id, roleRequestURLId,
			store.getInstance(this.id, roleRequestURLId));
	}

	/**
	 * Removes the link from this instance through role "requestURL" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeRequestURL(Object requestURL) {
		if (requestURL == null)
			return;
		store.removeLink(this.id, roleRequestURLId, requestURL);
	}

	/**
	 * Connects this instance to the given instance using role "requestURL".
	 * If there is already an instance connected to this instance through role "requestURL", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param requestURL the instance to connect
	 */
	public void setRequestURL(String requestURL) {
		store.setLink(this.id,  roleRequestURLId, requestURL);
	}

	@Override
	public PDWorkingCopy getPDWorkingCopy() {
		// TODO Auto-generated method stub
		return null;
	}
}
