package material.manufacturing;

import util.bdd.generale.BddObject;

public class Ingredient extends BddObject {
//  FIELD
	String idIngredient;
	String idProductFinished;
	String idProductUsed;
	double quantity;
	
//	CONSTRUCTOR
	public Ingredient() {
		super("ingredient", "idIngredient");
	}
	public Ingredient(String idIngredient, String idProductFinished, String idProductUsed, double quantity) {
		super("ingredient", "idIngredient");
		setIdIngredient(idIngredient);
		setIdProductFinished(idProductFinished);
		setIdProductUsed(idProductUsed);
		setQuantity(quantity);
	}
	
//	GETTER
	public String getIdIngredient() {
		return idIngredient;
	}
	public String getIdProductFinished() {
		return idProductFinished;
	}
	public String getIdProductUsed() {
		return idProductUsed;
	}
	public double getQuantity() {
		return quantity;
	}
	
//	SETTER
	public void setIdIngredient(String idIngredient) {
		this.idIngredient = idIngredient;
	}
	public void setIdProductFinished(String idProductFinished) {
		this.idProductFinished = idProductFinished;
	}
	public void setIdProductUsed(String idProductUsed) {
		this.idProductUsed = idProductUsed;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	
}
