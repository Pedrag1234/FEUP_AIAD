package amazon;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Map.Entry;

import AgentBehaviours.B_STORE_WAREHOUSE_REQUEST_REMOVE_ITEM;

import AgentBehaviours.A_STORE_CLIENT_PRESENT_PRODUCT_OFFER;

import AgentBehaviours.C_STORE_WAREHOUSE_REQUEST_INVENTORY;

import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class Store extends Agent {

	private static final long serialVersionUID = 1L;
	
	final int CHEAPEST = 0;
	final int MOST_EXPENSIVE = 1;
	final int IN_PROMOTION = 2;
	final int RAREST = 3;

	private double profit;
	private int n_customers;
	private int store_id;
	private String area;

	private Stack<Item> currItemOrder;
	private Stack<Integer> currItemNumber;
	private int order;
	private String sdType ="";
	
	private Hashtable<Item,Integer> storeWarehouseStock = new Hashtable<Item,Integer>();

	private DFAgentDescription dfd;


	// value of the stock_size
	private double stock_sz_value;
	// maximum promotion value of the item
	private double maxPromo;
	// minimum promotion value of the item
	private double minPromo;



	public Store(int store_id,
				 int maxPromo,
				 int minPromo,
				 int stock_sz_value,
				 String area){


		this.setProfit(0);
		this.setN_customers(0);
		this.setStore_id(store_id);
		this.setArea(area);

		this.setMaxPromo(maxPromo);
		this.setMinPromo(minPromo);
		this.setStock_sz_value(stock_sz_value);


		this.currItemNumber = new Stack<>();
		this.currItemOrder = new Stack<>();
		this.order = 0;

	}


	public void register() {
		ServiceDescription sd = new ServiceDescription();

		sd.setName(getLocalName());
		sd.setType("Store_"+this.store_id);
		sdType = "Store_"+this.store_id;

		this.dfd = new DFAgentDescription();
		dfd.setName(getAID());
		dfd.addServices(sd);

		try {
			DFService.register(this, this.dfd);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public ArrayList<Item> generateProducts2Offer() {
		
		ArrayList<Item> returnItems = null;
		
		
		int strategy = (int) (Math.random() * 3);
		
		
			
		switch (strategy) {
			case CHEAPEST: {
				System.out.println("Cheapest");
				returnItems = getCheapestItems(); 
				break;
			}
			case MOST_EXPENSIVE: {
				System.out.println("Most Expensive");
				returnItems = getMostExpensiveItems(); 
				break;
			}
			case IN_PROMOTION: {
				System.out.println("Cheapest");
				returnItems = getCheapestItems(); 
				break;
			}
			case RAREST: {
				System.out.println("Most Expensive");
				returnItems = getMostExpensiveItems(); 
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + strategy);
		}
			
		
		
		return returnItems;
	}
	
	public ArrayList<Item> getCheapestItems(){
		ArrayList<Item> cheapest = new ArrayList<>();
		
		Item minItem = null; 
		
		for (int i = 0; i < 4; i++) {
			
			Set<Map.Entry<Item, Integer>> entries = this.storeWarehouseStock.entrySet();
			Iterator<Map.Entry<Item, Integer>> itr = entries.iterator();
			
			Entry<Item, Integer> entry = null;
			
			int j = 0;
			
			while (itr.hasNext()) {
				
				entry = itr.next();
				
				if(j == 0 && cheapest.contains(entry.getKey())) {
					j++;
					minItem = entry.getKey();
				}
				else if(entry.getKey().getCurrentPrice() < minItem.getCurrentPrice()) {
					if (!cheapest.contains(entry.getKey())) {
						minItem = entry.getKey();
					}
				}
				else {
					continue;
				}
	
			}
			cheapest.add(minItem);
		}
		
		return cheapest;
	}
	
	public ArrayList<Item> getMostExpensiveItems(){
		ArrayList<Item> expensive = new ArrayList<>();
		
		Item maxItem = null; 
		
		for (int i = 0; i < 4; i++) {
			
			Set<Map.Entry<Item, Integer>> entries = this.storeWarehouseStock.entrySet();
			Iterator<Map.Entry<Item, Integer>> itr = entries.iterator();
			
			Entry<Item, Integer> entry = null;
			
			int j = 0;
			
			while (itr.hasNext()) {
				
				entry = itr.next();
				
				if(j == 0 && expensive.contains(entry.getKey())) {
					j++;
					maxItem = entry.getKey();
				}
				else if(entry.getKey().getCurrentPrice() > maxItem.getCurrentPrice()) {
					if (!expensive.contains(entry.getKey())) {
						maxItem = entry.getKey();
					}
				}
				else {
					continue;
				}
	
			}
			expensive.add(maxItem);
		}
		
		return expensive;
	}

	//TODO: needs to receive client data
	/*private double calculatePriceOffer(Item s, int n_items, int stock_size) {



		double randomPromo = Math.random() * (this.maxPromo - this.minPromo) + this.minPromo;

		int promotion = (int) (100 * (stock_size * this.stock_sz_value + randomPromo));

		s.applyPromotion(promotion);

		double price = s.getCurrentPrice();


		return price * n_items;
	}*/


	public void setup() {
		this.register();
		SequentialBehaviour loop = new SequentialBehaviour();
		loop.addSubBehaviour(new C_STORE_WAREHOUSE_REQUEST_INVENTORY(this));
		loop.addSubBehaviour(new B_STORE_WAREHOUSE_REQUEST_REMOVE_ITEM(this));
		loop.addSubBehaviour(new A_STORE_CLIENT_PRESENT_PRODUCT_OFFER(this));

		addBehaviour(loop);
	}

	



	@Override
	public String toString() {
		return "Store [profit=" + profit + ", n_customers=" + n_customers + ", store_id=" + store_id + ", area=" + area
				+ ", stock_sz_value=" + stock_sz_value + ", maxPromo=" + maxPromo + ", minPromo=" + minPromo + "]";
	}
	
	public String getStoreType()
	{
		return sdType;
	}


	public double getProfit() {
		return profit;
	}

	public void setProfit(double profit) {
		this.profit = profit;
	}

	public int getN_customers() {
		return n_customers;
	}

	public void setN_customers(int n_customers) {
		this.n_customers = n_customers;
	}

	public int getStore_id() {
		return store_id;
	}

	public void setStore_id(int store_id) {
		this.store_id = store_id;
	}

	public double getMaxPromo() {
		return maxPromo;
	}

	public void setMaxPromo(int maxPromo) {
		this.maxPromo = maxPromo;
	}

	public double getMinPromo() {
		return minPromo;
	}

	public void setMinPromo(int minPromo) {
		this.minPromo = minPromo;
	}

	public double getStock_sz_value() {
		return stock_sz_value;
	}

	public void setStock_sz_value(double stock_sz_value) {
		this.stock_sz_value = stock_sz_value;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Stack<Item> getCurrItemOrder() {
		return currItemOrder;
	}

	public void setCurrItemOrder(Stack<Item> currItemOrder) {
		this.currItemOrder = currItemOrder;
	}

	public Stack<Integer> getCurrItemNumber() {
		return currItemNumber;
	}

	public void setCurrItemNumber(Stack<Integer> currItemNumber) {
		this.currItemNumber = currItemNumber;
	}


	public int getOrder() {
		return order;
	}


	public void setOrder(int order) {
		this.order = order;
	}


	public void setStoreWarehouseStock(Hashtable<Item,Integer> newStock)
	{
		this.storeWarehouseStock = newStock;
	}
	
	public Hashtable<Item,Integer> getStoreWarehouseStock()
	{
		return this.storeWarehouseStock;
	}





}
