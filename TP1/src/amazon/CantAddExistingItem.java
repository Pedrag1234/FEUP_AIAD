package amazon;

public class CantAddExistingItem extends Exception {
	private static final long serialVersionUID = 1L;

	public CantAddExistingItem() {
		super("Item already Exists");
	}
}
