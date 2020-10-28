package amazon;

public class ItemStock {
	
	private Item item;
	private int stock;
	
	public ItemStock(int stock, String type, double price) {
		
		item = new Item(type, price, price);
		this.stock = stock;
		
	}
	
	
	public ItemStock(int stock, Item i) {
		
		try {
			item = i.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		this.stock = stock;
		
	}
	
	
    public void startPromotion(int promotionValue) {
    	item.applyPromotion(promotionValue);
	}
	
	public void endPromotion() {
		item.endPromotion();
	}
	
	public void addToStock(int number) {
		this.stock = this.stock + number;
	}
	
	public void removeFromStock(int number) throws NoStockException {
		if (this.stock - number < 0) {
			throw new NoStockException();
		}
		else {
			this.stock -= number;
		}
	}

	public Item getItem() {
		return item;
	}


	public void setItem(Item item) {
		this.item = item;
	}
	
	public int getStock() {
		return this.stock;
	}


	public void setStock(int stock) {
		this.stock = stock;
	}
}
