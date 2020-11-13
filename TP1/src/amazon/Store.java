package amazon;

import java.util.Random;

import jade.core.Agent;

public class Store extends Agent {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private WareHouse storeStorage;
	private String area;
	
	public Store(int id, String area){
		this.id = id;
		this.setStoreStorage(new WareHouse());
		this.area = area;
		//this.generateRandomStorage();
		
		//storeStorage.printStock();
	}
	
	public String getArea() {
		return area;
	}
	
	private void generateRandomStorage() {
		/*int n_items = new Random().nextInt(types.length);
		
		for (int i = 0; i < n_items; i++) {
			int index = new Random().nextInt(types.length);
			int stock_sz = new Random().nextInt(150) + 50;
			
			try {
				storeStorage.addNewItemStock(new ItemStock(stock_sz, new Item(types[index], prices[index])));
			} catch (CantAddExistingItem e) {
				try {
					storeStorage.addStock(types[index], stock_sz);
				} catch (ItemDoesntExist e1) {
					e1.printStackTrace();
				}
			}
			
		}*/
	}

	public WareHouse getStoreStorage() {
		return storeStorage;
	}

	public void setStoreStorage(WareHouse storeStorage) {
		this.storeStorage = storeStorage;
	}

	@Override
	public String toString() {
		return "Store [id=" + id + ", storeStorage=" + storeStorage + ", area=" + area + "]";
	}
	
	
	
	
}
