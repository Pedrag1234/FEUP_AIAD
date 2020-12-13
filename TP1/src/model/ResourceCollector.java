package model;

import java.util.HashMap;

import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import sajas.core.Agent;
import sajas.domain.DFService;

public class ResourceCollector extends Agent{
	
	private HashMap<Integer,Float> stores;
	
	private DFAgentDescription dfd;
	
	public ResourceCollector() {
		this.stores = new HashMap<>();
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

}
