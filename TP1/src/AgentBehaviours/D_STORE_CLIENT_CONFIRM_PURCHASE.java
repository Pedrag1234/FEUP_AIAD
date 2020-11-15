package AgentBehaviours;

import amazon.Item;
import amazon.Store;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class D_STORE_CLIENT_CONFIRM_PURCHASE extends SimpleBehaviour{
	

	private Store store;
	private boolean complete = false;


	public D_STORE_CLIENT_CONFIRM_PURCHASE(Store s){
		this.store = s;
	}

	@Override
	public void action() {
		//RECEIVE BUY ORDER
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
		ACLMessage msg = this.store.receive(mt);
		
		AID senderID = msg.getSender();
		if (msg != null) 
		{
			Item receivedItem; 
			try {
				receivedItem = (Item) msg.getContentObject();
				this.store.addItemToCurrItemOrder(receivedItem); //nothing to see here
				
			} catch (UnreadableException e) {
				e.printStackTrace();
			}
			
		}
		 //SEND REPLY
		ACLMessage msgReply = new ACLMessage(ACLMessage.INFORM);
		msgReply.addReceiver(senderID);
		this.store.send(msgReply);
		msgReply.setContent("PurchaseComplete");
		this.complete = true;
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return this.complete;
	}

}
