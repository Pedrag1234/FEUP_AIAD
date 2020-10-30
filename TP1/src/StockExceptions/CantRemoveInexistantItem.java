package StockExceptions;
public class CantRemoveInexistantItem extends Exception {
	private static final long serialVersionUID = 1L;

	public CantRemoveInexistantItem() {
		super("Item doesn't exist");
	}
}
