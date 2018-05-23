package core.entities;

import sun.security.util.Length;

public class BouncyBox extends AbstractBox {
	public BouncyBox(double x, double y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean deflect(Bullet b) {
		if(b.getX() > x && b.getX() < x + width && b.getY() > y && b.getY() < y + height) {
			b.setReadyToExplode(true);
		}
		else if(bulletHasCollidedWithBox(b)) {
			if((b.getX() < x && b.getX() + b.getWidth() > x) || (b.getX() < x + width && b.getX() + b.getWidth() > x + width)) {
				b.rimbalzaX();
			}
			
			if((b.getY() < y && b.getY() + b.getHeight() > y) || (b.getY() < y + height && b.getY() + b.getHeight() > y + height)) {
				b.rimbalzaY();
			}
		}
		
		// il proiettile non va mai distrutto dopo un rimbalzo
		return false;
	}

}
