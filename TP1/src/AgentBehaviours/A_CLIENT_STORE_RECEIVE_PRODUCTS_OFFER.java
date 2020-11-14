package AgentBehaviours;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import amazon.Item;
import amazon.Store;
import amazon.Client;

import StockExceptions.ItemDoesntExist;
import StockExceptions.NoStockException;
import amazon.MainWarehouse;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class A_CLIENT_STORE_RECEIVE_PRODUCTS_OFFER extends SimpleBehaviour{
	
	private Client client;
	private boolean complete;
	
	public A_CLIENT_STORE_RECEIVE_PRODUCTS_OFFER(Client client) {
		this.client = client;
		this.complete = false;
	}
	
	@Override
	public void action() {
		
		
		for (int i = 0; i < this.client.getStores_To_Contact().size(); i++) {
			ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
			
			DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			
			
			sd.setType("Store_"+this.client.getStores_To_Contact().get(i).getStore_id());
			dfd.addServices(sd);
			
			try {
				DFAgentDescription[] result = DFService.search(this.client, dfd);
				
				System.out.println(result.length);
				
				for (int n = 0; n < result.length; n++) {
					
					AID dest = result[n].getName();
					request.addReceiver(dest);
					
					System.out.println(dest.getName());
					
					this.complete = true;
					this.client.send(request);
				}
			
			} catch (FIPAException e) {
				e.printStackTrace();
			}
			
			MessageTemplate mt =  MessageTemplate.MatchPerformative(ACLMessage.AGREE);
			ACLMessage msg = this.client.blockingReceive(mt);
			
			if(msg != null) {
				
				try {
					ArrayList<Item> products = (ArrayList<Item>) msg.getContentObject();
					
					HashMap<Item, Integer> res = new HashMap<>();
					
					for (int j = 0; j < products.size(); j++) {
						res.put(products.get(j), 0);
					}
					
					this.client.addProposedItems(res);
					this.complete = true;
					
				} catch (UnreadableException e) {
					e.printStackTrace();
				}
				
				
				
			}
		}
		

		
		return;
	}
		
	
	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return this.complete;
	}

}
