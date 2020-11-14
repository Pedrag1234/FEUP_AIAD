


import amazon.Item;
import amazon.MainWarehouse;
import amazon.Store;
import amazon.Client;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import java.util.HashMap;


public class JADELauncher {
	
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
	
	private Runtime rt;
	private Profile p;
	private ContainerController cc;

	public static void main(String[] args) throws StaleProxyException {
		
		this.rt = Runtime.instance();
		this.p = new ProfileImpl(true);
		this.cc = rt.createMainContainer(p);
		
		Amazon amazon = new Amazon();
		
		
		
		
	/*	Runtime rt = Runtime.instance();

		Profile p1 = new ProfileImpl();
		ContainerController mainContainer = rt.createMainContainer(p1);
		
		
		MainWarehouse m = new MainWarehouse();
		Store store = new Store(0, 20, 10, 2,"TugaLand");
		
		HashMap<String, Double> needs = new HashMap<String, Double>();
		needs.put("Smartphone", 0.7);
		
		Client client = new Client(01, "Porto", 1000, 0.5, 0.5, 0, needs);
		
		store.setOrder(1);
		
		for (int i = 0; i < 3; i++) {	
			System.out.println("Type = " + types[i]);
			store.getCurrItemOrder().push(new Item(types[i], prices[i]));
			store.getCurrItemNumber().push((int) (Math.random() * 1000));
		}
		
		
		AgentController ac1;
		AgentController ac2;
		AgentController ac3;
		try {
			
			m.print();
			
			ac1 = mainContainer.acceptNewAgent("WareHouse", m);
			ac2 = mainContainer.acceptNewAgent("Store", store);
			ac3 = mainContainer.acceptNewAgent("Client", client);
			
			System.out.println("-----------------------------------------------");
			
			
			
			ac1.start();
			ac2.start();
			ac3.start();
			
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}*/
		
		
		/*Object[] agentArgs = new Object[0];
		AgentController ac2;
		try {
			ac2 = container.createNewAgent("name2", "jade.core.Agent", agentArgs);
			ac2.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}

		AgentController ac3;
		try {
			ac3 = mainContainer.acceptNewAgent("myRMA", new jade.tools.rma.rma());
			ac3.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}*/
	}

}
