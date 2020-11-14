package AgentBehaviours;

import amazon.Store;
import jade.core.behaviours.SimpleBehaviour;

public class D_STORE_CLIENT_CONFIRM_PURCHASE extends SimpleBehaviour{
	

	private Store store;
	private boolean complete = false;


	public D_STORE_CLIENT_CONFIRM_PURCHASE(Store s){
		this.store = s;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
