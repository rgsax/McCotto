package core.entities;

import core.parts.Cannon;

public class Bullet extends Entity {
	boolean readyToExplode = false;
	int timer = 45;
	double vX, vY;
	double angle;
	int damage = 1;
	CarroArmato owner;
	public int getDamage() {
		return damage;
	}

	public Bullet(CarroArmato owner, Cannon cannon) {
		super(13, 13);
		this.owner = owner;
		this.angle = cannon.getAngle();
		vX = (int) Math.round(Math.cos(Math.toRadians(angle)) * 10);
		vY = (int) Math.round(Math.sin(Math.toRadians(angle)) * 10);
		x = cannon.getcX() + vX * 10;
		y = cannon.getcY() + vY * 10;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public void update() {
		if(!readyToExplode) {
			--timer;
			if(timer == 0)
				readyToExplode = true;
			else {
				x += vX;
				y += vY;
			}
		}
	}
	
	public void rimbalzaY() {
		vY = -vY;
		update();
		updateAngle();
	}
	public void rimbalzaX() {
		vX = -vX;
		update();
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
