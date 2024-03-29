package AgentBehaviours;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Map.Entry;

import amazon.Item;
import amazon.Store;
import jade.core.AID;
import sajas.core.behaviours.Behaviour;
import sajas.core.behaviours.CyclicBehaviour;
import sajas.core.behaviours.SimpleBehaviour;
import sajas.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;





public class B_STORE_WAREHOUSE_REQUEST_REMOVE_ITEM extends SimpleBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2123129761660843976L;
	
	private Store store;
	private boolean complete = false;
	
	
	public B_STORE_WAREHOUSE_REQUEST_REMOVE_ITEM(Store s){
		this.setStore(s);
	}

	@Override
	public void action() {
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		
		Hashtable<String, Integer> MessageContents = new Hashtable<>();
		
		
		try {
			Stack<Item> i = this.store.getCurrItemOrder();
			Stack<Integer> n = this.store.getCurrItemNumber();
			
			while(i.size() > 0) {
				
				String temp = i.pop().getType();
				Integer temp1 = n.pop();
				
				MessageContents.put(temp,temp1);
				
			}
			
			
			msg.setContentObject(MessageContents);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		
		
		sd.setType("MainWarehouse");
		dfd.addServices(sd);
		
		
		try {
			DFAgentDescription[] result = DFService.search(this.store, dfd);
			
			
			for (int i = 0; i < result.length; i++) {
				
				AID dest = result[i].getName();
				msg.addReceiver(dest);
				
				System.out.println("[Store " + this.store.getStore_id() + "] [MSG SENT; Remove item from Warehouse]");
				
				this.store.send(msg);
				
				//System.out.println("ending storereqitem2warehouse");
				
			}
			
			
			
		
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		this.complete = true;


		
	}
	


	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	@Override
	public boolean done() {
		return this.complete;
	}

	

}
