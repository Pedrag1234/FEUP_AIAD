package amazon;


import java.util.Stack;

import jade.core.Agent;

import jade.core.behaviours.SequentialBehaviour;

public class Store extends Agent {

	private static final long serialVersionUID = 1L;
	
	private double profit;
	private int n_customers;
	private int store_id;
	private String area;
	
	private Stack<Item> currItemOrder;
	private Stack<Integer> currItemNumber;
	private int order;
	
	
	
	// value of the stock_size
	private double stock_sz_value;
	// maximum promotion value of the item
	private double maxPromo;
	// minimum promotion value of the item
	private double minPromo;
	
	
	private static final String[] types = {
			"Pen",
			"Book",
			"Pencil",
			"Pencil Case",
			"Calculator",
			"Eraser",
			"Laptop",
			"TV",
			"PC",
			"Monitors",
			"Smartphone",
			"DishWasher",
			"Laundry Machine",
			"Kitchen Utensils"
	};
	
	private static final double[] prices = {
			5.99,
			10.99,
			4.99,
			7.50,
			129.90,
			2.59,
			700.00,
			500.48,
			1250.99,
			350.33,
			599.99,
			329.99,
			289.00,
			26.00,	
	};
	
	
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
	
	void sendMessage2WareHouse() {
		//TODO: communication
	}
	
	void sendMessage2Cliente() {
		//TODO: communication
	}
	
	//TODO: needs to receive client data
	private double calculatePriceOffer(Item s, int n_items, int stock_size) {
		
		
		
		double randomPromo = Math.random() * (this.maxPromo - this.minPromo) + this.minPromo;
		
		int promotion = (int) (100 * (stock_size * this.stock_sz_value + randomPromo));
		
		s.applyPromotion(promotion);
		
		double price = s.getCurrentPrice();
		
		
		return price * n_items;
	}
	
	
	public void setup() {
		SequentialBehaviour loop = new SequentialBehaviour();
		//TODO: add behaviors
		
		
		addBehaviour(loop);
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

	
	
	 
	


	
}
