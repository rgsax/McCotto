package core.entities;

public class BouncyBox extends AbstractBox {
	public BouncyBox(int width, int height, int x, int y) {
		super(width, height, x, y);
		// TODO Auto-generated constructor stub
	}

	@Override
	boolean deflect(Bullet b) {
		if (bulletHasCollidedWithBox(b)) { // <-- sicuramente superfluo
			// devo capire in quale direzione deflettere
			if (b.getX() >= x && b.getX() <= x + width) { 
				b.rimbalzaY();
			} else if (b.getY() >= y && b.getY() <= y + height) {
				b.rimbalzaX();
			}
		}
		
		// il proiettile non va mai distrutto dopo un rimbalzo
		return false;
	}

}
