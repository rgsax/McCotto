package core.entities;

public abstract class AbstractBox extends Entity {
	// x, y rappresenta l'angolo in alto a sx, x+width, y+length rappresenta l'angolo in basso a dx
	public AbstractBox(int width, int height, int x, int y) {
		super(width, height, x, y);
		// TODO Auto-generated constructor stub
	}
	
	boolean bulletHasCollidedWithBox(Bullet b) {
		return (b.getX() >= this.x && b.getX() <= this.x+this.width) && (b.getY() >= y && b.getY() <= this.y + this.height);
	}
	
	// restituisce True se b va distrutto, False altrimenti
	abstract boolean deflect(Bullet b);

}
