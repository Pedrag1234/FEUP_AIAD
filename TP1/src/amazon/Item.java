package amazon;

public class Item {
	
	private String type;
	private Double currentPrice;
	private Double price;
	
	public Item(String type,Double price) {
		setType(type);
		setPrice(price);
		setCurrentPrice(price);
	}
	
	public Item(String type,Double price,Double currentPrice) {
		setType(type);
		setPrice(price);
		setCurrentPrice(currentPrice);
	}
	
	@Override
	public boolean equals(Object s) {
		if(((Item) s).getType() == type)
			return true;
		else
			return false;
		
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.type.hashCode();
	}
	
	@Override
	protected Item clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (Item) super.clone();
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public Double getPrice() {
		return price;
	}
	
	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(Double currentPrice) {
		this.currentPrice = currentPrice;
	}
	
	public void applyPromotion(int promotionValue) {
		setCurrentPrice(price * (promotionValue / 100));
	}
	
	public void endPromotion() {
		setCurrentPrice(price);
	}
	
}
