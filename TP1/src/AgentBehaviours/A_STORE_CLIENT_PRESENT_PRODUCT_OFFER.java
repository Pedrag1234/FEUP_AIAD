package AgentBehaviours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import amazon.Client;
import amazon.Item;
import amazon.Store;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class A_STORE_CLIENT_PRESENT_PRODUCT_OFFER extends SimpleBehaviour{

private static final long serialVersionUID = 2123129761660843976L;
	
	private Store store;
	private boolean complete = false;
	long start; 
	
	
	public A_STORE_CLIENT_PRESENT_PRODUCT_OFFER(Store s){
		this.store = s;
	}
	
	 @Override
	    public void action() {
		 	
	        //RECEIVE MESSAGE REQUEST
	        MessageTemplate request = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
	        ACLMessage m = this.store.blockingReceive(request);


	        if(m != null) {
	        	
	            ACLMessage msg = new ACLMessage(ACLMessage.AGREE);

	            ArrayList<Item> items = store.getOffering();

	            HashMap<Item,Integer> MessageContents = new HashMap<>();

	            for (int i = 0; i < items.size(); i++) {
	                MessageContents.put(items.get(i), this.store.getStore_id());
	            }

	            try {


	                msg.setContentObject(MessageContents);

	            } catch (IOException e) {
	                e.printStackTrace();
	            }

	            //REPLY MESSAGE

	            msg.addReceiver(m.getSender());
	           
	            this.store.send(msg);
	            System.out.println("[Store " + this.store.getStore_id() + "] [Sending Products to " + m.getSender().getLocalName() + "]" );
	            
	       
	            //this.complete = true;
	        }

	    }

	@Override
	public boolean done() {

		for(Client i : this.store.getClients()) {
			HashMap<Store,Integer> temp = i.getHas_received_products();
			
			try {
				TimeUnit.MILLISECONDS.sleep(500);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			
			for (Store j : temp.keySet()) {
				if(temp.get(j) == 0 && temp.get(j) != null) {
					return false;
				}
			}
		}
		return true;
	}
	

	
}
