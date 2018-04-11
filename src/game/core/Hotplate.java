package game.core;

public class Hotplate extends Countertop implements Cooker{

	@Override
	public void cook() {
		ingredient.remainingSecondsToCook--;		
	}
	
}
