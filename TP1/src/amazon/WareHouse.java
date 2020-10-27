package amazon;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class WareHouse {
	
	private Hashtable<String,ItemStock> stock;
	
	public WareHouse() {
		setStock(new Hashtable<String,ItemStock>());
	}
	
	
	public Hashtable<String,ItemStock> getStock() {
		return stock;
	}

	public void setStock(Hashtable<String,ItemStock> hashtable) {
		this.stock = hashtable;
	}
	
	public void printStock() {
		Set<Map.Entry<String,ItemStock>> entries = stock.entrySet();
		
		entries.forEach( entry ->{
			System.out.println("Type : "+  entry.getValue().getType() + 
					"|| Stock = " + entry.getValue().getStock() +
					"|| Price = " + entry.getValue().getPrice());
		} );
		
	}
	
	public void startPromotion(String itemName, int promoValue) throws NoStockException,ItemDoesntExist {
		
		if(stock.get(itemName) != null && stock.get(itemName).getStock() > 0) {
			stock.get(itemName).startPromotion(promoValue);
		}
		else {
			if(stock.get(itemName) != null) {
				throw new ItemDoesntExist();
			}
			else {
				throw new NoStockException();
			}
		}
		
	}
	
	public void endPromotion(String itemName) throws NoStockException,ItemDoesntExist {
		
		if(stock.get(itemName) != null && stock.get(itemName).getStock() > 0) {
			stock.get(itemName).endPromotion();
		}
		else {
			if(stock.get(itemName) != null) {
				throw new ItemDoesntExist();
			}
			else {
				throw new NoStockException();
			}
		}
		
	}
	
	
	public void removeStock(String itemName, int number) throws NoStockException,ItemDoesntExist{
		if(stock.get(itemName) != null) {
			stock.get(itemName).removeFromStock(number);
		}
		else {
			throw new ItemDoesntExist();
		}
	}
	
	
	public void addStock(String itemName, int number) throws ItemDoesntExist{
		if(stock.get(itemName) != null) {
			stock.get(itemName).addToStock(number);;
		}
		else {
			throw new ItemDoesntExist();
		}
	}
	
	public void addNewItemStock(int stockN, String type, double price) throws CantAddExistingItem{
		if(stock.get(type) == null) {
			stock.put(type,new ItemStock(stockN, type, price));
		}
		else {
			throw new CantAddExistingItem();
		}
	}
	
	public void removeItemStock(String type) {
		stock.remove(type);
	}
	
}
