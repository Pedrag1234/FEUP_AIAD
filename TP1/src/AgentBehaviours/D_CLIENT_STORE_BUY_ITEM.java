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
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
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
	//	System.out.println(this.client.getProposals());
		System.out.println("[Client " + this.client.getId() +"] [Decidind which items to buy ]");
		this.client.decide_which_items_to_buy(this.client.getProposals());
		
		HashMap<Item,Integer> buy_list = this.client.getBuy_list();

		for (Item i : buy_list.keySet()) {
			ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
			try {
				msg.setContentObject(i);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		//	buy_list.get(i); //i = Item , get(i)=id loja

			DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();


			sd.setType("Store_" + buy_list.get(i) ); //ALTERAR ISTO
			dfd.addServices(sd);


			try {
				DFAgentDescription[] result = DFService.search(this.client, dfd);

				for (int z = 0; z < result.length; z++) {

					AID dest = result[z].getName();
					msg.addReceiver(dest);

					
					this.client.send(msg);
					System.out.println("[Client " + this.client.getId() +"] [MSG SEND; Want to purchase " + i.getType() + " from Store_"+ buy_list.get(i) + "]");
				//	this.complete = true;
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
					if(msgReply.getContent() == "PurchaseComplete") {
						System.out.println("PURCHASE COMPLETE");
						this.client.setMoney_spent(this.client.getMoney_spent() + i.getPrice());
					}
				}
			}
			
			
		}
		
		this.complete = true;







	}

	@Override
	public boolean done() {
		return this.complete;
	}

}
