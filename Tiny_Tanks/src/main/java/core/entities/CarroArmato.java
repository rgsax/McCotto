package core.entities;

import core.Mondo;
import core.parts.*;

public class CarroArmato extends Entity {
	public static int baseWidth = 50;
	public static int baseHeight = 50;
	boolean collision = false;
	
	Integer id = null;
	
	Cannon cannone;
	Body macchina;
	int speed = 10;
	int life = 3;
	
	int shots = 3;
	Direction direction = Direction.E;
	Mondo mondo;
	
	public CarroArmato(double x, double y, Mondo mondo, Integer id) {
		super(baseWidth, baseHeight, x, y);
		this.mondo = mondo;
		macchina = new Body(x, y);
		cannone = new Cannon(x, y, x + macchina.getWidth() / 2, y + macchina.getHeight() / 2);
		this.id = id;
	}
	
	public CarroArmato(double x, double y, Mondo mondo) {
		super(baseWidth, baseHeight, x, y);
		this.mondo = mondo;
		macchina = new Body(x, y);
		cannone = new Cannon(x, y, x + macchina.getWidth() / 2, y + macchina.getHeight() / 2);
	}
	
	public Integer getId() {
		return id;
	}
	
	public int remainingShots() {
		return shots;
	}
	public Body getMacchina() {
		return macchina;
	}
	public Cannon getCannone() {
		return cannone;
	}
	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}
	
	
	public void muovi(Direction d) {
		double newX = x;
		double newY = y;
		if(d == Direction.W) {
			if(x > 0) {
				newX -= speed;
				//cannone.setX(x);
				//direction = d;
			}
		}
		else if(d == Direction.E) {
			if(x < mondo.getWidth() - macchina.getWidth()) {
				newX += speed;
				//cannone.setX(x);
				//direction = d;
			}
		}
		else if(d == Direction.N) {
			if(y > 0) {
				newY -= speed;
				//cannone.setY(y);
				//direction = d;
			}
		}
		else if(d == Direction.S) {
			if(y < mondo.getHeight() - macchina.getHeight()) {
				newY += speed;
				//cannone.setY(y);	
				//direction = d;	
			}
		}
		else if(d == Direction.NE) {
			if(y > 0 && x < mondo.getWidth() - macchina.getWidth()) {
				newY -= speed;
				newX += speed;
				//muovi(Direction.N);
				//muovi(Direction.E);
				//direction = d;
			}
		}
		else if(d == Direction.NW) {
			if(y > 0 && x > 0) {
				newY -= speed;
				newX -= speed;
				//muovi(Direction.N);
				//muovi(Direction.W);
				//direction = d;
			}
		}
		else if(d == Direction.SE) {
			if(y < mondo.getHeight() - macchina.getHeight() && x < mondo.getWidth() - macchina.getWidth()) {
				newY += speed;
				newX += speed;
				//muovi(Direction.S);
				//muovi(Direction.E);
				//direction = d;
			}
		}
		else if(d == Direction.SW) {
			if(y < mondo.getHeight() - macchina.getHeight() && x > 0) {
				newY += speed;
				newX -= speed;
				//muovi(Direction.S);
				//muovi(Direction.W);
				//direction = d;
			}
		}
		
		//boolean collision = false;
		collision = false;
		for(AbstractBox box : mondo.getBoxes()) {
			if(newX + width >= box.getX() && newX <= box.getX() + box.getWidth() &&
					newY + height >= box.getY() && newY <= box.getY() + box.getHeight())
				collision = true;
		}
		
		if(!collision) {
			direction = d;
			x = newX;
			y = newY;
			cannone.setX(newX);
			cannone.setY(newY);
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
	
	public boolean takeHit(int damage) {
		life -= damage;
		return life <= 0;
	}

	@Override
	public int getWidth() {
		return macchina.getWidth();
	}

	@Override
	public int getHeight() {
		return macchina.getHeight();
	}
}