package game.core;

public abstract class CookingStation extends Countertop {

	public CookingStation() { }
	
	@Override
	public void process() {
		ingredient.cook(1);		
	}
}
