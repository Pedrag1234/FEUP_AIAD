package AgentBehaviours;

import java.util.HashMap;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import model.ResourceCollector;
import sajas.core.behaviours.Behaviour;

public class E_RESOURCE_LISTEN_RESPONSE extends Behaviour{
	
	private ResourceCollector rsc;
	private boolean complete = false;
	
	public E_RESOURCE_LISTEN_RESPONSE(ResourceCollector rsc) {
		this.rsc = rsc;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		ACLMessage msg = this.rsc.receive(mt);
		
		
		if(msg != null) {
			
			try {
				HashMap<Integer, Float> msgContent = (HashMap<Integer, Float>) msg.getContentObject();
				
				
				for(Integer i : msgContent.keySet()) {
					this.rsc.updateValues(i, msgContent.get(i));				
				}
				
				this.complete = true;
				
				
			} catch (UnreadableException e) {
				e.printStackTrace();
			}
			
		}
		
	}

	@Override
	public boolean done() {

		return this.complete;
	}

}
