package core.entities;

public abstract class AbstractBox extends Entity {
	static public int minWidth = 40;
	static public int minHeight = 40;
	
	// x, y rappresenta l'angolo in alto a sx, x+width, y+length rappresenta l'angolo in basso a dx
	public AbstractBox(int width, int height, double x, double y) {
		super(width, height, x, y);
	}
	
	boolean bulletHasCollidedWithBox(Bullet b) {
		return (b.getX() + b.getVX() <= x + width && b.getX() + b.getVX() + b.getWidth() >= x) &&
				(b.getY() + b.getVY() <= y + height && b.getY() + b.getVY() + b.getHeight() >= y);
	}
	
	// restituisce True se b va distrutto, False altrimenti
	public abstract boolean deflect(Bullet b);

}
