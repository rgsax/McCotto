package core.entities;

public abstract class AbstractBox extends Entity {
	// x, y rappresenta l'angolo in alto a sx, x+width, y+length rappresenta l'angolo in basso a dx
	public AbstractBox(int width, int height, double x, double y) {
		super(width, height, x, y);
		// TODO Auto-generated constructor stub
	}
	
	boolean bulletHasCollidedWithBox(Bullet b) {
		double dist = Math.hypot(x + width / 2 - (b.getX() + b.getWidth() / 2), y + height / 2 - (b.getY() + b.getHeight() / 2));
		return dist <= width / 2 + b.getWidth() / 2 + 5;
	}
	
	// restituisce True se b va distrutto, False altrimenti
	public abstract boolean deflect(Bullet b);

}
