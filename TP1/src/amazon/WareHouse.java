package amazon;

import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Set;

public class WareHouse {
	
	private Hashtable<String,ItemStock> stock;
	
	public WareHouse() {
		setStock(new Hashtable<String,ItemStock>());
	}
	
	
	public Hashtable<String, ItemStock> getStock() {
		return stock;
	}

	public void setStock(Hashtable<String, ItemStock> hashtable) {
		this.stock = hashtable;
	}
	
	public void printStock() {
		Set<Entry<String, ItemStock>> entries = stock.entrySet();
		
		entries.forEach( entry ->{
			System.out.println("Type : "+  entry.getKey() + 
					"|| Stock = " + entry.getValue().getStock() +
					"|| Price = " + entry.getValue().getItem().getPrice());
		} );
		
	}
	
	public void startPromotion(String itemName, int promoValue) throws ItemDoesntExist {
		if(stock.get(itemName) != null){
			stock.get(itemName).startPromotion(promoValue);
		}
		else {
			throw new ItemDoesntExist();
		}
	}
	
	public void endPromotion(String itemName) throws ItemDoesntExist {
		
		if(stock.get(itemName) != null){
			stock.get(itemName).endPromotion();
		}
		else {
			throw new ItemDoesntExist();
		}
		
	}
	
	
	public void removeStock(String itemName, int number) throws NoStockException,ItemDoesntExist{
		if(stock.get(itemName) != null){
			stock.get(itemName).removeFromStock(number);
		}
		else {
			throw new ItemDoesntExist();
		}
	}
	
	
	public void addStock(String itemName, int number) throws ItemDoesntExist{
		if(stock.get(itemName) != null){
			stock.get(itemName).addToStock(number);
		}
		else {
			throw new ItemDoesntExist();
		}
	}
	
	public void addNewItemStock(ItemStock s) throws CantAddExistingItem{
		if(stock.get(s.getItem().getType()) != null){
			throw new CantAddExistingItem();
		}
		else {
			stock.put(s.getItem().getType(), s);
		}
	}
	
	public void removeItemStock(String type) throws CantRemoveInexistantItem {
		if(stock.get(type) != null){
			stock.remove(type);
		}
		else {
			throw new CantRemoveInexistantItem();
		}
	}
	
}
