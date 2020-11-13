package AgentBehaviours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

import amazon.Item;
import amazon.Store;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;




public class StoreRequestInventoryBehaviour extends SimpleBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6984357981660843976L;


	private Store store;
	private boolean complete = false;


	public StoreRequestInventoryBehaviour(Store s){
		this.setStore(s);
	}


	@Override
	public void action() {
		ACLMessage msg = new ACLMessage(ACLMessage.QUERY_IF);
		try {


			//msg.setContentObject(stock);
			msg.setContentObject("pLS Gib stock");

		} catch (IOException e) {
			e.printStackTrace();
		}

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

				System.out.println("MSG SENT; Requested Inventory");
				this.complete = true;
				this.store.send(msg);

			}

		} catch (FIPAException e) {
			e.printStackTrace();
		}


	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return this.complete;
	}

	public void setStore(Store store) {
		this.store = store;
	}

}
