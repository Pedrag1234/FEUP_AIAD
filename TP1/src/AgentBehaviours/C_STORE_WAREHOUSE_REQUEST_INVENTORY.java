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




public class C_STORE_WAREHOUSE_REQUEST_INVENTORY extends Behaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6984357981660843976L;


	private Store store;
	private boolean complete = false;


	public C_STORE_WAREHOUSE_REQUEST_INVENTORY(Store s){
		this.setStore(s);
	}


	public void action() {
		ACLMessage msg = new ACLMessage(ACLMessage.QUERY_IF);
		Hashtable<Item,Integer> stock = new Hashtable<Item,Integer>();


		//msg.setContentObject(stock);
		msg.setContent(this.store.getStoreType());


		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();


		sd.setType("MainWarehouse");
		dfd.addServices(sd);


		try {
			DFAgentDescription[] result = DFService.search(this.store, dfd);

			//System.out.println(result.length);

			for (int i = 0; i < result.length; i++) {

				AID dest = result[i].getName();
				msg.addReceiver(dest);

				System.out.println("[Store " + this.store.getStore_id() + "]" + " [MSG SENT; Requested Inventory]");

				this.store.send(msg);
				this.complete = true;

			}

		} catch (FIPAException e) {
			e.printStackTrace();
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
