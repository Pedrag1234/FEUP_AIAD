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

public class A_CLIENT_STORE_REQUEST_PRODUCTS_OFFER extends SimpleBehaviour{

	private Client client;
	private boolean complete;

	public A_CLIENT_STORE_REQUEST_PRODUCTS_OFFER(Client client) {
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

			//	System.out.println(result.length);

				for (int n = 0; n < result.length; n++) {

					AID dest = result[n].getName();
					request.addReceiver(dest);

				//	System.out.println(dest.getName());

					this.complete = true;
					this.client.send(request);
					System.out.println("[Client " + this.client.getId() +"] [MSG SEND; Sending request for products to " + "Store_"+this.client.getStores_To_Contact().get(i).getStore_id() + "]");     
					
				}

			} catch (FIPAException e) {
				e.printStackTrace();
			}

			
		}
		
		this.complete=true;

	}


	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return this.complete;
	}

}
