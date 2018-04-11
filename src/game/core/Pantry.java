package game.core;

public final class Pantry extends Countertop {
	Ingredient ingredient;
	public Pantry(Ingredient ingredient) {
		this.ingredient = ingredient;
	}
	
	public Ingredient getIngredient() {
		return ingredient;
	}
}
