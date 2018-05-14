package core.entities;

public abstract class Entity {
	protected int width, height;
	protected double x, y;
	
	public Entity(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public Entity(int width, int height, double x, double y) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public double getX() {
		return x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public double getY() {
		return y;
	}
}
