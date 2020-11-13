package amazon;

import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import AgentBehaviours.WarehouseHandleReq2RemItem;
import StockExceptions.*;

public class MainWarehouse extends Agent {

	private static final long serialVersionUID = 4363918672967765440L;
	
	private DFAgentDescription dfd;
	
	private static final String[] types = {
			"Pen",
			"Book",
			"Pencil",
			"Pencil Case",
			"Calculator",
			"Eraser",
			"Laptop",
			"TV",
			"PC",
			"Monitors",
			"Smartphone",
			"DishWasher",
			"Laundry Machine",
			"Kitchen Utensils"
	};
	
	private static final double[] prices = {
			5.99,
			10.99,
			4.99,
			7.50,
			129.90,
			2.59,
			700.00,
			500.48,
			1250.99,
			350.33,
			599.99,
			329.99,
			289.00,
			26.00,	
	};
	
	
	public void register() {
		ServiceDescription sd = new ServiceDescription();
		
		sd.setName(getLocalName());
		sd.setType("MainWarehouse");
		
		this.dfd = new DFAgentDescription();
		dfd.setName(getAID());
		dfd.addServices(sd);
		
		try {
			DFService.register(this, this.dfd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private WareHouse wares;
	
	public MainWarehouse(){
		wares = new WareHouse();
		
		this.generateWareHouseStock();
	}
	
	private void generateWareHouseStock() {
		
		for(int i = 0; i < types.length; i++) {
			try {
				wares.addNewItemStock(new Item(types[i],prices[i]),100000);
			} catch (CantAddExistingItem e) {
				// TODO: handle exception
			}
		}
		
	}
	
	public Integer getItemStock(Item s) throws ItemDoesntExist {
		return wares.getItemStock(s);
	}
	
	public void print() {
		this.wares.printStock();
		
	}
	
	public void removeItemFromStock(String itemName, int number) throws NoStockException, ItemDoesntExist {
		
		Set<Map.Entry<Item, Integer>> entries = this.wares.getStock().entrySet();
		Iterator<Map.Entry<Item, Integer>> itr = entries.iterator();
		
		Entry<Item, Integer> entry = null;
		
		while (itr.hasNext()) {
			
			entry = itr.next();
			
			if(entry.getKey().getType().equals(itemName)) {
				this.wares.removeStock(entry.getKey(), number);
			}
			
		}
		
		this.print();
	}
	
	public void setup() {
		this.register();
		SequentialBehaviour loop = new SequentialBehaviour();
		
		loop.addSubBehaviour(new WarehouseHandleReq2RemItem(this));
		
		addBehaviour(loop);
	}

	public WareHouse getWares() {
		return wares;
	}

	public void setWares(WareHouse wares) {
		this.wares = wares;
	}
}
