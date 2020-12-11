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
import sajas.core.behaviours.SimpleBehaviour;
import sajas.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class A_CLIENT_STORE_RECEIVE_PRODUCTS_OFFER extends SimpleBehaviour{

	private Client client;
	private boolean complete;
	private int counter = 0;

	public A_CLIENT_STORE_RECEIVE_PRODUCTS_OFFER(Client client) {
		this.client = client;
		this.complete = false;
	}

	@Override
	public void action() {

		
		
		for (int i = 0; i < this.client.getStores_To_Contact().size(); i++) {
			
			MessageTemplate mt =  MessageTemplate.MatchPerformative(ACLMessage.AGREE);
			ACLMessage msg = this.client.receive(mt);

			if(msg != null) {

				try {
					
					this.client.insert_into_has_received_products(this.client.getStores_To_Contact().get(i), 1);
					HashMap<Item,Integer> products = (HashMap<Item, Integer>) msg.getContentObject();
					
					Set<Map.Entry<Item, Integer>> entries = products.entrySet();
					Iterator<Map.Entry<Item, Integer>> itr = entries.iterator();
					Entry<Item, Integer> entry = null;
					while (itr.hasNext()) 
					{
						entry = itr.next();
						this.client.getProposals().put(entry.getKey(), entry.getValue());
					}
					
					System.out.println("[Client " + this.client.getId() +"] [MSG RECEIVE; Products received from " + "Store_"+this.client.getStores_To_Contact().get(i).getStore_id() + "]");
					
					
					counter++;

				} catch (UnreadableException e) {
					e.printStackTrace();
				}



			}
		}


	}


	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return (this.client.getStores_To_Contact().size() == counter);
	}

}
