

import amazon.MainWarehouse;
import amazon.Item;
import amazon.Store;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;



import java.util.HashMap;

import amazon.Client;
import amazon.Store;


public class Amazon {
	private ArrayList<Client> clients = new ArrayList<Client>();
	private ArrayList<Store> stores = new ArrayList<Store>();
	private MainWarehouse mainWarehouse;
	
	
	public Amazon() {
		get_clients();
		get_stores();
		
	/*	for (Client i : clients) {
			System.out.println(i.toString());
		}
		
		for (Store i : stores) {
			System.out.println(i.toString());
		}*/
		
	}
	
	
	public static void main(String[] args) {
		
		Amazon amazon = new Amazon();
			
		/*Runtime rt = Runtime.instance();

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
		} */
		
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
		
		/*Item item = new Item("Smartphone",599.99,599.99);
		Store store = new Store("Porto");
		
		HashMap<String, Double> needs = new HashMap<String, Double>();
		needs.put("Smartphone", 0.7);
		
		Client client = new Client(01, "Porto", 1000, 0.5, 0.5, 0, needs);
		boolean result = client.decide_if_buy(item, store);
		System.out.println(result);*/
	}
	
	public int get_clients() {
		
		String csvFile = "./docs/Clients.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] client = line.split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)",-1);
                HashMap<String, Double> needs_temp = new HashMap<String, Double>();
                needs_temp.put(client[6].replace("\"", ""), Double.parseDouble(client[7]));
                
                Client temp = new Client(Integer.parseInt(client[0]),client[1],Integer.parseInt(client[2]),Double.parseDouble(client[3]),Double.parseDouble(client[4]),Double.parseDouble(client[5]),needs_temp);
                		
                clients.add(temp);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;    
	}
	
public int get_stores() {
		
		String csvFile = "./docs/Stores.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] store = line.split(cvsSplitBy);
                
                Store temp = new Store(Integer.parseInt(store[0]),Integer.parseInt(store[1]),Integer.parseInt(store[2]),Integer.parseInt(store[3]),store[4]);
                		
                stores.add(temp);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;    
	}
	

}
