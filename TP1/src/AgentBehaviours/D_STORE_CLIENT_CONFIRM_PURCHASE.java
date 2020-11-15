package AgentBehaviours;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Stack;

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
import jade.lang.acl.UnreadableException;

public class D_STORE_CLIENT_CONFIRM_PURCHASE extends CyclicBehaviour{
	

	private Store store;
	private boolean complete = false;


	public D_STORE_CLIENT_CONFIRM_PURCHASE(Store s){
		this.store = s;
	}

	@Override
	public void action() {
		//RECEIVE BUY ORDER
		
		//System.out.println("AAA " + this.store.getStore_id());
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
		ACLMessage msg = this.store.receive(mt);
		
		
		
		if (msg != null) 
		{
			AID senderID = msg.getSender();
			System.out.println("[Store " + this.store.getStore_id() + "] [Received purchase request from  " + msg.getSender().getLocalName() + "]" );
			Item receivedItem; 
			try {
				receivedItem = (Item) msg.getContentObject();
				this.store.addItemToCurrItemOrder(receivedItem); //nothing to see here
				
			} catch (UnreadableException e) {
				e.printStackTrace();
			}
			
			
			ACLMessage confirmation = new ACLMessage(ACLMessage.REQUEST);
			
			Hashtable<String, Integer> MessageContents = new Hashtable<>();
			
			
			try {
				Stack<Item> i = this.store.getCurrItemOrder();
				
				while(i.size() > 0) {
					
					String temp = i.pop().getType();
					Integer temp1 = 1;
					
					MessageContents.put(temp,temp1);
					
				}
				
				
				confirmation.setContentObject(MessageContents);
				
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
					
					System.out.println("MSG SENT; REMOVE ITEM FROM WAREHOUSE");
					
					this.store.send(msg);
					
					//System.out.println("ending storereqitem2warehouse");
					this.complete = true;
					
				}
				
				ACLMessage res = this.store.blockingReceive();
				
				switch (res.getPerformative()) {
				
				case ACLMessage.ACCEPT_PROPOSAL: {
					
					ACLMessage msgReply = new ACLMessage(ACLMessage.INFORM);
					msgReply.addReceiver(senderID);
					this.store.send(msgReply);
					msgReply.setContent("PurchaseComplete");
					System.out.println("[Store " + this.store.getStore_id() + "] [Confirmed purchase from  " + msg.getSender().getLocalName() + "]" );
					
					break;
				}
				case ACLMessage.REJECT_PROPOSAL: {
					
					ACLMessage msgReply = new ACLMessage(ACLMessage.REFUSE);
					msgReply.addReceiver(senderID);
					this.store.send(msgReply);
					msgReply.setContent("PurchaseComplete");
					System.out.println("[Store " + this.store.getStore_id() + "] [Denied purchase from  " + msg.getSender().getLocalName() + "]" );
					
					break;
				}
				default:
					ACLMessage msgReply = new ACLMessage(ACLMessage.FAILURE);
					msgReply.addReceiver(senderID);
					this.store.send(msgReply);
					msgReply.setContent("PurchaseComplete");
					System.out.println("[Store " + this.store.getStore_id() + "] [Failure purchase from  " + msg.getSender().getLocalName() + "]" );
				}
				
				
			
			} catch (FIPAException e) {
				e.printStackTrace();
			}
			

			
			this.complete = true;
		
		}
		
	}


}
