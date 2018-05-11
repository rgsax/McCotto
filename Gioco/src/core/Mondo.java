package core;

import java.util.ArrayList;

import core.entities.Bullet;
import core.entities.CarroArmato;
import core.entities.Entity;
import core.parts.Cannon;
import core.parts.Direction;

public class Mondo {
	int width = 600;
	int height = 600;
	CarroArmato carro;
	ArrayList<Bullet> bullets = new ArrayList<>();
	ArrayList<CarroArmato> enemies = new ArrayList<>();
	
	public Mondo() {
	}
	
	public void spara(CarroArmato c) {
		if(c.remainingShots() > 0) {
			bullets.add(new Bullet(c, c.getCannone()));
			c.decreaseShots();
		}
	}
	
	public ArrayList<Bullet> getBullets() {
		return bullets;
	}
	
	public void setPlayer(CarroArmato carro) {
		this.carro = carro;
	}
	
	public void setEnemiesList(ArrayList<CarroArmato> enemies) {
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
	
	public void orientaCannone(CarroArmato c, int x, int y) {
		Cannon cannone = c.getCannone();
		cannone.setAngleX(x);
		cannone.setAngleY(y);
	}
	
	public void orientaCannone(CarroArmato c1, CarroArmato c2) {
		orientaCannone(c1, c2.getCannone().getcX(), c2.getCannone().getcY());
	}
	
//MOVIMENTO DEL NEMICO
	public void muoviNemici() {
		for(CarroArmato c : enemies) {
			if(Math.abs(c.getCannone().getcX() - carro.getCannone().getcX()) > carro.getMacchina().getWidth() ||
					Math.abs(c.getCannone().getcY() - carro.getCannone().getcY()) > carro.getMacchina().getHeight()) {
				c.muovitiVerso(carro.getCannone().getcX(), carro.getCannone().getcY());
				orientaCannone(c, carro);
			}
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
				
				if(b.getX() + b.getWidth() >= carro.getX() && b.getX() <= carro.getX() + carro.getMacchina().getWidth() 
						&& b.getY() + b.getHeight() >= carro.getY() && b.getY() <= carro.getY() + carro.getMacchina().getHeight())
				{
					//toDelete.add(carro);
					toDelete.add(b);
				}
				
				for(CarroArmato c : enemies) {
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
						System.out.println("BOOM!!");
						esplodiProiettile((Bullet)o);
					}
					else if (o instanceof CarroArmato) {	
						esplodiNemico((CarroArmato)o);
					}
				}
			}
		}
	}
	
}
