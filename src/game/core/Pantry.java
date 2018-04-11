package game.core;

public final class Pantry<I extends Ingredient> extends Countertop {
	I ingredient;
	public Pantry(I ingredient) {
		this.ingredient = ingredient;
	}
	
	public I getIngredient() {
		return ingredient;
	}
}
