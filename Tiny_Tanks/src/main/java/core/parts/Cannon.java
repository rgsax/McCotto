package core.parts;

import core.entities.Entity;

public class Cannon extends Entity{
	double cX, cY;
	double angleX = 0;
	double angleY = 0;
	
	public static int baseWidth = 75;
	public static int baseHeight = 50;
	
	public Cannon(double x, double y, double cX, double cY) {
		super(baseWidth, baseHeight, x, y);
		this.cX = cX;
		this.cY = cY;
	}

	public void setX(double x) {
		cX = cX - this.x;
		this.x = x;
		cX += this.x;
	}

	public void setY(double y) {
		cY = cY - this.y;
		this.y = y;
		cY += this.y;
	}

	public void setcX(double cX) {
		this.cX = cX;
	}

	public void setcY(double cY) {
		this.cY = cY;
	}

	
	public double getcX() {
		return cX;
	}

	public double getcY() {
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
