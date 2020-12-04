package AgentBehaviours;

import amazon.Store;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import amazon.Client;
import amazon.Item;
import jade.core.AID;
import sajas.core.behaviours.SimpleBehaviour;
import sajas.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class D_CLIENT_STORE_BUY_ITEM extends SimpleBehaviour{


	private Client client;
	private boolean complete = false;


	public D_CLIENT_STORE_BUY_ITEM(Client c){
		this.client = c;
	}

	@Override
	public void action() {


		HashMap<Item,Integer> buy_list = this.client.getBuy_list();

		for (Item i : buy_list.keySet()) {
			ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
			try {
				msg.setContentObject(i);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			buy_list.get(i); //i = Item , get(i)=id loja

			DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();


			sd.setType("Store_" + buy_list.get(i) ); //ALTERAR ISTO
			dfd.addServices(sd);


			try {
				DFAgentDescription[] result = DFService.search(this.client, dfd);

				System.out.println(result.length);

				for (int z = 0; z < result.length; z++) {

					AID dest = result[z].getName();
					msg.addReceiver(dest);

					System.out.println(dest.getName());

					this.complete = true;
					this.client.send(msg);
				}

			} catch (FIPAException e) {
				e.printStackTrace();
			}

			//RECEBER MENSAGEM
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);			
			ACLMessage msgReply;
			msgReply = this.client.receive(mt);
			while(msgReply == null)
			{
				msgReply = this.client.receive(mt);
				if(msgReply != null)
				{

					//receives stock info
					if(msgReply.getContent() == "PurchaseComplete")
						System.out.println("PURCHASE COMPLETE");


				}
			}

			this.complete = true;


		}








	}

	@Override
	public boolean done() {
		return this.complete;
	}

}
