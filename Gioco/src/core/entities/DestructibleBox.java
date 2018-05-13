package core.entities;

public class DestructibleBox extends AbstractBox {
	int health; 
	public DestructibleBox(int width, int height, int x, int y) {
		super(width, height, x, y);
		this.health = 1; 
		// TODO Auto-generated constructor stub
	}

	public void destroy() {
		health = 0; 
	}

	@Override
	boolean deflect(Bullet b) {
		if (bulletHasCollidedWithBox(b)) {
			this.health -= 1; // forse vogliamo muri più resistenti?
			
			// il proiettile va distrutto
			return true;
		}
		
		return false;
	}
	
	
}
