package amazon;

import java.util.Random;

import jade.core.Agent;

public class Store extends Agent {

	private static final long serialVersionUID = 1L;
	
	private static final String[] types = {
			"Pen",
			"Book",
			"Pencil",
			"Pencil Case",
			"Calculator",
			"Eraser",
			"Laptop",
			"TV",
			"PC",
			"Monitors",
			"Smartphone",
			"DishWasher",
			"Laundry Machine",
			"Kitchen Utensils"
	};
	
	private static final double[] prices = {
			5.99,
			10.99,
			4.99,
			7.50,
			129.90,
			2.59,
			700.00,
			500.48,
			1250.99,
			350.33,
			599.99,
			329.99,
			289.00,
			26.00,	
	};
	private WareHouse storeStorage;
	
	
	public Store(){
		this.setStoreStorage(new WareHouse());
		
		//this.generateRandomStorage();
		
		//storeStorage.printStock();
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
	
	
}
