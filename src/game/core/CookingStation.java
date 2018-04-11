package game.core;

public abstract class CookingStation extends Countertop implements Cooker {

	public CookingStation() { }
	
	@Override
	public void cook() {
		ingredient.remainingSecondsToCook--;		
	}
}
