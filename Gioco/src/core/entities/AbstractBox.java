package core.entities;

public abstract class AbstractBox extends Entity {
	static public int minWidth = 40;
	static public int minHeight = 40;
	
	// x, y rappresenta l'angolo in alto a sx, x+width, y+length rappresenta l'angolo in basso a dx
	public AbstractBox(int width, int height, double x, double y) {
		super(width, height, x, y);
	}
	
	boolean bulletHasCollidedWithBox(Bullet b) {
		double dist = Math.hypot(x + width / 2 - (b.getX() + b.getWidth() / 2), y + height / 2 - (b.getY() + b.getHeight() / 2));
		return dist <= (width + b.getWidth()) / 2 || dist <= (height + b.getHeight()) / 2;
	}
	
	// restituisce True se b va distrutto, False altrimenti
	public abstract boolean deflect(Bullet b);

}
