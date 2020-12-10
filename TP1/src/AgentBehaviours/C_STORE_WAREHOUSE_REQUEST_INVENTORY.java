package AgentBehaviours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

import amazon.Item;
import amazon.Store;
import jade.core.AID;
import sajas.core.behaviours.SimpleBehaviour;
import sajas.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;




public class C_STORE_WAREHOUSE_REQUEST_INVENTORY extends SimpleBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6984357981660843976L;


	private Store store;
	private boolean complete = false;


	public C_STORE_WAREHOUSE_REQUEST_INVENTORY(Store s){
		this.setStore(s);
	}


	@Override
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

			}

		} catch (FIPAException e) {
			e.printStackTrace();
		}

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
				this.store.setOffering(this.store.generateProducts2Offer());
				this.store.has_inventory = true;
				this.complete = true;
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
