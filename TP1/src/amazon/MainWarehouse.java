package amazon;

import jade.core.Agent;
import java.io.BufferedReader;  
import java.io.FileReader;  
import java.io.IOException;

import StockExceptions.CantAddExistingItem;

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
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("/docs/Stock.csv"));
			String line = "";
			String splitBy = ",";  
			
			while((line = br.readLine()) != null) {
				String[] items = line.split(",");
				
				wares.addNewItemStock(new Item(items[0],Double.parseDouble(items[1])),Integer.parseInt(items[2]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (CantAddExistingItem e) {
			e.printStackTrace();
		}
	}
	
	public void print() {
		this.wares.printStock();
	}
	
	public void setup() {
		this.wares.printStock();
	}

	public WareHouse getWares() {
		return wares;
	}

	public void setWares(WareHouse wares) {
		this.wares = wares;
	}
}
