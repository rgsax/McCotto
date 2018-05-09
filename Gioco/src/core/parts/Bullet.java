package core.parts;

import core.tank.CarroArmato;

public class Bullet {
	int x, y;
	int vX, vY;
	double angle;
	int width = 20;
	int height = 10;
	CarroArmato owner;
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Bullet(CarroArmato owner, Cannon line) {
		this.owner = owner;
		this.angle = line.getAngle();
		vX = (int) Math.round(Math.cos(angle) * 10);
		vY = (int) Math.round(Math.sin(angle) * 10);
		this.x = line.cX - width / 2 + 4 * vX;
		this.y = line.cY - height / 2 + 4 * vY;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public void update() {		
		x += vX;
		y += vY;
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
	public void rimbalzaY() {
		vY = -vY;
		updateAngle();
	}
	public void rimbalzaX() {
		vX = -vX;
		updateAngle();
	}
	public void updateAngle() {
		angle = (Math.acos(vX / Math.hypot(vX, vY))) * (vY < 0 ? -1 : 1);
	}
	public CarroArmato getOwner() {
		return owner;
	}
}
