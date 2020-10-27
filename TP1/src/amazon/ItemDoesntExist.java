package amazon;

public class ItemDoesntExist extends Exception {
	private static final long serialVersionUID = 1L;

	public ItemDoesntExist() {
		super("Does not exist in the stock.");
	}
}
