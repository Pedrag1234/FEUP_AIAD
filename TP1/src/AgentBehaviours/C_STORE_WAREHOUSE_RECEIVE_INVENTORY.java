package AgentBehaviours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

import amazon.Item;
import amazon.Store;
import jade.core.AID;
import sajas.core.behaviours.Behaviour;
import sajas.core.behaviours.OneShotBehaviour;
import sajas.core.behaviours.SimpleBehaviour;
import sajas.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;




public class C_STORE_WAREHOUSE_RECEIVE_INVENTORY extends Behaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6984357981660843976L;


	private Store store;
	private boolean complete = false;


	public C_STORE_WAREHOUSE_RECEIVE_INVENTORY(Store s){
		this.setStore(s);
	}


	public void action() {
		
		Hashtable<Item,Integer> stock = new Hashtable<Item,Integer>();

		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		ACLMessage msgReply;
		msgReply = this.store.receive(mt);
		int i;
		
			
			if(msgReply != null)
			{
				
				System.out.println("[Store " + this.store.getStore_id() + "]" + " [MSG REPLY RECEIVED; GETTING STORE INFO NOW]");
				try {
					//receives stock info
					stock = (Hashtable<Item,Integer>)msgReply.getContentObject();
					//updates store's information about its warehouse stock
					this.store.setStoreWarehouseStock(stock);
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/*if(stock.size() != 0)
					{System.out.println("got stock size!:");
					System.out.println(stock.size());}*/
			
		
		
				//System.out.println("ending StoreRequestInventory");
				this.complete = true;
				this.store.setOffering(this.store.generateProducts2Offer());
				this.store.has_inventory = true;
				
			}
			
	}

	public boolean done() {
		// TODO Auto-generated method stub
		return this.complete;
	}

	public void setStore(Store store) {
		this.store = store;
	}

}
