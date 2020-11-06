package amazon;

import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Set;

import StockExceptions.*;

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
	
	public void startPromotion(Item itemName, int promoValue) throws ItemDoesntExist {
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
	
	public void endPromotion(Item itemName) throws ItemDoesntExist {
		
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
	
	
	public void removeStock(Item itemName, int number) throws NoStockException,ItemDoesntExist{
		if(stock.get(itemName) != null){
			stock.remove(itemName);
		}
		else {
			throw new ItemDoesntExist();
		}
	}
	
	
	public void addStock(Item itemName, int number) throws ItemDoesntExist{
		if(stock.get(itemName) != null){
			stock.put(itemName, number);
		}
		else {
			throw new ItemDoesntExist();
		}
	}
	
	public void addNewItemStock(Item itemName, int number) throws CantAddExistingItem{
		if(stock.get(itemName) != null){
			throw new CantAddExistingItem();
		}
		else {
			stock.put(itemName, number);
		}
	}
	
	public void removeItemStock(Item type) throws CantRemoveInexistantItem {
		if(stock.get(type) != null){
			stock.remove(type);
		}
		else {
			throw new CantRemoveInexistantItem();
		}
	}
	
}
