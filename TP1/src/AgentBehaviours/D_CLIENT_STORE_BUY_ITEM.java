package AgentBehaviours;

import amazon.Store;

import java.util.ArrayList;
import java.util.HashMap;

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

		ArrayList<Item> buy_list = this.client.getBuy_list();
		HashMap<Item,Integer> tempHashMap = new HashMap<Item,Integer>();
		
		for(int i = 0; i < buy_list.size(); i++)
		{
			tempHashMap = buy_list.get(i);
		}

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
