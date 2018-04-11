package game.core;

public final class ChoppingBoard extends Countertop {

	@Override
	public void process() {
		ingredient.cut(1);
	}

}
