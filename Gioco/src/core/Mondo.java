package core;

import java.util.ArrayList;

import javax.swing.border.AbstractBorder;

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
	int width = 600;
	int height = 600;
	CarroArmato carro;
	
	// (40, 40) sono le dimensioni delle casse
	boolean[][] worldMatrix = new boolean[width/40][height/40];
	ArrayList<Bullet> bullets = new ArrayList<>();
	ArrayList<Enemy> enemies = new ArrayList<>();
	ArrayList<AbstractBox> boxes = new ArrayList<>();
	
	public Mondo() {
		
	}
	
	public void spara(CarroArmato c) {
boolean scatolaInMezzo = false;
		
		if (c instanceof Enemy) {
			for (AbstractBox AB : boxes) { //FARE PROVE MODIFICANDO QUEL +15
				  if (Math.sqrt(Math.pow(carro.getCannone().getcX() - c.getCannone().getcX(), 2) 
									+ Math.pow(carro.getCannone().getcY() - c.getCannone().getcY(), 2)) + 15 >=
									Math.sqrt(Math.pow(carro.getCannone().getcX() - AB.getX(), 2) 
											+ Math.pow(carro.getCannone().getcY() - AB.getY(), 2)) + 
									Math.sqrt(Math.pow(c.getCannone().getcX() - AB.getX(), 2) 
											+ Math.pow(c.getCannone().getcY() - AB.getY(), 2)))
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
			c.muovitiVerso(carro.getCannone().getcX(), carro.getCannone().getcY());
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
	
	public void checkCollisions() {
		if(!bullets.isEmpty()) {
			ArrayList<Entity> toDelete = new ArrayList<>();
			for(Bullet b : getBullets()) {
				b.update();
				if(b.getX() < 0 || b.getX() > 600 - b.getWidth()) {
					b.rimbalzaX();
				}
				if(b.getY() < 0 || b.getY() > 600 - b.getHeight()) {
					b.rimbalzaY();
				}
				
				for(AbstractBox box : boxes) {
						if(box.deflect(b)) {
							toDelete.add(b);
							toDelete.add(box);
						}
				}
				
				if(b.getX() + b.getWidth() >= carro.getX() && b.getX() <= carro.getX() + carro.getMacchina().getWidth() 
						&& b.getY() + b.getHeight() >= carro.getY() && b.getY() <= carro.getY() + carro.getMacchina().getHeight())
				{
					//toDelete.add(carro);
					toDelete.add(b);
				}
				
				for(Enemy c : enemies) {
					if(b.getX() + b.getWidth() >= c.getX() && b.getX() <= c.getX() + c.getMacchina().getWidth() 
							&& b.getY() + b.getHeight() >= c.getY() && b.getY() <= c.getY() + c.getMacchina().getHeight())
					{
						if(c.takeHit(b.getDamage()))
							toDelete.add(c);
						toDelete.add(b);
					}
				}
				
				for(Bullet bullet : bullets) {
					if(!b.equals(bullet) && b.getX() + b.getWidth() >= bullet.getX() && b.getX() <= bullet.getX() + bullet.getWidth()
						&& b.getY() + b.getHeight() >= bullet.getY() && b.getY() <= bullet.getY() + bullet.getHeight())
					{
						toDelete.add(bullet);
					}
				}
			}
		
			if(!toDelete.isEmpty()) {
				for(Entity o : toDelete) {
					if(o instanceof Bullet) {
						esplodiProiettile((Bullet)o);
					}
					else if (o instanceof CarroArmato) {	
						esplodiNemico((CarroArmato)o);
					}
					else if(o instanceof DestructibleBox) {
						System.out.println("Casse @ " + (int) o.getX()/40 + ", " + (int) o.getY()/40 + " distrutta");
						deletBox((DestructibleBox)o); 
						worldMatrix[(int) o.getX()/40][(int) o.getY()/40] = false;
					}
				}
			}
		}
	}
	
}
