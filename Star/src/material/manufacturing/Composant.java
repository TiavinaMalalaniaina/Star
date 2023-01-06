package material.manufacturing;

import java.util.Vector;

import util.bdd.connection.ConnectionBase;
import util.bdd.connection.ConnectionOracle;
import util.bdd.generale.BddObject;

public class Composant {
//	FIELDS
	String idComposant;
	Vector<Composant> ingredients = new Vector<Composant>();
	boolean isPrimaire;
	double unitPrice;
	
//	CONSTRUCTOR
	public Composant(String idComposant, Vector<Composant> ingredients, boolean isPrimaire, double unitPrice) {
		setIdComposant(idComposant);
		setIngredients(ingredients);
		setIsPrimaire(isPrimaire);
		setUnitPrice(unitPrice);
	}
	
	public Composant(ConnectionBase cb, String idComposant) throws Exception {
		prepareComposant(cb, idComposant);
	}
	
	
//	GETTER
	public String getIdComposant() {
		return idComposant;
	}
	public Vector<Composant> getIngredients() {
		return ingredients;
	}
	public boolean getIsPrimaire() {
		return isPrimaire;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	
//	SETTER
	public void setIdComposant(String idComposant) {
		this.idComposant = idComposant;
	}
	public void setIngredients(Vector<Composant> ingredients) {
		this.ingredients = ingredients;
	}
	public void setIngredients(ConnectionBase cb, Vector<Ingredient> ingredients) throws Exception {
		for (Ingredient in : ingredients) {
			getIngredients().add(new Composant(cb, in.getIdProductUsed()));
		}
	}
	public void setIsPrimaire(boolean isPrimaire) {
		this.isPrimaire = isPrimaire;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	
	public void prepareComposant(ConnectionBase cb, String idComposant) throws Exception {
		Product p = new Product();
		Ingredient in = new Ingredient();
		boolean wasConnected = true;
		if(cb.get_co()==null) {
			cb.preparingConnection();
			wasConnected = false;
		}
		Vector<BddObject> productsObject = p.select(cb,"idProduct", idComposant);
//		setIdComposant(idComposant);
//		setIsPrimaire(true);
//		setUnitPrice((double)productsObject.get(0).get("unitPrice"));
	
		Product product = (Product)(productsObject.get(0));
		if (product.getIsPrimary()=="y") {
			setIdComposant(idComposant);
			setIsPrimaire(true);
			setUnitPrice(product.getUnitPrice());
		}else {
			Vector<BddObject> ingredientsObject = in.select(cb,"idProductFinished", idComposant);			
			Vector<Ingredient> ingredients = new Vector<Ingredient>();
			for (BddObject b : ingredientsObject) 	ingredients.add((Ingredient)b);
			setIdComposant(idComposant);
			setIsPrimaire(false);
			setIngredients(cb, ingredients);
			setUnitPrice(-1);	
		}
		
		
		if(!wasConnected) cb.closeConnection();
	}

	public static void main(String[] args) throws Exception {
		Composant c = new Composant(new ConnectionOracle("star","star"), "PRO0001");
		System.out.println(c.getIngredients().get(2).getIdComposant());
	}
}
