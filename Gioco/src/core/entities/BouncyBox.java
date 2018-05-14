package core.entities;

public class BouncyBox extends AbstractBox {
	public BouncyBox(int width, int height, double x, double y) {
		super(width, height, x, y);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean deflect(Bullet b) {
		if(bulletHasCollidedWithBox(b)) {
			if (b.getX() + b.getWidth() >= x && b.getX() <= x + width) { 
				b.rimbalzaX();
			}
			
			if (b.getY() + b.getHeight() >= y && b.getY() <= y + height) {
				b.rimbalzaY();
			}
		}
		
		// il proiettile non va mai distrutto dopo un rimbalzo
		return false;
	}

}
