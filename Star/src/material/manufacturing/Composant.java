package material.manufacturing;

public class Composant {
//	FIELDS
	String idComposant;
	Composant[] ingredients;
	boolean isPrimaire;
	double unitPrice;
	
//	CONSTRUCTOR
	public Composant(String idComposant, Composant[] ingredients, boolean isPrimaire, double unitPrice) {
		setIdComposant(idComposant);
		setIngredients(ingredients);
		setIsPrimaire(isPrimaire);
		setUnitPrice(unitPrice);
	}
	
	
//	GETTER
	public String getIdComposant() {
		return idComposant;
	}
	public Composant[] getIngredients() {
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
	public void setIngredients(Composant[] ingredients) {
		this.ingredients = ingredients;
	}
	public void setIsPrimaire(boolean isPrimaire) {
		this.isPrimaire = isPrimaire;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	
//	DECOMPOSER
	
}
