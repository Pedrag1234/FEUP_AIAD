package AgentBehaviours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import amazon.MainWarehouse;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class WarehouseReturnInventoryBehaviour extends SimpleBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6984357965980843976L;

	private MainWarehouse warehouse;
	private boolean complete;

	public WarehouseReturnInventoryBehaviour(MainWarehouse warehouse) {
		this.warehouse = warehouse;
		this.complete = false;
	}

	@Override
	public void action() {
		//RECEIVE MSG
		String StoreType;
		Hashtable<String,Integer> stock = new Hashtable<>();
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.QUERY_IF);
		ACLMessage msg = this.warehouse.receive(mt);

		if (msg != null) {

			System.out.println("MSG RECEIVED; GIVE BACK INVENTORY INFO");			
			//System.out.println(msg.getContent());
			//stock = (Hashtable<String,Integer>)msg.getContentObject();
			//REPLY:
			StoreType = msg.getContent();
			ACLMessage msgReply = new ACLMessage(ACLMessage.INFORM);
			try {
				//Fill MSG with inventory information to return
				stock = this.warehouse.getStock();
				msgReply.setContentObject(stock);
				//System.out.println("FILLED MSG WITH STOCK INFO"); //, size:");
				//System.out.println(stock.size());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType(StoreType);
			dfd.addServices(sd);

			//try?
			try {
				DFAgentDescription[] result = DFService.search(this.warehouse, dfd);

				for (int i = 0; i < result.length; i++) {

					AID dest = result[i].getName();
					msgReply.addReceiver(dest);

					System.out.println("REPLY MSG SENT; CURRENT INVENTORY IN WAREHOUSE");

					this.warehouse.send(msgReply);
				}
			} catch (FIPAException e) {
				e.printStackTrace();
			}

			//System.out.println("ending WarehouseReturnInventory");
			this.complete = true;

		}
		else {
			//block();
		}

		return;
	}

	@Override
	public boolean done() {
		return this.complete;
	}

}
