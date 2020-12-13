package AgentBehaviours;

import java.util.Hashtable;

import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import model.ResourceCollector;
import sajas.core.behaviours.Behaviour;
import sajas.domain.DFService;

public class E_RESOURCE_STORE_GATHER_INFO extends Behaviour{
	
	private ResourceCollector rsc;
	private boolean complete = false;
	
	public E_RESOURCE_STORE_GATHER_INFO(ResourceCollector rsc) {
		this.rsc = rsc;
	}

	@Override
	public void action() {
		
		for (int i = 0; i < this.rsc.getStoreIds().size(); i++) {
			String ClientType = "Client_" + i;
			
			ACLMessage res = new ACLMessage(ACLMessage.QUERY_IF);
			
			DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			
			
			sd.setType(ClientType);
			dfd.addServices(sd);
			
			try {
				DFAgentDescription[] result = DFService.search(this.rsc, dfd);
				
				for (int j = 0; j < result.length; j++) {
					
					AID dest = result[j].getName();
					res.addReceiver(dest);
					
					
					this.rsc.send(res);
	
				}
			} catch (FIPAException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		this.complete = true;
		
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return this.complete;
	}

}
