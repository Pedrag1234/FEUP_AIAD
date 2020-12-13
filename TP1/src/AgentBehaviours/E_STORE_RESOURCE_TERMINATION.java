package AgentBehaviours;

import java.io.IOException;
import java.util.HashMap;

import amazon.Store;
import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import sajas.core.behaviours.SimpleBehaviour;
import sajas.domain.DFService;

public class E_STORE_RESOURCE_TERMINATION extends SimpleBehaviour{
	
	private Store st;
	private boolean complete = false;
	
	public E_STORE_RESOURCE_TERMINATION(Store st) {
		this.st = st;
	}

	@Override
	public void action() {
		
		ACLMessage res = new ACLMessage(ACLMessage.UNKNOWN);
		
	
		
		DFAgentDescription dfd1 = new DFAgentDescription();
		ServiceDescription sd1 = new ServiceDescription();
		
		
		sd1.setType("ResourceCollector");
		dfd1.addServices(sd1);
		
		try {
			DFAgentDescription[] result1 = DFService.search(this.st, dfd1);
			
			
			for (int i = 0; i < result1.length; i++) {
				
				AID dest = result1[i].getName();
				res.addReceiver(dest);
			

				this.st.send(res);
				
			}
			
			
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
		this.complete = true;
		
	}

	@Override
	public boolean done() {
		return this.complete;
	}

}
