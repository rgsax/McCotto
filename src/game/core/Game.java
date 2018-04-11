package game.core;

import java.util.ArrayList;
import java.util.List;

public class Game {
	GameMap map;
	Player chef;
	ArrayList<Recipe> orders;
	
	// TODO
	void playerMoves() { 
		int target_x = chef.pos_x + chef.dir_x;
		int target_y = chef.pos_y + chef.dir_y;
		
		if ( map.getTile(target_x, target_y) instanceof Countertop ) {
			//
		} else {
			chef.move();
		}
	}
	
	// TODO
	void addRecipe() { }
	
	// TODO
	void playerInteracts() { }
	
	// TODO
	void removeRecipe() { }
	
	// TODO
	void playerPickups() { }
	
	// TODO
	void playerDrops() { }
	
	
}
