package core;

import java.util.ArrayList;
import java.util.HashMap;

import com.vividsolutions.jts.algorithm.RectangleLineIntersector;
import com.vividsolutions.jts.geom.Coordinate;

import core.entities.AbstractBox;
import core.entities.BouncyBox;
import core.entities.Bullet;
import core.entities.CarroArmato;
import core.entities.DestructibleBox;
import core.entities.Enemy;
import core.entities.Entity;
import core.parts.Cannon;
import core.parts.Direction;

public class Mondo {
	int width;
	int height;
	CarroArmato carro;
	
	// (40, 40) sono le dimensioni delle casse
	boolean[][] worldMatrix = new boolean[width/40][height/40];
	ArrayList<Bullet> bullets = new ArrayList<>();
	ArrayList<Enemy> enemies = new ArrayList<>();
	ArrayList<AbstractBox> boxes = new ArrayList<>();
	HashMap<Integer, CarroArmato> players = new HashMap<>();
	
	public Mondo(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void setPlayers(HashMap<Integer, CarroArmato> players) {
		this.players = players;
		carro = this.players.get(0);
	}
	
	public boolean checkBoxes (CarroArmato c) {
		Coordinate playerCenter = new Coordinate(carro.getX()+carro.getWidth()/2, carro.getY()+carro.getHeight()/2);
		Coordinate enemyCenter =  new Coordinate(c.getX()+c.getHeight()/2, c.getY()+c.getWidth()/2);
		
		for (AbstractBox AB : boxes) {
			RectangleLineIntersector r = new RectangleLineIntersector(AB.envelope());
			if (r.intersects(playerCenter, enemyCenter)) return true;
		}
		  /*if (Math.hypot(carro.getCannone().getcX() - c.getCannone().getcX(),
					  carro.getCannone().getcY() - c.getCannone().getcY()) + AB.getWidth() * AB.getHeight() / 100 >=
								Math.hypot(carro.getCannone().getcX() - AB.getX(), 
										carro.getCannone().getcY() - AB.getY()) + 
								Math.hypot(c.getCannone().getcX() - AB.getX(),  
										c.getCannone().getcY() - AB.getY())) {
											return true;
										}
		  }*/
	  
	  return false;
	}
	
	public void spara(CarroArmato c) {
		boolean scatolaInMezzo = false;
		
		if (c instanceof Enemy) {
			for (AbstractBox AB : boxes) { //FARE PROVE MODIFICANDO QUEL +15
				  if ((AB instanceof BouncyBox) && checkBoxes(c))
					  scatolaInMezzo = true;
			}
		}	
		if(!scatolaInMezzo && c.remainingShots() > 0) {
			bullets.add(new Bullet(c, c.getCannone()));
			c.decreaseShots();
		}
	}
	
	public void addBox(AbstractBox ab) {
		boxes.add(ab);
		worldMatrix[(int) ab.getX()/40][(int) ab.getY()/40] = true;
		System.out.println("Aggiunta cassa @ " + (int) ab.getX()/40 + ", " + (int) ab.getY()/40);
	}
	
	public void setBoxes(ArrayList<AbstractBox> boxes) {
		this.boxes = boxes;
	}
	
	public ArrayList<AbstractBox> getBoxes() {
		return boxes;
	}
	
	public ArrayList<Bullet> getBullets() {
		return bullets;
	}
	
	public void setPlayer(CarroArmato carro) {
		this.carro = carro;
	}
	
	public void setEnemiesList(ArrayList<Enemy> enemies) {
		this.enemies = enemies;
	}
	
	public void muoviPlayer(Direction d) {
		carro.muovi(d);
		for(CarroArmato c : enemies) {
			orientaCannone(c, carro);
		}
	}
	
	public void sparaPlayer() {
		spara(carro);
	}
	
	public void esplodiProiettile(Bullet b) {
		deleteBullet(b);
	}
	
	public void esplodiNemico(CarroArmato c) {
		enemies.remove(c);
	}
	
	public void orientaCannone(CarroArmato c, double d, double e) {
		Cannon cannone = c.getCannone();
		cannone.setAngleX(d);
		cannone.setAngleY(e);
	}
	
	public void orientaCannone(CarroArmato c1, CarroArmato c2) {
		orientaCannone(c1, c2.getCannone().getcX(), c2.getCannone().getcY());
	}
	
//MOVIMENTO DEL NEMICO
	public void muoviNemici() {
		for(Enemy c : enemies) {
		  if (!checkBoxes(c)) {	
			c.muovitiVerso(carro.getCannone().getcX(), carro.getCannone().getcY());
		  }
		  else
			c.pickRandomDirection();  
			for(Enemy e : enemies)
				/*if(!e.equals(c) && 
						(c.getX() + c.getWidth() >= e.getX() && c.getX() <= e.getX() + e.getWidth()) &&
						(c.getY() + c.getHeight() >= e.getY() && c.getY() <= e.getY() + e.getHeight()))*/
				if (c.intersects(e))
					c.undo();
			orientaCannone(c, carro);
		}
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	public void deleteBullet(Bullet b) {
		b.getOwner().increaseShots();
		bullets.remove(b);
	}
	
	public void deletBox(DestructibleBox box) {
		boxes.remove(box);
	}
	
	public void update() {
			ArrayList<Entity> toDelete = new ArrayList<>();
			for(Bullet b : getBullets()) {
				b.update();
				
				checkBorderCollision(b);
				checkBulletsCollision(b);
				
				toDelete.addAll(checkBoxesCollision(b));
				toDelete.addAll(checkPlayersCollision(b));
				toDelete.addAll(checkEnemiesCollision(b));				
			}

			toDelete.addAll(checkExplodibleBullets());
			
			destroyEntities(toDelete);
	}
	
	void checkBorderCollision(Bullet b) {
		if(b.getX() < 0 || b.getX() > width - b.getWidth()) {
			b.rimbalzaX();
		}
		if(b.getY() < 0 || b.getY() > height - b.getHeight()) {
			b.rimbalzaY();
		}
	}
	
	ArrayList<Entity> checkEnemiesCollision(Bullet b) {
		ArrayList<Entity> toDelete = new ArrayList<>();
		for(Enemy c : enemies) {
			/*if(b.getX() + b.getWidth() >= c.getX() && b.getX() <= c.getX() + c.getMacchina().getWidth() 
					&& b.getY() + b.getHeight() >= c.getY() && b.getY() <= c.getY() + c.getMacchina().getHeight())*/
			if (b.intersects(c))
			{
				if(c.takeHit(b.getDamage()))
					toDelete.add(c);
				b.setReadyToExplode(true);
			}
		}
		
		return toDelete;
	}
	
	ArrayList<Entity> checkBoxesCollision(Bullet b) {
		ArrayList<Entity> toDelete = new ArrayList<>();
		for(AbstractBox box : boxes) {
			if(box.intersects(b)) {
				if (box.deflect(b)) b.setReadyToExplode(true);
				toDelete.add(box);
			}
		}
		
		return toDelete;
	}
		
	ArrayList<Entity> checkPlayersCollision(Bullet b) {
		ArrayList<Entity> toDelete = new ArrayList<>();
		/*if(b.getX() + b.getWidth() >= carro.getX() && b.getX() <= carro.getX() + carro.getMacchina().getWidth() 
				&& b.getY() + b.getHeight() >= carro.getY() && b.getY() <= carro.getY() + carro.getMacchina().getHeight())*/
		if (b.envelope().intersects(carro.envelope()))
		{
			for(CarroArmato player : players.values()) {
				if(player.takeHit(b.getDamage())) {
					System.out.println("qualcuno ha perso!");
					toDelete.add(player);
					//System.exit(0);
				}
				b.setReadyToExplode(true);
			}
		}
		
		return toDelete;
	}
	
	void checkBulletsCollision(Bullet b) {
		for(Bullet bullet : bullets) {
			/*if(!b.equals(bullet) && b.getX() + b.getWidth() >= bullet.getX() && b.getX() <= bullet.getX() + bullet.getWidth()
				&& b.getY() + b.getHeight() >= bullet.getY() && b.getY() <= bullet.getY() + bullet.getHeight())*/
			if (b.intersects(bullet))
			{
				bullet.setReadyToExplode(true);
				b.setReadyToExplode(true);
			}
		}
	}
	
	ArrayList<Entity> checkExplodibleBullets() {
		ArrayList<Entity> toDelete = new ArrayList<>();
		for(Bullet bullet : bullets) {
			if(bullet.isReadyToExplode())
				toDelete.add(bullet);
		}
		
		return toDelete;
	}
	
	void destroyEntities(ArrayList<Entity> toDelete) {
		for(Entity o : toDelete) {
			if(o instanceof Bullet) {
				esplodiProiettile((Bullet)o);
			}
			else if (o instanceof Enemy) {	
				esplodiNemico((CarroArmato)o);
			}
			else if(o instanceof DestructibleBox) {
				deletBox((DestructibleBox)o); 
				//worldMatrix[(int) o.getX()/40][(int) o.getY()/40] = false;
			}
			else if(o instanceof CarroArmato) {
				players.remove(((CarroArmato)o).getId());
			}
		}
	}
}
