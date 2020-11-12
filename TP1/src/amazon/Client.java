package amazon;

import jade.core.Agent;
import java.util.ArrayList;
import java.util.HashMap;

public class Client extends Agent{
	
	private int id;
	private String area;
	private int money_to_spend;
	private double buy_Local;
	private double spender;
	private double suscetible;
	HashMap<String, Double> needs = new HashMap<String, Double>();
	
	
	public Client(int id, String area, int money_to_spend, double buy_Local, double spender, double suscetible, HashMap<String, Double> needs) {
		this.id = id;
		this.area = area;
		this.money_to_spend = money_to_spend;
		this.buy_Local = buy_Local;
		this.spender = spender;
		this.suscetible = suscetible;
		this.needs = needs;
	}
	
	public double calculate_buying_local_chance (Store store) { 
	
		String store_area = store.getArea();
		
		if(this.area.equals(store_area)) {
			return this.buy_Local;
		}
		else {
			return 0;
		}
		
	}
	
	public double calculate_susceptibility(Item item) {
		double discount = item.getCurrentPrice()/item.getPrice();
		
		if(discount == 0) {
			return 0;
		}
		else {
			return (discount + this.suscetible)/2;
		}
		
	}
	
	public double calculate_money_percentage_remaining(double price) {
		double percentage = price/this.money_to_spend;
		if(percentage > 1) {
			return -1;
		}
		else return (1-percentage);
	}
	
	public double check_need(Item item) {
		for (String i : needs.keySet()) {
			  if(i == item.getType()) {
				  return needs.get(i);
			  }
		}
		
		return 0;
	}
	
	
	public boolean decide_if_buy(Item item, Store store) {
			
		double price = item.getCurrentPrice();
		
		double buying_local_chance = calculate_buying_local_chance(store); 
		double susceptibility = calculate_susceptibility(item);
		double money_percentage_remaining = calculate_money_percentage_remaining(price);
		
		if(money_percentage_remaining == -1) { //Check if the price of the item is bigger than the money available
			return false;
		}
		
		double need = check_need(item);
		
		double chance_of_buying = (buying_local_chance + susceptibility + money_percentage_remaining + need)/4;
		System.out.println("Chance of buying was:" + chance_of_buying);
		var d = Math.random();
		if (d < chance_of_buying)
		    return true;
		else 
		    return false;
		
			
	}
	
	


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public int getMoney_to_spend() {
		return money_to_spend;
	}

	public void setMoney_to_spend(int money_to_spend) {
		this.money_to_spend = money_to_spend;
	}

	public double getBuy_Local() {
		return buy_Local;
	}

	public void setBuy_Local(double buy_Local) {
		this.buy_Local = buy_Local;
	}

	public double getSpender() {
		return spender;
	}

	public void setSpender(double spender) {
		this.spender = spender;
	}

	public double getSuscetible() {
		return suscetible;
	}

	public void setSuscetible(double suscetible) {
		this.suscetible = suscetible;
	}


	
	
}
