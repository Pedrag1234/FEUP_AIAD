package AgentBehaviours;

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
		Hashtable<String,Integer> stock = new Hashtable<>();
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.QUERY_IF);
		ACLMessage msg = this.warehouse.receive(mt);

		if (msg != null) {

			System.out.println("MSG RECEIVED Nigggggggggggg");			
			//System.out.println(msg.getContent());
			//stock = (Hashtable<String,Integer>)msg.getContentObject();
			//REPLY:
			ACLMessage msgReply = new ACLMessage(ACLMessage.INFORM);
			DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("Store");
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
