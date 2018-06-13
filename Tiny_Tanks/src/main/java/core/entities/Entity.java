package core.entities;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

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
	
	public Envelope envelope() {
		return new Envelope(new Coordinate(x, y), new Coordinate(x+width+5, y+height+5)); 
	}
	
	public int getWidth() {
		return width;
	}
	
	public boolean intersects(Entity other) {
		return this.envelope().intersects(other.envelope()) && !this.equals(other);
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setX(double d) {
		this.x = d;
	}
	
	public double getX() {
		return x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getY() {
		return y;
	}
}
