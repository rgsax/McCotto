package game.core;

public abstract class Ingredient extends GameObject {
	protected int remainingSecondsToCut;
	protected int remainingSecondsToCook;
	
//	public Ingredient(int remainingSecondsToCut, int remainingSecondsToCook) {
//		super();
//		this.remainingSecondsToCut = remainingSecondsToCut;
//		this.remainingSecondsToCook = remainingSecondsToCook;
//	}

	public int getRemainingSecondsToCut() {
		return remainingSecondsToCut;
	}

	public void setRemainingSecondsToCut(int remainingSecondsToCut) {
		this.remainingSecondsToCut = remainingSecondsToCut;
	}

	public int getRemainingSecondsToCook() {
		return remainingSecondsToCook;
	}

	public void setRemainingSecondsToCook(int remainingSecondsToFry) {
		this.remainingSecondsToCook = remainingSecondsToFry;
	}

	public void cook(int t) {
		if (remainingSecondsToCook <= 0) {
			remainingSecondsToCook -= t;
		}
	}

	public void cut(int t) {
		if (remainingSecondsToCut <= 0) {
			remainingSecondsToCut -= t;
		}
	}
	
	public boolean isCooked() {
		return remainingSecondsToCook <= 0; 
	}
	
	public boolean isCutted() {
		return remainingSecondsToCut <= 0; 
	}
}
