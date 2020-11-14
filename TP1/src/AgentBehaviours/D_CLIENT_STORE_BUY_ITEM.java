package AgentBehaviours;

import amazon.Store;
import amazon.Client;
import amazon.Item;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class D_CLIENT_STORE_BUY_ITEM extends SimpleBehaviour{


	private Client client;
	private boolean complete = false;


	public D_CLIENT_STORE_BUY_ITEM(Client c){
		this.client = c;
	}

	@Override
	public void action() {
		ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);

		msg.setContent("Pls i buy"); //ALTERAR ISTO


		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();


		sd.setType("Store"); //ALTERAR ISTO
		dfd.addServices(sd);


		try {
			DFAgentDescription[] result = DFService.search(this.client, dfd);

			System.out.println(result.length);

			for (int i = 0; i < result.length; i++) {

				AID dest = result[i].getName();
				msg.addReceiver(dest);

				System.out.println(dest.getName());

				this.complete = true;
				this.client.send(msg);
			}

		} catch (FIPAException e) {
			e.printStackTrace();
		}


	}

	@Override
	public boolean done() {
		return this.complete;
	}

}
