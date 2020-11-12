package AgentBehaviours;

import amazon.Store;
import jade.core.behaviours.Behaviour;
import jade.domain.introspection.ACLMessage;

public class StoreReq2WarehouseBehaviour extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2123129761660843976L;
	
	private Store store;
	
	
	public StoreReq2WarehouseBehaviour(Store s){
		this.setStore(s);
	}

	@Override
	public void action() {

	}
	

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	

}
