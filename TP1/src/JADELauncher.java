

import amazon.MainWarehouse;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import amazon.Item;

import java.util.HashMap;

import amazon.Client;
import amazon.Store;


public class JADELauncher {

	public static void main(String[] args) {
			
	/*	Runtime rt = Runtime.instance();

		Profile p1 = new ProfileImpl();
		ContainerController mainContainer = rt.createMainContainer(p1);
		
		Profile p2 = new ProfileImpl();
		ContainerController container = rt.createAgentContainer(p2);
		
		
		
		AgentController ac1;
		try {
			MainWarehouse m = new MainWarehouse();
			m.print();
			
			ac1 = mainContainer.acceptNewAgent("WareHouse", m);
			
			m.setup();
			
			ac1.start();
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
