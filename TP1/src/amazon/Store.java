package amazon;

import jade.core.Agent;

public class Store extends Agent {

	private static final long serialVersionUID = 1L;
	
	private double profit;
	private int n_customers;
	private int store_id;
	
	
	
	// price value according to how much stock there is
	private double stock_sz_value;
	
	// price value if client is a spender or not
	private double spender_value;
	
	// price value if client is wealth or not
	private double economic_value;
	
	// price value if client is wealth or not
	private double sus_value;
	
	
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
				 double stock_sz_value, 
				 double spender_value,
				 double economic_value,
				 double sus_value){
		
		
		this.setProfit(0);
		this.setN_customers(0);
		this.setStore_id(store_id);
		
		
		
		this.setStock_sz_value(stock_sz_value);
		this.setSpender_value(spender_value);
		this.setEconomic_value(economic_value);
		this.setSus_value(sus_value);
		
	}
	
	void sendMessage2WareHouse() {
		//TODO: communication
	}
	
	void sendMessage2Cliente() {
		//TODO: communication
	}
	
	//TODO: needs to receive client data
	private double calculatePriceOffer(Item s, int n_items) {
		
		int promotion = 0;
		
		s.applyPromotion(promotion);
		
		double price = s.getCurrentPrice();
		
		
		return price * n_items;
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

	public double getStock_sz_value() {
		return stock_sz_value;
	}

	public void setStock_sz_value(double stock_sz_value) {
		this.stock_sz_value = stock_sz_value;
	}

	public double getSpender_value() {
		return spender_value;
	}

	public void setSpender_value(double spender_value) {
		this.spender_value = spender_value;
	}

	public double getEconomic_value() {
		return economic_value;
	}

	public void setEconomic_value(double economic_value) {
		this.economic_value = economic_value;
	}

	public double getSus_value() {
		return sus_value;
	}

	public void setSus_value(double sus_value) {
		this.sus_value = sus_value;
	}
	
	
	 
	


	
}
