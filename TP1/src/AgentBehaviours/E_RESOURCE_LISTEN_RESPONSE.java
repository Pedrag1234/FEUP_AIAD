package AgentBehaviours;

import java.io.IOException;
import java.util.HashMap;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.wrapper.ControllerException;
import model.ResourceCollector;
import sajas.core.behaviours.Behaviour;
import sajas.core.behaviours.CyclicBehaviour;
import sajas.core.behaviours.SimpleBehaviour;

public class E_RESOURCE_LISTEN_RESPONSE extends SimpleBehaviour{
	
	private ResourceCollector rsc;
	private boolean complete = false;
	private int counter = 0;
	
	public E_RESOURCE_LISTEN_RESPONSE(ResourceCollector rsc) {
		this.rsc = rsc;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		MessageTemplate mt = MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchPerformative(ACLMessage.UNKNOWN));
		ACLMessage msg = this.rsc.receive(mt);
		
		
		if(msg != null) {
			
			switch (msg.getPerformative()) {
			
				case ACLMessage.INFORM: {
				
					try {
						HashMap<Integer, Float> msgContent = (HashMap<Integer, Float>) msg.getContentObject();
						
						System.out.println("[Resource Collector received info from store sale]");
						for(Integer i : msgContent.keySet()) {
							this.rsc.updateValues(i, msgContent.get(i));				
						}
						
						
						
					} catch (UnreadableException e) {
						e.printStackTrace();
					}
					break;
				}
				
				case ACLMessage.UNKNOWN: {
					
					counter++;
				}
			}	
		}
		
	}

	@Override
	public boolean done() {
		
		if(this.rsc.get_number_stores() == counter) {
			try {
				System.out.println("[Saving Data and Shutting Down]");
				this.rsc.getF().append("\n");
				this.rsc.getF().append("Total Profit = " + this.rsc.get_total_profit() + ".\n");
				this.rsc.getF().append("Average Profit = " + this.rsc.get_total_profit()/this.rsc.get_number_stores() + ".\n");
				
				for (int i = 0; i < this.rsc.get_number_stores(); i++) {
					this.rsc.getF().append("Store_" + i + " Profit = " + this.rsc.get_individual_profit(i) + ".\n");
				}
				this.rsc.getF().close();
				this.rsc.getContainerController().getPlatformController().kill();
			} catch (ControllerException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return (this.rsc.get_number_stores() == counter);
	}



}
