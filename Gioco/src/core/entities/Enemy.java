package core.entities;

import core.Mondo;
import core.parts.Direction;

public class Enemy extends CarroArmato {

	public Enemy(int x, int y, Mondo mondo) {
		super(x, y, mondo);
	}
	
	//METODO PER IL MOVIMENTO DEL NEMICO	
	public void muovitiVerso(int x, int y) {
			
		boolean dirW = false;
		boolean dirE = false;
		boolean dirN = false;
		boolean dirS = false;
		
		Direction newDirection = this.direction;
		
		if (this.x < x) { 
			dirE = true;			
		}
		else if (this.x > x) {
			dirW = true;
		}
		if (this.y < y) {
			dirS = true;
		}
		else if (this.y > y) {
			dirN = true;
		}
		
		if (dirW && dirN) {
			newDirection = Direction.NW;
		}
		else if (dirE && dirN) {
			newDirection = Direction.NE;
		}
		else if (dirW && dirS) {
			newDirection = Direction.SW;
		}
		else if (dirE && dirS) {
			newDirection = Direction.SE;
		}
		else if (dirN) {
			newDirection = Direction.N;
		}
		else if (dirS) {
			newDirection = Direction.S;
		}
		else if (dirE) {
			newDirection = Direction.E;
		}
		else if (dirW) {
			newDirection = Direction.W;
		}
		
		muovi(newDirection);
	}
}
