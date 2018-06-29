package it.unical.igpe.tinyTanks.core.entities;
import com.vividsolutions.jts.algorithm.RectangleLineIntersector;
import com.vividsolutions.jts.geom.Coordinate;

public class BouncyBox extends AbstractBox {
	public BouncyBox(int width, int height, double x, double y) {
		super(width, height, x, y);
	}
	
	public BouncyBox(double x, double y) {
		super(minWidth, minHeight, x, y);
	}
	
	@Override	
	public boolean deflect(Bullet b) {		
		// il proiettile è dentro la cassa: nel mondo che vorrei non ci sarebbe nemmeno arrivato
		while (this.envelope().contains(b.envelope())) {
			// lo porto indietro finché non è fuori
			b.undo(.10);
		}
		
		// sono al timestep in cui vorrei che il proiettile rimbalzasse
		Bullet dummy = new Bullet(b);
		
		// Ma mi assicuro che non sia completamente fuori
		while (! this.envelope().intersects(b.envelope())) {
			b.update(.1);
		}
		
		// spigoli della box
		Coordinate UL = new Coordinate(x, y);
		Coordinate UR = new Coordinate(x+width, y);
		Coordinate BL = new Coordinate(x, y+height);
		Coordinate BR = new Coordinate(x+width, y+height);
		
		// guardo con quale lato collide e rimbalzo di conseguenza
		RectangleLineIntersector r = new RectangleLineIntersector(dummy.envelope());
		boolean up = r.intersects(UL, UR);
		boolean down = r.intersects(BL, BR);
		boolean right = r.intersects(UR, BR);
		boolean left = r.intersects(UL, BL);
		
		if (up && left || up && right || down && right || down && left) {
			b.rimbalzaX();
			b.rimbalzaY();
			// SPIGOLI
			// Potrebbe capitare: quando interseca "solo" gli spigoli
		} else if (up) {
			b.rimbalzaY();
		} else if (down) {
			b.rimbalzaY();
		} else if (left) {
			b.rimbalzaX();
		} else if (right) {
			b.rimbalzaX();
		}
		
		//while (this.intersects(b)) {
		//	b.update(.2);
		//}
		
		/*
		if (bulletHasCollidedWithBox(copy))
			if(Math.abs(copy.getVX()) > Math.abs(copy.getVY()) && copy.getX() > x + width && copy.getX() + copy.getWidth() < x) {
				b.rimbalzaX();
			}
			
			else if(Math.abs(copy.getVX()) < Math.abs(copy.getVY()) && copy.getY() > y + height && copy.getY() + copy.getHeight() < y) {
				b.rimbalzaY();
			}
		*/
	
		// il proiettile non va mai distrutto dopo un rimbalzo
		return false;
	}
}
