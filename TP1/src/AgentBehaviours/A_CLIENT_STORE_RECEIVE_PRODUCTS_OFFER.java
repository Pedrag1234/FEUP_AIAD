package AgentBehaviours;

import java.util.ArrayList;
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
import jade.core.behaviours.SimpleBehaviour;
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
		
		MessageTemplate mt =  MessageTemplate.MatchPerformative(ACLMessage.AGREE);
		ACLMessage msg = this.client.blockingReceive(mt);
		
		if(msg != null) {
			
			try {
				ArrayList<Item> products = (ArrayList<Item>) msg.getContentObject();
				
				this.client.addProposedItems(products);
				this.complete = true;
				
			} catch (UnreadableException e) {
				e.printStackTrace();
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
