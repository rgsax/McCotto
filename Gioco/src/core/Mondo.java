package core;

import java.util.ArrayList;

import core.parts.Bullet;
import core.parts.Cannon;
import core.parts.Direction;
import core.tank.CarroArmato;

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
	
	public void orientaCannone(CarroArmato c, int x, int y) {
		Cannon cannone = c.getCannone();
		cannone.setAngleX(x);
		cannone.setAngleY(y);
	}
	
	public void orientaCannone(CarroArmato c1, CarroArmato c2) {
		orientaCannone(c1, c2.getCannone().getcX(), c2.getCannone().getcY());
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
}
