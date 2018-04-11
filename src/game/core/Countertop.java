package game.core;

public class Countertop extends Tile {
	Ingredient ingredient = null;
	public void put(Ingredient ingredient) {
		this.ingredient = ingredient;
	}
}
