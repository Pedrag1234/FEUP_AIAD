package AgentBehaviours;


import java.util.Hashtable;

import amazon.Item;
import amazon.Store;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import sajas.core.behaviours.Behaviour;
import sajas.core.behaviours.CyclicBehaviour;
import sajas.domain.DFService;

public class D_STORE_CLIENT_SEND_CONFIRMATION extends Behaviour{
	
	private Store store;
	private Boolean complete = false;

	public D_STORE_CLIENT_SEND_CONFIRMATION(Store s){
		this.store = s;
	}

	@Override
	public void action() {
		
		MessageTemplate test = MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL), MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL));
		
		ACLMessage res = this.store.receive(test);
		
		if(res != null) {
			
			String clientId = res.getContent();
			
			DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			
			sd.setType(clientId);
			dfd.addServices(sd);
			
			System.out.println("[Store " + this.store.getStore_id() + "] [Received Message]");
			
			try {
				DFAgentDescription[] result = DFService.search(this.store, dfd);

			//	System.out.println(result.length);

				for (int n = 0; n < result.length; n++) {
					
					AID dest = result[n].getName();
					
					switch (res.getPerformative()) {
					
					case ACLMessage.ACCEPT_PROPOSAL: {
						
						ACLMessage msgReply = new ACLMessage(ACLMessage.INFORM);
						msgReply.setContent("PurchaseComplete");
						msgReply.addReceiver(dest);
						
						Hashtable<String, Integer> requests = (Hashtable<String, Integer>)res.getContentObject();
						
						this.store.send(msgReply);
						
						System.out.println("[Store " + this.store.getStore_id() + "] [Confirmed purchase from  " + clientId + "]" );
						
						
						for (String i : requests.keySet()) {
							  for(Item j : this.store.getStoreWarehouseStock().keySet()) {
								  if(j.getType() == i) {
									  this.store.setProfit(this.store.getProfit() + j.getCurrentPrice());
								  }
							  }
						}
						
						System.out.println("[Store " + this.store.getStore_id() + " " + this.store.getStrategy1() +  "] [Current profit of store is  " + this.store.getProfit() + "$]" );
						break;
					}
					
					case ACLMessage.REJECT_PROPOSAL: {
						
						ACLMessage msgReply = new ACLMessage(ACLMessage.REFUSE);
						msgReply.addReceiver(dest);
						msgReply.setContent("PurchaseComplete");
						this.store.send(msgReply);
						System.out.println("[Store " + this.store.getStore_id() + "] [Denied purchase from  " + clientId + "]" );
						
						break;
					}
					
					default:
						ACLMessage msgReply = new ACLMessage(ACLMessage.FAILURE);
						msgReply.addReceiver(dest);
						msgReply.setContent("PurchaseComplete");
						this.store.send(msgReply);
						System.out.println("[Store " + this.store.getStore_id() + "] [Failure purchase from  " + clientId + "]" );
					}
				
				}
				
				

			} catch (FIPAException | UnreadableException e) {
				e.printStackTrace();
			}
			this.complete = true;
			
		}
		
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return this.complete;
	}

}
