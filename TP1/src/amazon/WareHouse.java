package amazon;

import java.util.ArrayList;

import jade.core.Agent;

public class WareHouse extends Agent {
	
	private static final long serialVersionUID = 1L;

	private ArrayList<ItemStock> stock;
	
	public WareHouse() {
		setStock(new ArrayList<>());
		
		stock.add(new ItemStock(100, "book", 10.03));
		stock.add(new ItemStock(200, "pen", 0.79));
	}
	
	public void setup() {
		this.printStock();
		
		try {
			
			this.stock.get(0).removeFromStock(60);
			
		} catch (Exception e) {
			
			System.out.print("Not enough stock");
			
		}
		this.printStock();
		
		try {
			
			this.stock.get(0).removeFromStock(60);
			
		} catch (Exception e) {
			System.out.print("Not enough stock");
		}
		this.printStock();
	}

	public ArrayList<ItemStock> getStock() {
		return stock;
	}

	public void setStock(ArrayList<ItemStock> stock) {
		this.stock = stock;
	}
	
	public void printStock() {
		for (ItemStock itemStock : stock) {
			System.out.print("Nº of items = "+itemStock.getStock() + " || type = " + itemStock.getType());
		}
	}
	
}
