import game.core.*;

public class Main {
	public static void main(String[] args) {
		Ingredient i = new Meat();
		Countertop tagliere = new ChoppingBoard();
		Countertop piastra = new Hotplate();
		tagliere.put(i);
		tagliere.process();
		tagliere.process();
		tagliere.process();
		
		piastra.put(i);
		piastra.process();
		piastra.process();
		
		System.out.println(tagliere.get().isCutted());
		System.out.println(tagliere.get().isCooked());
		System.out.println(tagliere.get().isOvercooked());

	}

}
