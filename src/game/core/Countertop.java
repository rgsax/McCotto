package game.core;

public class Countertop extends Tile {
	protected Ingredient ingredient = null;
	
	public Countertop() { }
	
	public void put(Ingredient ingredient) {
		this.ingredient = ingredient;
	}
	
	public Ingredient get() {
		return this.ingredient;
	}
	
	public void process() {	}
}
