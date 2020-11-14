package amazon;

import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import AgentBehaviours.A_CLIENT_STORE_RECEIVE_PRODUCTS_OFFER;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import AgentBehaviours.B_STORE_WAREHOUSE_REQUEST_REMOVE_ITEM;


public class Client extends Agent{
	
	private int id;
	private String area;
	private int money_to_spend;
	private double buy_Local;
	private double spender;
	private double suscetible;
	HashMap<String, Double> needs = new HashMap<String, Double>();
	ArrayList<Item> buy_list = new ArrayList<Item>();
	
	private DFAgentDescription dfd;
	
	
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
	
	public ArrayList<Item> decide_which_items_to_buy(ArrayList<HashMap<Item, Integer>> store_stock, Store store) {
		double money_spent_so_far = 0;
		HashMap<Item, Double> temp = new HashMap<Item, Double>();
		
		for (HashMap<Item, Integer> i : store_stock) {
			
			for (Item j : i.keySet()) {
				  temp.put(j, decide_if_buy(j,store));
			}
		      
		}
		
		temp = sortHashMapByValues(temp);
		
		ArrayList<Item> items_most_interested_in_buying = new ArrayList<>(temp.keySet());
		ArrayList<Item> buy_list_temp = new ArrayList<>();
		
		for (Item i : items_most_interested_in_buying) {
			
			var d = Math.random();
			if (d < temp.get(i)) {
				buy_list_temp.add(i);
			}
		}
		
		for (Item i : buy_list_temp) {
			
			if(i.getCurrentPrice() + money_spent_so_far < this.money_to_spend) {
				this.buy_list.add(i);
			}
			
		}
		
		return this.buy_list;
		
		
	}
	
	
	public double decide_if_buy(Item item, Store store) {
			
		double price = item.getCurrentPrice();
		
		double buying_local_chance = calculate_buying_local_chance(store); 
		double susceptibility = calculate_susceptibility(item);
		double money_percentage_remaining = calculate_money_percentage_remaining(price);
		
		if(money_percentage_remaining == -1) { //Check if the price of the item is bigger than the money available
			return 0;
		}
		
		double need = check_need(item);
		
		double chance_of_buying = (buying_local_chance + susceptibility + money_percentage_remaining + need)/4;
		System.out.println("Chance of buying was:" + chance_of_buying);
		
		return chance_of_buying;
		
		/*var d = Math.random();
		if (d < chance_of_buying)
		    return true;
		else 
		    return false;*/
		
			
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

	@Override
	public String toString() {
		return "Client [id=" + id + ", area=" + area + ", money_to_spend=" + money_to_spend + ", buy_Local=" + buy_Local
				+ ", spender=" + spender + ", suscetible=" + suscetible + ", needs=" + needs + "]";
	}


	public void register() {
		ServiceDescription sd = new ServiceDescription();
		
		sd.setName(getLocalName());
		System.out.println("Client_"+ this.id);
		sd.setType("Client_"+ this.id);
		
		
		this.dfd = new DFAgentDescription();
		dfd.setName(getAID());
		dfd.addServices(sd);
		
		try {
			DFService.register(this, this.dfd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setup() {
		this.register();
		SequentialBehaviour loop = new SequentialBehaviour();

		loop.addSubBehaviour(new A_CLIENT_STORE_RECEIVE_PRODUCTS_OFFER(this));

		addBehaviour(loop);
	}
	
	public LinkedHashMap<Item, Double> sortHashMapByValues(
	        HashMap<Item, Double> passedMap) {
	    List<Item> mapKeys = new ArrayList<>(passedMap.keySet());
	    List<Double> mapValues = new ArrayList<>(passedMap.values());
	    Collections.sort(mapValues);
	  //  Collections.sort(mapKeys);

	    LinkedHashMap<Item, Double> sortedMap =
	        new LinkedHashMap<>();

	    Iterator<Double> valueIt = mapValues.iterator();
	    while (valueIt.hasNext()) {
	    	Double val = valueIt.next();
	        Iterator<Item> keyIt = mapKeys.iterator();

	        while (keyIt.hasNext()) {
	        	Item key = keyIt.next();
	            Double comp1 = passedMap.get(key);
	            Double comp2 = val;

	            if (comp1.equals(comp2)) {
	                keyIt.remove();
	                sortedMap.put(key, val);
	                break;
	            }
	        }
	    }
	    return sortedMap;
	}
	
}
