package AgentBehaviours;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import StockExceptions.ItemDoesntExist;
import StockExceptions.NoStockException;
import amazon.Item;
import amazon.MainWarehouse;
import sajas.core.behaviours.CyclicBehaviour;
import sajas.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;


public class B_WAREHOUSE_STORE_REMOVED_ITEM extends CyclicBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7403896444357228044L;
	
	
	private MainWarehouse warehouse;
	private boolean complete;
	
	public B_WAREHOUSE_STORE_REMOVED_ITEM(MainWarehouse warehouse) {
		this.warehouse = warehouse;
		this.complete = false;
	}

	@Override
	public void action() {
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		ACLMessage msg = this.warehouse.receive(mt);
		
		if (msg != null) {
			try {
				Hashtable<String, Integer> requests = (Hashtable<String, Integer>)msg.getContentObject();
				
				System.out.println("MSG RECEIVED; DELETE ITEM");
				Set<Map.Entry<String, Integer>> entries = requests.entrySet();
				Iterator<Map.Entry<String, Integer>> itr = entries.iterator();
				
				Entry<String, Integer> entry = null;
				
				while (itr.hasNext()) {
					
					entry = itr.next();
					
					try {
						warehouse.removeItemFromStock(entry.getKey(), entry.getValue());
					} catch (NoStockException | ItemDoesntExist e) {
						e.printStackTrace();
					}
					
				}

			} catch (UnreadableException e) {
				e.printStackTrace();
			}
			this.complete = true;
			//System.out.println("ending warehousehandlereq2rem");
		}
		else {
			//block();
		}
		
		return;
	}



	public MainWarehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(MainWarehouse warehouse) {
		this.warehouse = warehouse;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

}
