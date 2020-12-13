package AgentBehaviours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

import amazon.Item;
import amazon.Store;
import jade.core.AID;
import sajas.core.behaviours.CyclicBehaviour;
import sajas.core.behaviours.SimpleBehaviour;
import sajas.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class D_STORE_CLIENT_CONFIRM_PURCHASE extends CyclicBehaviour{
	

	private Store store;
	private boolean complete = false;
	
	private Item items_sent;


	public D_STORE_CLIENT_CONFIRM_PURCHASE(Store s){
		this.store = s;
		this.items_sent =null;
	}

	@Override
	public void action() {
		//RECEIVE BUY ORDER
		
	//	System.out.println("AAA " + this.store.getStore_id());
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
		ACLMessage msg = this.store.receive(mt);
		
		
		if (msg != null) 
		{
			AID senderID = msg.getSender();
			System.out.println("[Store " + this.store.getStore_id() + "] [Received purchase request from  " + msg.getSender().getLocalName() + "]" );
			Item receivedItem; 
			try {
				receivedItem = (Item) msg.getContentObject();
				this.items_sent =  receivedItem;
				this.store.addItemToCurrItemOrder(receivedItem); //nothing to see here
				
			} catch (UnreadableException e) {
				e.printStackTrace();
			}
			
			
			//add profits here instead of on confirmation
			System.out.println("[Store " + this.store.getStore_id() + "] [Confirmed purchase from  " + msg.getSender().getLocalName() + "]" );
			this.store.setProfit(this.store.getProfit() + this.items_sent.getCurrentPrice());
			this.store.setSales(this.store.getSales()+1);
			System.out.println("[Store " + this.store.getStore_id() + " " + this.store.getStrategy1() +  "] [Current profit of store is  " + this.store.getProfit() + "$] [Items sold: "+ this.store.getSales()+"]" );

			
			
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
					confirmation.addReceiver(dest);
					
					System.out.println("[Store " + this.store.getStore_id() + "] [MSG SENT; Remove item from Warehouse]");
					
					this.store.send(confirmation);
					System.out.println("[Warehouse] [MSG RECEIVED; Delete Item]");
					
					//System.out.println("ending storereqitem2warehouse");
					
					
				}
				
				////////////////////////////////////////////////////////////
			
				/*
				MessageTemplate test = MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL), MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL));
			
				ACLMessage res = this.store.receive(test);
				
				if(res != null) {
				
					System.out.println("[Store " + this.store.getStore_id() + "] [Received Message]");
					
					switch (res.getPerformative()) {
					
					case ACLMessage.ACCEPT_PROPOSAL: {
						
						ACLMessage msgReply = new ACLMessage(ACLMessage.INFORM);
						msgReply.setContent("PurchaseComplete");
						msgReply.addReceiver(senderID);
						
						
						this.store.send(msgReply);
						
						//System.out.println("[Store " + this.store.getStore_id() + "] [Confirmed purchase from  " + msg.getSender().getLocalName() + "]" );
						//this.store.setProfit(this.store.getProfit() + this.items_sent.getCurrentPrice());
						//System.out.println("[Store " + this.store.getStore_id() + " " + this.store.getStrategy1() +  "] [Current profit of store is  " + this.store.getProfit() + "$]" );
						break;
					}
					
					case ACLMessage.REJECT_PROPOSAL: {
						
						ACLMessage msgReply = new ACLMessage(ACLMessage.REFUSE);
						msgReply.addReceiver(senderID);
						msgReply.setContent("PurchaseComplete");
						this.store.send(msgReply);
						//System.out.println("[Store " + this.store.getStore_id() + "] [Denied purchase from  " + msg.getSender().getLocalName() + "]" );
						
						break;
					}
					
					default:
						ACLMessage msgReply = new ACLMessage(ACLMessage.FAILURE);
						msgReply.addReceiver(senderID);
						msgReply.setContent("PurchaseComplete");
						this.store.send(msgReply);
						//System.out.println("[Store " + this.store.getStore_id() + "] [Failure purchase from  " + msg.getSender().getLocalName() + "]" );
					}
				
				}
			*/
			} catch (FIPAException e) {
				e.printStackTrace();
			}
			

			
			this.complete = true;
			
		}
		
	}


}
