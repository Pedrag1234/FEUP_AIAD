package amazon;



import sajas.core.Agent;
import sajas.core.behaviours.SequentialBehaviour;
import sajas.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import AgentBehaviours.A_CLIENT_STORE_RECEIVE_PRODUCTS_OFFER;
import AgentBehaviours.A_CLIENT_STORE_REQUEST_PRODUCTS_OFFER;
import AgentBehaviours.A_STORE_CLIENT_PRESENT_PRODUCT_OFFER;
import AgentBehaviours.B_STORE_WAREHOUSE_REQUEST_REMOVE_ITEM;
import AgentBehaviours.D_CLIENT_STORE_BUY_ITEM;
import AgentBehaviours.D_CLIENT_STORE_RECEIVE_BUY_CONFIRMATION;
import AgentBehaviours.D_STORE_CLIENT_CONFIRM_PURCHASE;


public class Client extends Agent{

	private int id;
	private int storesToCheckPerClient = 2;
	private String area;
	private double money_to_spend;
	private double money_spent = 0;
	private double buy_Local;
	private double spender;
	private double suscetible;



	private ArrayList<Store> stores_available = new ArrayList<Store>();
	private ArrayList<Store> stores_to_contact = new ArrayList<Store>();

	private HashMap<Item,Integer> proposals ;
	private HashMap<Item,Integer> buy_list = new HashMap<Item, Integer>();
	private HashMap<String, Double> needs = new HashMap<String, Double>();
	private HashMap<Store, Integer> has_received_products = new HashMap<Store, Integer>();


	private DFAgentDescription dfd;




	public Client(int id, String area, double money_to_spend, double buy_Local, double spender, double suscetible, HashMap<String, Double> needs, ArrayList<Store> stores_available) {
		this.id = id;
		this.area = area;
		this.money_to_spend = money_to_spend;
		this.buy_Local = buy_Local;
		this.spender = spender;
		this.suscetible = suscetible;
		this.needs = needs;
		this.stores_available = stores_available;
		this.setProposals(new HashMap<Item, Integer>());
		decide_which_stores_to_contact();

	}

	public void decide_which_stores_to_contact() {

		ArrayList<Integer> list = new ArrayList<Integer>();		
		ArrayList<Integer> listStores = new ArrayList<Integer>();
		for (int i = 0; i < stores_available.size(); i++) {
			listStores.add(i);
		}
		
		Collections.shuffle(listStores);

	    for (int i = 0; i < storesToCheckPerClient; i++) {
	        list.add(listStores.get(i));
	    }
//	    System.out.println("lista stores mixed: " + listStores);
//	    System.out.println("lista stores escolhida: " + list);
		for (int i=0; i<list.size(); i++) {
			int d = list.get(i);

			stores_to_contact.add(stores_available.get(d));
			stores_available.get(d).getClients().add(this);
			has_received_products.put(stores_available.get(d), 0);
		}


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

	public void decide_which_items_to_buy(HashMap<Item, Integer> store_stock) {

		//	System.out.println(store_stock);

		double money_spent_so_far = 0;
		HashMap<Item, Double> temp = new HashMap<Item, Double>();


		for (Item j : store_stock.keySet()) {
			temp.put(j, decide_if_buy(j, this.stores_available.get(store_stock.get(j)) ));

		}


		temp = sortHashMapByValues(temp);

		ArrayList<Item> items_most_interested_in_buying = new ArrayList<>(temp.keySet());
		ArrayList<Item> buy_list_temp = new ArrayList<>();

		//	System.out.println("A" + items_most_interested_in_buying);

		for (Item i : items_most_interested_in_buying) {

			var d = Math.random();
			if (d < temp.get(i)) {
				buy_list_temp.add(i);
			}
		}



		for (Item i : buy_list_temp) {

			if(i.getCurrentPrice() + money_spent_so_far < this.money_to_spend) {
				this.buy_list.put(i,this.stores_available.get(store_stock.get(i)).getStore_id());
			}

		}

		return;


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
		//	System.out.println("Chance of buying " + item + " was:" + chance_of_buying);

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

	public double getMoney_to_spend() {
		return money_to_spend;
	}

	public void setMoney_to_spend(double money_to_spend) {
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

	public void removeFromBuy_list(Item i) {
		this.buy_list.remove(i);
	}



	public double getMoney_spent() {
		return money_spent;
	}

	public void setMoney_spent(double money_spent) {
		this.money_spent = money_spent;
	}

	@Override
	public String toString() {
		return "Client [id=" + id + ", area=" + area + ", money_to_spend=" + money_to_spend + ", buy_Local=" + buy_Local
				+ ", spender=" + spender + ", suscetible=" + suscetible + ", needs=" + needs + "]";
	}


	public void register() {
		ServiceDescription sd = new ServiceDescription();

		sd.setName(getLocalName());
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



		setTimeout(() -> addBehaviour(new A_CLIENT_STORE_REQUEST_PRODUCTS_OFFER(this)), 500);

		SequentialBehaviour temp = new SequentialBehaviour();

		temp.addSubBehaviour(new A_CLIENT_STORE_RECEIVE_PRODUCTS_OFFER(this));
		temp.addSubBehaviour(new D_CLIENT_STORE_BUY_ITEM(this));
		temp.addSubBehaviour(new D_CLIENT_STORE_RECEIVE_BUY_CONFIRMATION(this));

		setTimeout(() -> addBehaviour(temp), 5000);




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

	public LinkedHashMap<Item, Double> sortHashMapByValues(
			HashMap<Item, Double> passedMap) {
		List<Item> mapKeys = new ArrayList<>(passedMap.keySet());
		List<Double> mapValues = new ArrayList<>(passedMap.values());
		Collections.sort(mapValues);
		Collections.reverse(mapValues);
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

	public HashMap<Item, Integer> getProposals() {
		return proposals;
	}

	public void setProposals(HashMap<Item, Integer> proposals) {
		this.proposals = proposals;
	}

	public ArrayList<Store> getStores_To_Contact() {
		return this.stores_to_contact;
	}

	public HashMap<Item, Integer> getBuy_list() {
		return buy_list;
	}

	public void setBuy_list(HashMap<Item, Integer> buy_list) {
		this.buy_list = buy_list;
	}


	public HashMap<Store, Integer> getHas_received_products() {
		return has_received_products;
	}

	public void setHas_received_products(HashMap<Store, Integer> has_received_products) {
		this.has_received_products = has_received_products;
	}

	public void insert_into_has_received_products (Store store, Integer bool) {
		this.has_received_products.put(store, bool);
	}

	public void takeDown()
	{
		// retira registo no DF
		try {
			DFService.deregister(this);
		} catch(FIPAException e) {
			e.printStackTrace();
		}
	}


}
