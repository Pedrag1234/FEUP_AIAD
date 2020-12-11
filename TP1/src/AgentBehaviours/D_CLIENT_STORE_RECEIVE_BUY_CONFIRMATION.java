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

public class D_CLIENT_STORE_RECEIVE_BUY_CONFIRMATION extends SimpleBehaviour{


	private Client client;
	private boolean complete = false;
	private int counter = 0;


	public D_CLIENT_STORE_RECEIVE_BUY_CONFIRMATION(Client c){
		this.client = c;
	}

	@Override
	public void action() {
			
		HashMap<Item,Integer> buy_list = this.client.getBuy_list();

		
		
		for (Item i : buy_list.keySet()) {
			
			
			//RECEBER MENSAGEM
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);			
			ACLMessage msgReply;
			
				msgReply = this.client.receive(mt);
				if(msgReply != null)
				{

					//receives stock info
					if(msgReply.getContent() == "PurchaseComplete") {
						System.out.println("[Client " + this.client.getId() +"] PURCHASE COMPLETE");
						this.client.setMoney_spent(this.client.getMoney_spent() + i.getPrice());
						counter++;
					}
				}
			
			
			
		}
		


	}

	@Override
	public boolean done() {
		return (this.client.getBuy_list().size() == counter);
	}

}
