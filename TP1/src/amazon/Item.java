package amazon;

import jade.util.leap.Serializable;

public class Item implements Serializable {
	
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (currentPrice == null) {
			if (other.currentPrice != null)
				return false;
		} else if (!currentPrice.equals(other.currentPrice))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currentPrice == null) ? 0 : currentPrice.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
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

	@Override
	public String toString() {
		return "Item [type=" + type + ", currentPrice=" + currentPrice + ", price=" + price + "]";
	}
	
	
	
}
