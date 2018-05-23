package core.entities;

public class DestructibleBox extends AbstractBox {
	int health; 
	public DestructibleBox(double x, double y) {
		super(x, y);
		this.health = 1; 
		// TODO Auto-generated constructor stub
	}

	public void destroy() {
		health = 0; 
	}

	@Override
	public boolean deflect(Bullet b) {
		if (bulletHasCollidedWithBox(b)) {
			this.health -= b.getDamage(); // forse vogliamo muri più resistenti?
			
			// il proiettile va distrutto
			return health <= 0;
		}
		
		return false;
	}
	
	
}
