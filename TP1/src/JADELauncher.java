

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
import uchicago.src.sim.analysis.BinDataSource;
import uchicago.src.sim.analysis.OpenHistogram;
import uchicago.src.sim.analysis.OpenSequenceGraph;
import uchicago.src.sim.analysis.Sequence;
import uchicago.src.sim.engine.Schedule;
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
	
	public static final boolean SEPARATE_CONTAINERS = false;
	private ContainerController mainContainer;
	private ContainerController agentContainer;
	private ArrayList<Client> clients = new ArrayList<Client>();
	private ArrayList<Store> stores = new ArrayList<Store>();
	private ArrayList<Agent> store_agents = new ArrayList<Agent>();
	private MainWarehouse mainWarehouse;
	private ResourceCollector rsc;
	private OpenSequenceGraph plot;
	private OpenSequenceGraph plot2;
	private OpenSequenceGraph plot3;
	private Schedule schedule;
	private OpenHistogram bar;
	private int NumberOfClients = 50;
	private int NumberOfStores = 5;
	private int StoresPerClient = 2;
	
	public static void main(String[] args) {

		SimInit init = new SimInit();
		init.setNumRuns(1);
		init.loadModel(new JADELauncher(), null, false);
		
	}
	
	@Override
	protected void launchJADE() {
		
		Runtime rt = Runtime.instance();
		Profile p = new ProfileImpl(true);
		mainContainer = rt.createMainContainer(p);
		
		clients = new ArrayList<Client>();
		stores = new ArrayList<Store>();
		
		get_stores();
		
		get_clients();
		
		if(SEPARATE_CONTAINERS) {
			Profile p2 = new ProfileImpl();
			agentContainer = rt.createAgentContainer(p2);
		} else {
			agentContainer = mainContainer;
		}
		
		launchAgents();
		

	}
	
	private void launchAgents() {
		boolean condition = false;
		
		
		try {
			
			MainWarehouse m = new MainWarehouse(this.stores.size());
			AgentController ac3;
			ac3 = agentContainer.acceptNewAgent("WareHouse", m);
			ac3.start();
			System.out.println("[MainWarehouse] Created");
			
			start_stores(agentContainer);
			
			
			
			start_clients(agentContainer);	
			
			rsc = new ResourceCollector(this.stores.size());
			AgentController ac4;
			ac4 = mainContainer.acceptNewAgent("ResourceCollector", rsc);
			ac4.start();
			
		}catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
	@Override
	public String[] getInitParam() {
		return new String[] {"NumberOfClients", "NumberOfStores", "StoresPerClient"};
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Welcome to the Sales Simulation";
	}
	
	public void begin() {
		super.begin();
		buildDisplay();
		buildSchedule();
	}
	
	@Override
	public void setup() {
		super.setup();  // crucial!
		schedule = new Schedule();
		// property descriptors
		// ...
	}
	
	private void buildDisplay() {


		// graph
		if (plot != null) plot.dispose();
		
		plot = new OpenSequenceGraph("Total profit", this);
		plot.setYRange(0, 1000.0);
		plot.setXRange(0, 150.0);
		plot.setAxisTitles("time", "total profit");
        // plot number of different existing colors
		plot.addSequence("Total profit", new Sequence() {
			public double getSValue() {
				return rsc.get_total_profit();
			}
		});
		
		plot.addSequence("Average profit per store", new Sequence() {
			public double getSValue() {
				return rsc.get_total_profit()/(stores.size());
			}
		});
		

		plot.display();
		
		
		
		if (plot2 != null) plot2.dispose();
		plot2 = new OpenSequenceGraph("Profits of individual stores", this);
		plot2.setYRange(0, 1000.0);
		plot2.setXRange(0, 150.0);
		plot2.setAxisTitles("time", "Profit");
		// plot number of different existing colors
		for (int i = 0; i < this.stores.size(); i++) {
			String name = "Store_" + i + " [" + this.stores.get(i).getStrategy1() + "]";
			final int j = i;
			plot2.addSequence(name, new Sequence() {
				public double getSValue() {
					return rsc.get_individual_profit(j);
				}
			});
			
		}

		plot2.display();
		
		
		
		if (plot3 != null) plot3.dispose();
		plot3 = new OpenSequenceGraph("Items sold per store", this);
		plot3.setYRange(0, 30);
		plot3.setXRange(0, 150.0);
		plot3.setAxisTitles("time", "Profit");
		// plot number of different existing colors
		for (int i = 0; i < this.stores.size(); i++) {
			String name = "Store_" + i + " [" + this.stores.get(i).getStrategy1() + "]";
			final int j = i;
			plot3.addSequence(name, new Sequence() {
				public double getSValue() {
					return rsc.get_products_counter(j);
				}
			});
			
		}

		plot3.display();
				
		///////////////////////////////////
/*		bar = new OpenHistogram("Agent Wealth Distribution", 10, 0);

		bar.setYRange(-1, 500.0);
		
		BinDataSource source = new BinDataSource()  {
			public double getBinValue(Object o) {
				Store agent = (Store)o;
				return (agent.getProfit());
		}
		};

		bar.createHistogramItem("Wealth", rsc.getStoresResults(), source);
		bar.display();*/
	}
	
	private void buildSchedule() {
		getSchedule().scheduleActionAtInterval(0.1, plot, "step", Schedule.CONCURRENT);
		getSchedule().scheduleActionAtInterval(0.1, plot2, "step", Schedule.CONCURRENT);
		getSchedule().scheduleActionAtInterval(0.1, plot3, "step", Schedule.CONCURRENT);
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
		
		String csvFile = "./docs/Clients.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        int counter = 0;
        
        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null && counter <= this.NumberOfClients) {

                // use comma as separator
                String[] client = line.split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)",-1);
                HashMap<String, Double> needs_temp = new HashMap<String, Double>();
                needs_temp.put(client[6].replace("\"", ""), Double.parseDouble(client[7]));
                
                Client temp = new Client(Integer.parseInt(client[0]),client[1],Double.parseDouble(client[2]),Double.parseDouble(client[3]),Double.parseDouble(client[4]),Double.parseDouble(client[5]),needs_temp,stores,StoresPerClient);
                		
                clients.add(temp);
                counter++;

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
        int counter = 0;

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null && counter < this.NumberOfStores) {

                // use comma as separator
                String[] store = line.split(cvsSplitBy);
                
                Store temp = new Store(Integer.parseInt(store[0])-1,Integer.parseInt(store[1]),Integer.parseInt(store[2]),Integer.parseInt(store[3]),store[4]);
                			
                stores.add(temp);
                counter++;
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

	public int getNumberOfClients() {
		return NumberOfClients;
	}

	public void setNumberOfClients(int numberOfClients) {
		NumberOfClients = numberOfClients;
	}

	public int getNumberOfStores() {
		return NumberOfStores;
	}

	public void setNumberOfStores(int numberOfStores) {
		NumberOfStores = numberOfStores;
	}

	public int getStoresPerClient() {
		return StoresPerClient;
	}

	public void setStoresPerClient(int storesPerClient) {
		StoresPerClient = storesPerClient;
	}
	
	
	

}
