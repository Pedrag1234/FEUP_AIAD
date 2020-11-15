

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
		
		get_stores();
		get_clients();
		
		start_stores();
		
	/*	for (Client i : clients) {
			System.out.println(i.toString());
		}
		
		for (Store i : stores) {
			System.out.println(i.toString());
		}*/
		
	}
	
	public void start_stores() {
		
		
			
	//		ac2 = mainContainer.acceptNewAgent("Store", store);
		
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
                
                Client temp = new Client(Integer.parseInt(client[0]),client[1],Integer.parseInt(client[2]),Double.parseDouble(client[3]),Double.parseDouble(client[4]),Double.parseDouble(client[5]),needs_temp,stores);
                		
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
