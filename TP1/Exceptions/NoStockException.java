package amazon;

public class NoStockException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoStockException() {
		super("Not enough stock.");
	}
}
