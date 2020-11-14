package AgentBehaviours;

import java.io.IOException;
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

public class STORE_CLIENT_PRESENT_PRODUCTS extends SimpleBehaviour{

private static final long serialVersionUID = 2123129761660843976L;
	
	private Store store;
	private boolean complete = false;
	
	
	public STORE_CLIENT_PRESENT_PRODUCTS(Store s){
		this.store = s;
	}
	
	@Override
	public void action() {
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		
		String MessageContents = "Yo Yo Yo";
		
		
		try {
			
			
			msg.setContentObject(MessageContents);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		
		
		sd.setType("Client_1");
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
	
	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return this.complete;
	}
	
}
