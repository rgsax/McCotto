package core.entities;

public class BouncyBox extends AbstractBox {
	public BouncyBox(int width, int height, double x, double y) {
		super(width, height, x, y);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean deflect(Bullet b) {
		if(bulletHasCollidedWithBox(b)) {
			if (Math.abs(b.getVX()) >= Math.abs(b.getVY()) && (b.getX() + b.getWidth() >= x - 5 && b.getX() <= x + width + 5)){ 
				b.rimbalzaX();
			}
			if (Math.abs(b.getVY()) >= Math.abs(b.getVX()) && (b.getY() + b.getHeight() >= y && b.getY() - 5 <= y + height + 5)) {
				b.rimbalzaY();
			}
		}
		
		// il proiettile non va mai distrutto dopo un rimbalzo
		return false;
	}

}
