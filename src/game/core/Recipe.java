package game.core;

import java.util.LinkedHashSet;
import java.util.Set;

public class Recipe {
	Set<Ingredient> ingredients = new LinkedHashSet<Ingredient>();
	
	Recipe(Ingredient i, Ingredient j, Ingredient k) {
		ingredients.add(i);
		ingredients.add(j);
		ingredients.add(k);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ingredients == null) ? 0 : ingredients.hashCode());
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
		if (!(obj instanceof Recipe)) {
			return false;
		}
		Recipe other = (Recipe) obj;
		if (ingredients == null) {
			if (other.ingredients != null) {
				return false;
			}
		} else if (!ingredients.equals(other.ingredients)) {
			return false;
		}
		return true;
	}	
	
}
