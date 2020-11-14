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
import jade.lang.acl.MessageTemplate;

public class A_STORE_CLIENT_PRESENT_PRODUCT_OFFER extends SimpleBehaviour{

private static final long serialVersionUID = 2123129761660843976L;
	
	private Store store;
	private boolean complete = false;
	
	
	public A_STORE_CLIENT_PRESENT_PRODUCT_OFFER(Store s){
		this.store = s;
	}
	
	@Override
	public void action() {
		
		MessageTemplate request = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		ACLMessage m = this.store.blockingReceive(request);
		
		if(m != null) {
			
			ACLMessage msg = new ACLMessage(ACLMessage.AGREE);
			
			ArrayList<Item> MessageContents = store.generateProducts2Offer();
			
			
			try {
				
				
				msg.setContentObject(MessageContents);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			
			
			sd.setType("Client_"+this.store.getCurrClientId());
			dfd.addServices(sd);
			
			
			try {
				DFAgentDescription[] result = DFService.search(this.store, dfd);
				
				System.out.println(result.length);
				
				for (int i = 0; i < result.length; i++) {
					
					AID dest = result[i].getName();
					msg.addReceiver(dest);
					
					System.out.println(dest.getName());
					
					this.complete = true;
					this.store.send(msg);
				}
			
			} catch (FIPAException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return this.complete;
	}
	
}
