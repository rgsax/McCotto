package core.entities;

import core.Mondo;
import core.parts.Direction;

public class Enemy extends CarroArmato {
	double chaseRangeMax = 300.0;
	double chaseRangeMin = 150.0;
	
	public Enemy(int x, int y, Mondo mondo) {
		super(x, y, mondo);
	}
	
	//METODO PER IL MOVIMENTO DEL NEMICO	
	public void muovitiVerso(double x, double y) {
		double cannoneX = cannone.getX();
		double cannoneY = cannone.getY();
		
		if (Math.hypot(cannoneX - x, cannoneY - y) > chaseRangeMax) {
			boolean dirW = false;
			boolean dirE = false;
			boolean dirN = false;
			boolean dirS = false;
			
			Direction newDirection = this.direction;
			
			if (cannoneX < x) { 
				dirE = true;			
			}
			else if (cannoneX > x) {
				dirW = true;
			}
			if (cannoneY < y) {
				dirS = true;
			}
			else if (cannoneY > y) {
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
		else if(Math.hypot(cannoneX - x, cannoneY - y) < chaseRangeMin) {
			boolean dirW = false;
			boolean dirE = false;
			boolean dirN = false;
			boolean dirS = false;
			
			Direction newDirection = this.direction;
			
			if (cannoneX < x) { 
				dirW = true;			
			}
			else if (cannoneX > x) {
				dirE = true;
			}
			if (cannoneY < y) {
				dirN = true;
			}
			else if (cannoneY > y) {
				dirS = true;
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
}
