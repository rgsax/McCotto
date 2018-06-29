package core.entities;

import core.parts.Cannon;

public class Bullet extends Entity {
	boolean readyToExplode = false;
	int timer = 45;
	double vX, vY;
	double angle;
	int damage = 1;
	
	public static int baseSpeed = 15;
	
	CarroArmato owner;
	public int getDamage() {
		return damage;
	}
	
	public Bullet(Bullet b) {
		super(10, 10); 
		this.readyToExplode = b.readyToExplode;
		this.timer = b.timer;
		this.owner = b.owner;
		this.angle = b.angle; 
		this.vX = b.vX; 
		this.vY = b.vY; 
		this.baseSpeed = b.baseSpeed;
		this.damage = b.damage;
		x = b.x;
		y = b.y; 
	}

	public Bullet(CarroArmato owner, Cannon cannon) {
		super(13, 13);
		this.owner = owner;
		this.angle = cannon.getAngle();
		vX = (int) Math.round(Math.cos(Math.toRadians(angle)) * baseSpeed);
		vY = (int) Math.round(Math.sin(Math.toRadians(angle)) * baseSpeed);
		x = cannon.getcX() + vX * 3.5;
		y = cannon.getcY() + vY * 3.5;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public void update() {
		if(!readyToExplode) {
			--timer;
			if(timer <= 0)
				readyToExplode = true;
			else {
				x += vX;
				y += vY;
			}
		}
	}
	
	public void update(double i) {
		if(!readyToExplode) {
			timer -= i;
			if(timer <= 0)
				readyToExplode = true;
			else {
				x += i*vX;
				y += i*vY;
			}
		}
		
		updateAngle();
	}
	
	public void undo(double n) {
		x -= n*vX;
		y -= n*vY;
		//timer += (int) n; 
		updateAngle();
	}
	
	public void rimbalzaY() {
		vY = -vY;
		updateAngle();
	}
	public void rimbalzaX() {
		vX = -vX;
		updateAngle();
	}
	public void updateAngle() {
		angle = Math.toDegrees((Math.acos(vX / Math.hypot(vX, vY))) * (vY < 0 ? -1 : 1));
	}
	public CarroArmato getOwner() {
		return owner;
	}
	public boolean isReadyToExplode() {
		return readyToExplode;
	}
	public void setReadyToExplode(boolean readyToExplode) {
		this.readyToExplode = readyToExplode;
	}
	public double getVX() {
		return vX;
	}
	public double getVY() {
		return vY;
	}
}
