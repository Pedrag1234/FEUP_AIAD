package AgentBehaviours;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Stack;

import amazon.Item;
import amazon.Store;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;




public class StoreReqItem2WarehouseBehaviour extends SimpleBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2123129761660843976L;
	
	private Store store;
	private boolean complete = false;
	
	
	public StoreReqItem2WarehouseBehaviour(Store s){
		this.setStore(s);
	}

	@Override
	public void action() {
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		
		Hashtable<String, Integer> MessageContents = new Hashtable<>();
		
		System.out.println("It got here somehow: Store");
		
		try {
			Stack<Item> i = this.store.getCurrItemOrder();
			Stack<Integer> n = this.store.getCurrItemNumber();
			
			for (int j = 0; j < i.size() ; j++) {
				
				MessageContents.put(i.pop().getType(),n.pop());
				
			}
			
			msg.setContentObject("You are geh");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		
		
		sd.setType("MainWarehouse");
		dfd.addServices(sd);
		
		
		try {
			DFAgentDescription[] result = DFService.search(this.store, dfd);
			
			System.out.println(result.length);
			
			for (int i = 0; i < result.length; i++) {
				
				AID dest = result[i].getName();
				msg.addReceiver(dest);
				
				System.out.println(dest.getName());
				
				this.complete = true;
				this.store.send(msg);
			}
		
		} catch (FIPAException e) {
			e.printStackTrace();
		}

		
	}
	


	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return this.complete;
	}

	

}
