package amazon;

import java.util.ArrayList;
import java.util.Iterator;

public class ItemStock {
	
	private int stock;
	private ArrayList<Item> items;
	private String type;
	private double price;
	
	public ItemStock(int stock, String type, double price) {
		
		setStock(stock);
		setType(type);
		setPrice(price);
		setItems(new ArrayList<Item>());
		
		for (int i = 0; i < stock; i++) {
			items.add(new Item(type,price));
		}
	}
	
	public int getStock() {
		return stock;
	}
	
	public void setStock(int stock) {
		this.stock = stock;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	public double getPrice() {
		return price;
	}

	
	public void setPrice(double price) {

		this.price = price;
	}
	
    public void startPromotion(int promotionValue) {
		
		for (Item item : items) {
			item.applyPromotion(promotionValue);
		}
		
	}
	
	public void endPromotion() {
		
		for (Item item : items) {
			item.endPromotion();
		}
		
	}
	
	public void addToStock(int number) {
		setStock(getStock() + number);
		
		if (items.size() == 0) {
			
			for (int i = 0; i < number; i++) {
				items.add(new Item(type,price));
			}
			
		}
		else {
			double currentPrice = items.get(0).getCurrentPrice();
			
			for (int i = 0; i < number; i++) {
				items.add(new Item(type,price,currentPrice));
			}
			
		}
	}
	
	public void removeFromStock(int number) {
	
	}
}
