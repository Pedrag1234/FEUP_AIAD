package amazon;

import jade.core.Agent;
import java.io.*;  
import java.util.Scanner;  

public class MainWarehouse extends Agent {

	private static final long serialVersionUID = 4363918672967765440L;
	
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
	
	
	private WareHouse wares;
	
	public MainWarehouse(){
		wares = new WareHouse();
		
		this.generateWareHouseStock();
	}
	
	private void generateWareHouseStock() {
		Scanner sc = new Scanner(new File("/docs/Stock.csv"));
		sc.useDelimiter(","); 
		
		while(sc.hasNext()) {
			String type = sc.next();
			Float price = sc.next();
			Integer stock = sc.next();
			
			wares.addNewItemStock(new Item(type,price),stock);
		}
	}
	
	public void print() {
		this.warehouse.printStock();
	}
	
	public void setup() {
		this.warehouse.printStock();
	}

	public WareHouse getWares() {
		return wares;
	}

	public void setWares(WareHouse wares) {
		this.wares = wares;
	}
}
