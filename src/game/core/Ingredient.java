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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + remainingSecondsToCook;
		result = prime * result + remainingSecondsToCut;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Ingredient)) {
			return false;
		}
		Ingredient other = (Ingredient) obj;
		return this.getClass() == obj.getClass() && this.isCooked() == other.isCooked() && this.isCutted() && other.isCutted();
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
	
	void cook(int t) {
		if(this.isCutted())
			remainingSecondsToCook -= t;
	}

	void cut(int t) {
		if (remainingSecondsToCut > 0) {
			remainingSecondsToCut -= t;
		}
	}
	
	public boolean isCooked() {
		return remainingSecondsToCook <= 0; 
	}
	
	public boolean isOvercooked() {
		return remainingSecondsToCook < -5;
	}
	
	public boolean isCutted() {
		return remainingSecondsToCut <= 0; 
	}
}
