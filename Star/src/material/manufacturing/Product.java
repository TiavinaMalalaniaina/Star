package material.manufacturing;

import util.bdd.generale.BddObject;

public class Product extends BddObject {
//	FIELD
	String idProduct;
	String productName;
	String isPrimary;
	double unitPrice;
		
//	CONSTRUCTOR
	public Product() {
		super("product", "idProduct");
	}

//	GETTER
	public String getIdProduct() {
		return idProduct;
	}
	public String getProductName() {
		return productName;
	}
	public String getIsPrimary() {
		return isPrimary;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	
//	SETTER
	public void setIdProduct(String idProduct) {
		this.idProduct = idProduct;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public void setIsPrimary(String isPrimary) {
		this.isPrimary = isPrimary;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	
	public static void main(String[] args) {
		String tit = "Tiavina";
		System.out.println(tit.substring(0,1).toLowerCase());
	}
}
