package amazon;

import jade.core.Agent;

public class Store extends Agent {
	
	private WareHouse storeStorage;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Store(){
		this.setStoreStorage(new WareHouse());
	}

	public WareHouse getStoreStorage() {
		return storeStorage;
	}

	public void setStoreStorage(WareHouse storeStorage) {
		this.storeStorage = storeStorage;
	}

}
