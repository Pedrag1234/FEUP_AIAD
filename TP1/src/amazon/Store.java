package amazon;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Map.Entry;


import AgentBehaviours.B_STORE_WAREHOUSE_REQUEST_REMOVE_ITEM;
import AgentBehaviours.C_STORE_WAREHOUSE_RECEIVE_INVENTORY;
import AgentBehaviours.A_CLIENT_STORE_RECEIVE_PRODUCTS_OFFER;
import AgentBehaviours.A_STORE_CLIENT_PRESENT_PRODUCT_OFFER;

import AgentBehaviours.C_STORE_WAREHOUSE_REQUEST_INVENTORY;

import AgentBehaviours.D_STORE_CLIENT_CONFIRM_PURCHASE;
import AgentBehaviours.E_STORE_RESOURCE_TERMINATION;
import sajas.core.Agent;
import sajas.core.behaviours.SequentialBehaviour;
import sajas.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class Store extends Agent {

	private static final long serialVersionUID = 1L;
	
	final int CHEAPEST = 0;
	final int MOST_EXPENSIVE = 1;
	final int RAREST = 2;
	final int COMMON = 3;
	final int IN_PROMOTION = 4;
	
	public boolean has_inventory = false;

	private double profit;
	private int n_customers;
	private int store_id;
	private int sales = 0;
	private String area;
	private String strategy1;
	
	private String currClientId = "";
	private Stack<Item> currItemOrder;
	private Stack<Integer> currItemNumber;
	private int order;
	private String sdType ="";
	private ArrayList<Item> offering = new ArrayList<Item>();
	private ArrayList<Client> clients = new ArrayList<Client>();
	public boolean proposal_accepted = false;
	
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
		
		int strategy = (int) (Math.random() * 3);
		
		switch (strategy) {
			case CHEAPEST: {
				this.strategy1 = "Cheapest";
				break;
			}
			case MOST_EXPENSIVE: {
				this.strategy1 = "Most Expensive";
				break;
			}
			case IN_PROMOTION: {
				this.strategy1 = "In Promotion";
				break;
			}
			case RAREST: {
				this.strategy1 = "Rarest";
				break;
			}
			case COMMON: {
				this.strategy1 = "Common";
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + strategy);
		}
		
	}
	
	@Override
	protected void setup() {	
		
		this.register();
		
		
	//	SequentialBehaviour loop = new SequentialBehaviour();
		addBehaviour(new C_STORE_WAREHOUSE_REQUEST_INVENTORY(this));
		addBehaviour(new C_STORE_WAREHOUSE_RECEIVE_INVENTORY(this));
		
		setTimeout(() -> addBehaviour(new A_STORE_CLIENT_PRESENT_PRODUCT_OFFER(this)), 500);
		
		setTimeout(() -> addBehaviour(new D_STORE_CLIENT_CONFIRM_PURCHASE(this)), 2500);
		
		setTimeout(() -> addBehaviour(new E_STORE_RESOURCE_TERMINATION(this)), 45000);
		

		
		
		
	//	addBehaviour(loop);
	}
	
	  public static void setTimeout(Runnable runnable, int delay) {
	        new Thread(() -> {
	            try {
	                Thread.sleep(delay);
	                runnable.run();
	            } catch (Exception e) {
	                System.err.println(e);
	            }
	        }).start();
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
				
		switch (this.strategy1) {
			case "Cheapest": {
				returnItems = getCheapestItems(); 
				break;
			}
			case "Most Expensive": {
				returnItems = getMostExpensiveItems(); 
				break;
			}
			case "In Promotion": {
				returnItems = getItemsInPromotion(); 
				break;
			}
			case "Rarest": {
				returnItems = getRarestItems(); 
				break;
			}
			case "Common": {
				returnItems = getMostCommonItems(); 
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + strategy);
		}

		return returnItems;
	}
	
	private ArrayList<Item> getCheapestItems(){
		ArrayList<Item> cheapest = new ArrayList<>();
		
		Item minItem = null; 
		
		for (int i = 0; i < 4; i++) {
			
			Set<Map.Entry<Item, Integer>> entries = this.storeWarehouseStock.entrySet();
			Iterator<Map.Entry<Item, Integer>> itr = entries.iterator();
			
			Entry<Item, Integer> entry = null;
			
			int j = 0;
			
			while (itr.hasNext()) {
				
				entry = itr.next();
				
				if(j == 0 && cheapest.contains(entry.getKey()) == false) {
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
	
	private ArrayList<Item> getMostExpensiveItems(){
		ArrayList<Item> expensive = new ArrayList<>();
		
		Item maxItem = null; 
		
		for (int i = 0; i < 4; i++) {
			
			Set<Map.Entry<Item, Integer>> entries = this.storeWarehouseStock.entrySet();
			Iterator<Map.Entry<Item, Integer>> itr = entries.iterator();
			
			Entry<Item, Integer> entry = null;
			
			int j = 0;
			
			while (itr.hasNext()) {
				
				entry = itr.next();
				
				if(j == 0 && expensive.contains(entry.getKey()) == false) {
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
	
	private ArrayList<Item> getItemsInPromotion(){
		ArrayList<Item> promotion = new ArrayList<>();
		
		int i = 0;
		
		Object[] temp = this.storeWarehouseStock.keySet().toArray();
		
		while(i < 4) {
			
			int random_index = (int) (Math.random() * temp.length);
			Item random_item = (Item) temp[random_index];
			
			
			if(!promotion.contains(random_item)) {
				i++;
				
				int stock_sz = this.storeWarehouseStock.get(random_item);
				
				this.calculatePriceOffer(random_item, stock_sz);
				
				promotion.add(random_item);
			}
			
			
		}
		
		
		return promotion;
	}
	
	
	
	public String getStrategy1() {
		return strategy1;
	}


	public void setStrategy1(String strategy1) {
		this.strategy1 = strategy1;
	}


	private ArrayList<Item> getRarestItems(){
		ArrayList<Item> rarest = new ArrayList<>();
		
		Item minItem = null; 
		
		for (int i = 0; i < 4; i++) {
			
			Set<Map.Entry<Item, Integer>> entries = this.storeWarehouseStock.entrySet();
			Iterator<Map.Entry<Item, Integer>> itr = entries.iterator();
			
			Entry<Item, Integer> entry = null;
			
			int j = 0;
			
			while (itr.hasNext()) {
				
				entry = itr.next();
				
				if(j == 0 && rarest.contains(entry.getKey())==false) {
					j++;
					minItem = entry.getKey();
				}
				else if(entry.getValue() < this.storeWarehouseStock.get(minItem)) {
					if (!rarest.contains(entry.getKey())) {
						minItem = entry.getKey();
					}
				}
				else {
					continue;
				}
	
			}
			rarest.add(minItem);
		}
		
		return rarest;
	}
	
	private ArrayList<Item> getMostCommonItems(){
		ArrayList<Item> common = new ArrayList<>();
		
		Item maxItem = null; 
		
		for (int i = 0; i < 4; i++) {
			
			Set<Map.Entry<Item, Integer>> entries = this.storeWarehouseStock.entrySet();
			Iterator<Map.Entry<Item, Integer>> itr = entries.iterator();
			
			Entry<Item, Integer> entry = null;
			
			int j = 0;
			
			while (itr.hasNext()) {
				
				entry = itr.next();
				
				if(j == 0 && common.contains(entry.getKey())) {
					j++;
					maxItem = entry.getKey();
				}
				else if(entry.getValue() > this.storeWarehouseStock.get(maxItem)) {
					if (!common.contains(entry.getKey())) {
						maxItem = entry.getKey();
					}
				}
				else {
					continue;
				}
	
			}
			common.add(maxItem);
		}
		
		return common;
	}

	
 	private void calculatePriceOffer(Item s, int stock_size) {

		double randomPromo = Math.random() * (this.maxPromo - this.minPromo) + this.minPromo;

		int promotion = (int) (100 * (stock_size * this.stock_sz_value + randomPromo));

		s.applyPromotion(promotion);

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
	
	public void addItemToCurrItemOrder (Item itemToAdd)
	{
		this.currItemOrder.push(itemToAdd);
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


	public String getCurrClientId() {
		return currClientId;
	}


	public void setCurrClientId(String currClientId) {
		this.currClientId = currClientId;
	}


	public ArrayList<Item> getOffering() {
		return offering;
	}


	public void setOffering(ArrayList<Item> offering) {
		this.offering = offering;
	}


	public ArrayList<Client> getClients() {
		return clients;
	}


	public void setClients(ArrayList<Client> clients) {
		this.clients = clients;
	}

	public int getSales() {
		return sales;
	}

	public void setSales(int sales) {
		this.sales = sales;
	}

	
	// m�todo takeDown
	protected void takeDown() {

		// retira registo no DF
		try {
			DFService.deregister(this);
		} catch(FIPAException e) {
			e.printStackTrace();
		}
	}
} // fim da classe Store
