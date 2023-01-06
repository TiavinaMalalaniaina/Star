package material.manufacturing;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Vector;

import util.bdd.connection.ConnectionBase;
import util.bdd.connection.ConnectionOracle;
import util.bdd.generale.BddObject;
import util.generale.BaseObject;

public class Composant extends BaseObject{
//	FIELDS
	String idComposant;
	String productName;
	Vector<Composant> ingredients = new Vector<Composant>();
	boolean isPrimaire;
	double unitPrice;
	
//	CONSTRUCTOR
	public Composant(String idComposant, String productName, Vector<Composant> ingredients, boolean isPrimaire, double unitPrice) {
		setIdComposant(idComposant);
		setProductName(productName);
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
	public String getProductName() {
		return productName;
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
	public void setProductName(String productName) {
		this.productName = productName;
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
	
//	Find all ingredient and their derived of the composant
	public void prepareComposant(ConnectionBase cb, String idComposant) throws Exception {
		Product p = new Product();
		Ingredient in = new Ingredient();
		boolean wasConnected = true;
		if(cb.get_co()==null) {
			cb.preparingConnection();
			wasConnected = false;
		}
		Vector<BddObject> productsObject = p.select(cb,"idProduct", idComposant);
		Product product = (Product)(productsObject.get(0));
		setIdComposant(idComposant);
		setProductName(product.getProductName());
		if (product.getIsPrimary().equals("y")) {
			setIsPrimaire(true);
			setUnitPrice(product.getUnitPrice());
		}else {
			Vector<BddObject> ingredientsObject = in.select(cb,"idProductFinished", idComposant);			
			Vector<Ingredient> ingredients = new Vector<Ingredient>();
			for (BddObject b : ingredientsObject) 	ingredients.add((Ingredient)b);
			setIsPrimaire(false);
			setIngredients(cb, ingredients);
			setUnitPrice(-1);	
		}
		if(!wasConnected) cb.closeConnection();
	}
	
	public Vector<Composant> decompose(){
		Vector<Composant> composants = new Vector<Composant>();
		for(Composant ingredient : getIngredients() ) {
			if(ingredient.getIsPrimaire()) composants.add(ingredient);
			else {
				Vector<Composant> derives = ingredient.decompose();
				for (Composant derive : derives) composants.add(derive);
			}
		}
		return composants;
	}
	
	public double getCostPrice() throws Exception {
    	double res = 0;
    	for(Composant ingredient : decompose()) {
    		res += (double)ingredient.get("unitPrice");
    	}
    	return res;
    }
	
	

	public static void main(String[] args) throws Exception {
		Composant c = new Composant(new ConnectionOracle("star","star"), "PRO0004");
		Vector<Composant> ing = c.decompose();
		System.out.println(c.getCostPrice());
	}
	

	
	/**********************************************************************************************************/
	//Get the field attributed to the object
	    public String[] get_fieldObject() {
	        Field[] f = getClass().getDeclaredFields();
	        String[] result = new String[f.length];
	        for (int i = 0; i < f.length; i++) {
	            result[i] = f[i].getName().split("\\.")[f[i].getName().split("\\.").length-1];       
	        }
	        return result;
	    }




	  //Get field that you want
	    public Object get(String field) throws Exception{
	    	String part1 = field.substring(0, 1);
	    	String part2 = field.substring(1);
	    	part1 = part1.toUpperCase();
	    	field = part1.concat(part2);
	        String m = "get"+field;
	        try {
	            Method method = getClass().getDeclaredMethod(m, new Class[0]);
	            return method.invoke(this, new Object[0]);        
	        } catch (Exception e) {
	            throw e;
	        }
	    }

	    public void displayBddObject() {
	        try {
	        	System.out.println("******************");
	            String[] str = get_fieldObject();
	            for (int i = 0; i < str.length ; i++) {
	                System.out.print(str[i]+":");
	                System.out.println(get(str[i]));
	            }
	        	System.out.println("******************");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}
