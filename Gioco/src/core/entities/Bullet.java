package core.entities;

import core.parts.Cannon;

public class Bullet extends Entity {
	int vX, vY;
	double angle;
	int damage = 1;
	CarroArmato owner;
	public int getDamage() {
		return damage;
	}

	public Bullet(CarroArmato owner, Cannon line) {
		super(20, 10);
		this.owner = owner;
		this.angle = line.getAngle();
		vX = (int) Math.round(Math.cos(Math.toRadians(angle)) * 10);
		vY = (int) Math.round(Math.sin(Math.toRadians(angle)) * 10);
		x = line.getcX() - width / 2 + 4 * vX;
		y = line.getcY() - height / 2 + 4 * vY;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public void update() {		
		x += vX;
		y += vY;
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
}
