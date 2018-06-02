package core.entities;

import java.util.Random;

import core.Mondo;
import core.parts.Direction;

public class Enemy extends CarroArmato {
	double chaseRangeMax = 200.0;
	double chaseRangeMin = 150.0;
	boolean randomMoving = false;
	
	public Enemy(double x, double y, Mondo mondo) {
		super(x, y, mondo);
		speed = 5;
	}
	
	//METODO PER IL MOVIMENTO DEL NEMICO	
	public void muovitiVerso(double x, double y) {
		randomMoving = false;
		
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
	
	public void pickRandomDirection() {
		if (randomMoving && !collision) {
			muovi(this.direction);
		}
		else {
		Random random = new Random();
		int newDir = random.nextInt(4);
		
		switch(newDir) {
		case 1 : muovi(Direction.N);
			     break;
		case 2 : muovi(Direction.E);
				 break;
		case 3 : muovi(Direction.S);
				 break;
		default : muovi(Direction.W);
				 break;
		}
	}
		randomMoving = true;
	}
	
	public void undo() {
		Direction currentDirection = direction;
		muovi(direction.opposite());
		direction = currentDirection;
	}
}