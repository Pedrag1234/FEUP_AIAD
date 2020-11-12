package AgentBehaviours;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import StockExceptions.ItemDoesntExist;
import StockExceptions.NoStockException;
import amazon.Item;
import amazon.MainWarehouse;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;


public class WarehouseHandleReq2RemItem extends SimpleBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7403896444357228044L;
	
	
	private MainWarehouse warehouse;
	private boolean complete;
	
	public WarehouseHandleReq2RemItem(MainWarehouse warehouse) {
		this.warehouse = warehouse;
		this.complete = false;
	}

	@Override
	public void action() {
		ACLMessage msg = this.warehouse.blockingReceive();
		
		if (msg != null) {
			try {
				Hashtable<Item, Integer> requests = (Hashtable<Item, Integer>)msg.getContentObject();
				
				Set<Map.Entry<Item, Integer>> entries = requests.entrySet();
				Iterator<Map.Entry<Item, Integer>> itr = entries.iterator();
				
				Map.Entry<Item, Integer> entry = null;
				
				while (itr.hasNext()) {
					
					entry = itr.next();
					try {
						this.warehouse.removeItemFromStock(entry.getKey(), entry.getValue());;
					} catch (NoStockException | ItemDoesntExist e) {
						e.printStackTrace();
					}
				}

			} catch (UnreadableException e) {
				e.printStackTrace();
			}
			this.complete = true;
		}
		else {
			block();
		}
		
		return;
	}

	@Override
	public boolean done() {
		return this.complete;
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
