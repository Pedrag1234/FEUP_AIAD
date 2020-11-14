package amazon;


import java.util.Hashtable;
import java.util.Stack;

import AgentBehaviours.StoreReqItem2WarehouseBehaviour;

import AgentBehaviours.StorePresentProduct2Client;

import AgentBehaviours.StoreRequestInventoryBehaviour;

import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class Store extends Agent {

	private static final long serialVersionUID = 1L;

	private double profit;
	private int n_customers;
	private int store_id;
	private String area;

	private Stack<Item> currItemOrder;
	private Stack<Integer> currItemNumber;
	private int order;
	private String sdType ="";
	
	private Hashtable<String,Integer> storeWarehouseStock = new Hashtable<>();

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
		loop.addSubBehaviour(new StoreRequestInventoryBehaviour(this));
		loop.addSubBehaviour(new StoreReqItem2WarehouseBehaviour(this));
		loop.addSubBehaviour(new StorePresentProduct2Client(this));

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


	public void setStoreWarehouseStock(Hashtable<String,Integer> newStock)
	{
		this.storeWarehouseStock = newStock;
	}
	
	public Hashtable<String,Integer> getStoreWarehouseStock()
	{
		return this.storeWarehouseStock;
	}





}
