package AgentBehaviours;

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
import jade.lang.acl.UnreadableException;

public class ClientReceiveProductOffer extends SimpleBehaviour{
	
	private Client client;
	private boolean complete;
	
	public ClientReceiveProductOffer(Client client) {
		this.client = client;
		this.complete = false;
	}
	
	@Override
	public void action() {
		ACLMessage msg = this.client.receive();
		
		if (msg != null) {
			try {
				String requests = (String)msg.getContentObject();
				System.out.println(requests);
				
			} catch (UnreadableException e) {
				e.printStackTrace();
			}
		
				

			
			this.complete = true;
		}
		else {
			//block();
		}
		
		return;
	}
	
	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return this.complete;
	}

}
