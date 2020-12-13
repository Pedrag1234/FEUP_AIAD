package model;

import java.util.ArrayList;
import java.util.HashMap;

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
	
	public void setup(){
		this.register();
		
		
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
