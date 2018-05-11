package core.parts;

import core.entities.Entity;

public class Cannon extends Entity{
	int cX, cY;
	double angleX = 0;
	double angleY = 0;
	
	public Cannon(int x, int y, int cX, int cY) {
		super(50, 50, x, y);
		this.cX = cX;
		this.cY = cY;
	}

	public void setX(int x) {
		cX = cX - this.x;
		this.x = x;
		cX += this.x;
	}

	public void setY(int y) {
		cY = cY - this.y;
		this.y = y;
		cY += this.y;
	}

	public void setcX(int cX) {
		this.cX = cX;
	}

	public void setcY(int cY) {
		this.cY = cY;
	}

	
	public int getcX() {
		return cX;
	}

	public int getcY() {
		return cY;
	}
	public void setAngleX(double d) {
		this.angleX = d;
	}
	public void setAngleY(double e) {
		this.angleY = e;
	}
	public double getAngle() {
		return Math.toDegrees((Math.acos((angleX - cX) / Math.hypot(angleX - cX, angleY - cY))) * (angleY - cY < 0 ? -1 : 1));
	}

	public double getAngleX() {
		return angleX;
	}

	public double getAngleY() {
		return angleY;
	}

}
