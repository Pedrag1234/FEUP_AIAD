package amazon;

import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Set;

public class WareHouse {
	
	private Hashtable<Item,Integer> stock;
	
	public WareHouse() {
		setStock(new Hashtable<Item,Integer>());
	}
	
	
	public Hashtable<Item,Integer> getStock() {
		return stock;
	}

	public void setStock(Hashtable<Item,Integer> hashtable) {
		this.stock = hashtable;
	}
	
	public void printStock() {
		Set<Entry<Item,Integer>> entries = stock.entrySet();
		
		entries.forEach( entry ->{
			System.out.println("Type : "+  entry.getKey().getType() + 
					"|| Stock = " + entry.getValue() +
					"|| Price = " + entry.getKey().getPrice());
		} );
		
	}
	
	public void startPromotion(String itemName, int promoValue) throws ItemDoesntExist {
		if(stock.get(itemName) != null){
			Set<Entry<Item,Integer>> entries = stock.entrySet();
			
			entries.forEach( entry ->{
				if(entry.getKey().equals(itemName))
					entry.getKey().applyPromotion(promoValue);
			} );
			
		}
		
		else {
			throw new ItemDoesntExist();
		}
	}
	
	public void endPromotion(String itemName) throws ItemDoesntExist {
		
		if(stock.get(itemName) != null){
			Set<Entry<Item,Integer>> entries = stock.entrySet();
			
			entries.forEach( entry ->{
				if(entry.getKey().equals(itemName))
					entry.getKey().endPromotion();
			} );
			
		}
		else {
			throw new ItemDoesntExist();
		}
		
	}
	
	
	public void removeStock(String itemName, int number) throws NoStockException,ItemDoesntExist{
		if(stock.get(itemName) != null){
			stock.remove();
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
	
	public void addNewItemStock(String itemName, int number) throws CantAddExistingItem{
		if(stock.get(itemName) != null){
			throw new CantAddExistingItem();
		}
		else {
			stock.put(itemName, number);
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
