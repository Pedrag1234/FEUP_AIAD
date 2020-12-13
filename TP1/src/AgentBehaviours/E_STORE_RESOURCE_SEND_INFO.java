package AgentBehaviours;

import java.io.IOException;
import java.util.Hashtable;

import amazon.Store;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import sajas.core.behaviours.Behaviour;

public class E_STORE_RESOURCE_SEND_INFO extends Behaviour{
	
	private Store st;
	
	public E_STORE_RESOURCE_SEND_INFO(Store st) {
		this.st = st;
	}

	@Override
	public void action() {
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.QUERY_IF);
		ACLMessage msg = this.st.receive(mt);
		
		if(msg != null) {
			
			AID senderID = msg.getSender();
			
			ACLMessage res = new ACLMessage(ACLMessage.INFORM);
			
			Hashtable<Integer, Float> MessageContents = new Hashtable<>();
			
			MessageContents.put(st.getStore_id(),(float) st.getProfit());
			
			DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			
			
			sd.setType("ResourceCollector");
			dfd.addServices(sd);
			
			res.addReceiver(senderID);
			try {
				res.setContentObject(MessageContents);
				this.st.send(res);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
