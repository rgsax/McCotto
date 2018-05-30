package core.entities;

public class BouncyBox extends AbstractBox {
	public BouncyBox(int width, int height, double x, double y) {
		super(width, height, x, y);
	}
	
	public BouncyBox(double x, double y) {
		super(minWidth, minHeight, x, y);
	}

	@Override
	public boolean deflect(Bullet b) {
		if(b.getX() > x && b.getX() + b.getWidth() < x + width && b.getY() > y && b.getY() + b.getHeight() < y + height) {
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
