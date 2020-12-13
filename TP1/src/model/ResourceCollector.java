package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import AgentBehaviours.D_STORE_CLIENT_CONFIRM_PURCHASE;
import AgentBehaviours.E_RESOURCE_LISTEN_RESPONSE;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import sajas.core.Agent;
import sajas.domain.DFService;

public class ResourceCollector extends Agent{
	
	private HashMap<Integer,Float> storesResults;
	private HashMap<Integer,Integer> storesItemCounter;
	private ArrayList<Integer> storeIds;
	private Writer f;
	
	private DFAgentDescription dfd;
	
	public ResourceCollector(Integer number_of_Ids) {
		this.storesResults = new HashMap<>();
		this.storesItemCounter = new HashMap<>();
		this.storeIds = new ArrayList<>();
		for (int i = 0; i < number_of_Ids; i++) {
			this.storeIds.add(i);
			this.storesResults.put(i, (float) 0);
		}
		
		Date g = new Date();
		long s = g.getTime();
		try {
			String asssss = "Output_" + s + ".txt";
			setF(new BufferedWriter(new FileWriter(asssss, true)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int get_number_stores() {
		return this.storeIds.size();
	}
	
	public void setup(){
		this.register();
		
		
		setTimeout(() -> addBehaviour(new E_RESOURCE_LISTEN_RESPONSE(this)), 2500);
		
		
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
		sd.setType("ResourceCollector");


		this.dfd = new DFAgentDescription();
		dfd.setName(getAID());
		dfd.addServices(sd);

		try {
			DFService.register(this, this.dfd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void printData(){
		for(Integer i : this.storesResults.keySet()) {
			System.out.println("Store_" + i + " has a profit of " + this.storesResults.get(i) + " $.");
		}
	}

	public float get_individual_profit(Integer i){
		
		return this.storesResults.get(i);
	}
	
	public int get_total_profit(){
		int sum = 0;
		
		for(Integer i : this.storesResults.keySet()) {
			sum += this.storesResults.get(i);
		}
		
		return sum;
	}
	
	public boolean isEmpty() {
		return (this.storesResults.size() == 0);
	}
	
	public void updateValues(Integer i, Float s) {
		if(this.storesResults.get(i) == null) {
			this.storesResults.put(i,s);
		}
		else {
			this.storesResults.remove(i);
			this.storesResults.put(i,s);
		}
		
		
		if(this.storesItemCounter.get(i) == null) {
			this.storesItemCounter.put(i,1);
		}
		else {
			
			this.storesItemCounter.put(i,storesItemCounter.get(i)+1);
		}
	}
	
	public int get_products_counter(int i) {
		if(this.storesItemCounter.get(i) == null) {
			return 0;
		}
		else {
			return this.storesItemCounter.get(i);
		}
	}

	public ArrayList<Integer> getStoreIds() {
		return storeIds;
	}

	public void setStoreIds(ArrayList<Integer> storeIds) {
		this.storeIds = storeIds;
	}

	public Writer getF() {
		return f;
	}

	public void setF(Writer f) {
		this.f = f;
	}

}
