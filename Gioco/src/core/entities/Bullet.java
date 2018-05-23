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

	public Bullet(CarroArmato owner, Cannon line) {
		super(10, 10);
		this.owner = owner;
		this.angle = line.getAngle();
		vX = (int) Math.round(Math.cos(Math.toRadians(angle)) * 6);
		vY = (int) Math.round(Math.sin(Math.toRadians(angle)) * 6);
		x = line.getcX() - width / 2 + 8 * vX;
		y = line.getcY() - height / 2 + 8 * vY;
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
