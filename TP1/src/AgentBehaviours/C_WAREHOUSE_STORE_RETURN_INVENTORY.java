package AgentBehaviours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import amazon.Item;
import amazon.MainWarehouse;
import jade.core.AID;
import sajas.core.behaviours.CyclicBehaviour;
import sajas.core.behaviours.SimpleBehaviour;
import sajas.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class C_WAREHOUSE_STORE_RETURN_INVENTORY extends SimpleBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6984357965980843976L;

	private MainWarehouse warehouse;
	private boolean complete;
	private int storeCounter = 0;

	public C_WAREHOUSE_STORE_RETURN_INVENTORY(MainWarehouse warehouse) {
		this.warehouse = warehouse;
		this.complete = false;
	}

	@Override
	public void action() {
		//RECEIVE MSG
		
		String StoreType;
		Hashtable<Item,Integer> stock = new Hashtable<Item,Integer>();

		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.QUERY_IF);
		ACLMessage msg = this.warehouse.receive(mt);

		if (msg != null) {

			System.out.println("[Warehouse] [MSG RECEIVED; Giving back inventory info now]");            
            //System.out.println(msg.getContent());
            //stock = (Hashtable<String,Integer>)msg.getContentObject();
            
            //REPLY:
            StoreType = msg.getContent();
            ACLMessage msgReply = new ACLMessage(ACLMessage.INFORM);
            try {
                //Fill MSG with inventory information to return
                stock = this.warehouse.getStock();
                msgReply.setContentObject(stock);
                
                //System.out.println("FILLED MSG WITH STOCK INFO"); //, size:");
                //System.out.println(stock.size());
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        
            //SEND REPLY
            AID senderID = msg.getSender();
            msgReply.addReceiver(senderID);

            this.warehouse.send(msgReply);
            storeCounter++;


		}
		else {
			//block();
		}

		return;
	}

	@Override
	public boolean done() {
		if(storeCounter == this.warehouse.getNumberOfStores())
		{
			System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
			this.warehouse.doDelete();
		}
		
		return (storeCounter == this.warehouse.getNumberOfStores());
	}



}
