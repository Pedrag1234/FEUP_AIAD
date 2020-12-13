package model;

import java.util.ArrayList;
import java.util.HashMap;

import AgentBehaviours.D_STORE_CLIENT_CONFIRM_PURCHASE;
import AgentBehaviours.E_RESOURCE_LISTEN_RESPONSE;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import sajas.core.Agent;
import sajas.domain.DFService;

public class ResourceCollector extends Agent{
	
	private HashMap<Integer,Float> storesResults;
	private ArrayList<Integer> storeIds;
	
	private DFAgentDescription dfd;
	
	public ResourceCollector(Integer number_of_Ids) {
		this.storesResults = new HashMap<>();
		this.storeIds = new ArrayList<>();
		for (int i = 0; i < number_of_Ids; i++) {
			this.storeIds.add(i);
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
	}

	public ArrayList<Integer> getStoreIds() {
		return storeIds;
	}

	public void setStoreIds(ArrayList<Integer> storeIds) {
		this.storeIds = storeIds;
	}

}
