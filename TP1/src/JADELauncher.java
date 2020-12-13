

import amazon.MainWarehouse;
import amazon.Item;
import amazon.Store;
import amazon.Client;
import jade.core.Profile;
import jade.core.ProfileImpl;
import sajas.core.Agent;
import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.AgentController;
import sajas.wrapper.ContainerController;
import uchicago.src.sim.engine.SimInit;
import jade.wrapper.StaleProxyException;
import model.ResourceCollector;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;



import java.util.HashMap;

import amazon.Client;
import amazon.Store;


public class JADELauncher extends Repast3Launcher{
	
	private ContainerController cc;
	private ArrayList<Client> clients = new ArrayList<Client>();
	private ArrayList<Store> stores = new ArrayList<Store>();
	private MainWarehouse mainWarehouse;
	
	
	public static void main(String[] args) {

		SimInit init = new SimInit();
		init.setNumRuns(1);
		init.loadModel(new JADELauncher(), null, false);
		
	}
	
	@Override
	protected void launchJADE() {
		
		Runtime rt = Runtime.instance();
		Profile p = new ProfileImpl(true);
		cc = rt.createMainContainer(p);
		
		get_stores();
		
		get_clients();
		
		launchAgents();
		

	}
	
	private void launchAgents() {
		boolean condition = false;
		
		try {
			
			MainWarehouse m = new MainWarehouse(this.stores.size());
			AgentController ac3;
			ac3 = cc.acceptNewAgent("WareHouse", m);
			ac3.start();
			System.out.println("[MainWarehouse] Created");
			
			start_stores(cc);
			
			
			
			start_clients(cc);	
			
			ResourceCollector rsc = new ResourceCollector(this.stores.size());
			AgentController ac4;
			ac4 = cc.acceptNewAgent("ResourceCollector", rsc);
			ac4.start();
			
		}catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
	@Override
	public String[] getInitParam() {
		return new String[0];
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Welcome to the Sales Simulation";
	}
	
	
	
	public void start_stores(ContainerController cc) throws StaleProxyException {
		
		for (Store i : this.stores) {
			AgentController ac2;
			ac2 = cc.acceptNewAgent("Store" + i.getStore_id(), i);
			System.out.println("[Store_" + i.getStore_id() + "] Created");
			ac2.start();
		
		}
		
	}
	
	public void start_clients(ContainerController cc) throws StaleProxyException {
		
		
		for (Client i : this.clients) {
			AgentController ac1;
			
			ac1 = cc.acceptNewAgent("Client" + i.getId(), i);
			System.out.println("[Client_" + i.getId() + "] Created");
			ac1.start();
		}
		
	}
	
	
	
	public int get_clients() {
		
		String csvFile = "./docs/Clients_50.csv";
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
                
                Client temp = new Client(Integer.parseInt(client[0]),client[1],Double.parseDouble(client[2]),Double.parseDouble(client[3]),Double.parseDouble(client[4]),Double.parseDouble(client[5]),needs_temp,stores);
                		
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
		
		String csvFile = "./docs/Stores_5.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] store = line.split(cvsSplitBy);
                
                Store temp = new Store(Integer.parseInt(store[0])-1,Integer.parseInt(store[1]),Integer.parseInt(store[2]),Integer.parseInt(store[3]),store[4]);
                		
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
