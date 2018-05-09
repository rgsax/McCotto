package core.tank;

import core.Mondo;
import core.parts.*;

public class CarroArmato {
	Cannon cannone;
	Body macchina;
	int speed = 10;
	int x, y;
	int shots = 3;
	Direction direction = Direction.E;
	Mondo mondo;
	public CarroArmato(int x, int y, Mondo mondo) {
		this.mondo = mondo;
		this.x = x;
		this.y = y;
		macchina = new Body(x, y);
		cannone = new Cannon(x, y, x + macchina.getWidth() / 2, y + macchina.getHeight() / 2);
	}
	
	public int remainingShots() {
		return shots;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public Body getMacchina() {
		return macchina;
	}
	public Cannon getCannone() {
		return cannone;
	}
	
	
	public void muovi(Direction d) {		
		if(d == Direction.W) {
			if(x > 0) {
				x -= speed;
				cannone.setX(x);
				direction = d;
			}
		}
		else if(d == Direction.E) {
			if(x < mondo.getWidth() - macchina.getWidth()) {
				x += speed;
				cannone.setX(x);
				direction = d;
			}
		}
		else if(d == Direction.N) {
			if(y > 0) {
				y -= speed;
				cannone.setY(y);
				direction = d;
			}
		}
		else if(d == Direction.S) {
			if(y < mondo.getHeight() - macchina.getHeight()) {
				y += speed;
				cannone.setY(y);	
				direction = d;	
			}
		}
		else if(d == Direction.NE) {
			if(y > 0 && x < mondo.getWidth() - macchina.getWidth()) {
				muovi(Direction.N);
				muovi(Direction.E);
				direction = d;
			}
		}
		else if(d == Direction.NW) {
			if(y > 0 && x > 0) {
				muovi(Direction.N);
				muovi(Direction.W);
				direction = d;
			}
		}
		else if(d == Direction.SE) {
			if(y < mondo.getHeight() - macchina.getHeight() && x < mondo.getWidth() - macchina.getWidth()) {
				muovi(Direction.S);
				muovi(Direction.E);
				direction = d;
			}
		}
		else if(d == Direction.SW) {
			if(y < mondo.getHeight() - macchina.getHeight() && x > 0) {
				muovi(Direction.S);
				muovi(Direction.W);
				direction = d;
			}
		}
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public void increaseShots() {
		shots++;
	}
	
	public void decreaseShots() {
		shots--;
	}
	
	public void setShots(int shots) {
		this.shots = shots;
	}
}
