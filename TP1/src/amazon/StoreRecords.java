package amazon;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class StoreRecords {
	
	private Store[] stores;
	
	
	public StoreRecords(Store[] stores){
		this.setStores(stores);
	}
	
	
	public void saveStoreRecords() throws IOException {
		File f = new File("logs/StoreRecords.csv");
		FileWriter fw;
		
		if(f.exists()) {
			fw = new FileWriter(f,true);
		}
		else {
			f.createNewFile();
			fw = new FileWriter(f);
		}
		
		PrintWriter pw = new PrintWriter(fw);
		
		pw.printf("Id, Area, N Customers, Profit, Stock size value, Max Promo, Min Promo,\n");
		
		
		for (int i = 0; i < stores.length; i++) {
			pw.printf("%d,%s,%d,%f,%f,%f,%f,\n",
					stores[i].getStore_id(),
					stores[i].getArea(),
					stores[i].getN_customers(),
					stores[i].getProfit(),
					stores[i].getStock_sz_value(),
					stores[i].getMaxPromo(),
					stores[i].getMinPromo());
		}
		
		pw.close();
		
	}


	public Store[] getStores() {
		return stores;
	}


	public void setStores(Store[] stores) {
		this.stores = stores;
	}
	
	
	
}
