package core.parts;

public class Cannon{
	int cX, cY;
	int x, y;
	int angleX = 0;
	int angleY = 0;
	
	public Cannon(int x, int y, int cX, int cY) {
		this.cX = cX;
		this.cY = cY;
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		cX = cX - this.x;
		this.x = x;
		cX += this.x;
	}

	public int getY() {
		return y;
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
	public void setAngleX(int angleX) {
		this.angleX = angleX;
	}
	public void setAngleY(int angleY) {
		this.angleY = angleY;
	}
	public double getAngle() {
		return(Math.acos((angleX - cX) / Math.hypot(angleX - cX, angleY - cY))) * (angleY - cY < 0 ? -1 : 1);
	}

	public int getAngleX() {
		return angleX;
	}

	public int getAngleY() {
		return angleY;
	}
}
