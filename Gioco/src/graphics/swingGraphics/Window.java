package graphics.swingGraphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.Timer;

import core.Mondo;
import core.entities.Bullet;
import core.entities.CarroArmato;
import core.entities.Enemy;
import core.parts.Direction;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Window extends JPanel {
	boolean up = false, down = false, right = false, left = false;
	CarroArmato carroPlayer;
	ArrayList<Enemy> enemies = new ArrayList<>();
	Mondo mondo;
	Timer timer;
	Image imgCarroPlayer;
	Image imgCannonePlayer;
	Image imgNemico;
	Image imgCannoneNemico;
	Image imgBullet;
	int rot = 0;
	public Window() {
		mondo = new Mondo();
		carroPlayer = new CarroArmato(300, 300, mondo);
		mondo.setPlayer(carroPlayer);
		mondo.setEnemiesList(enemies);
		Enemy enemy = new Enemy(100, 100, mondo);
		enemies.add(enemy);
		mondo.orientaCannone(enemy, carroPlayer);
		
		initGUI();
		initEH();
	}

	void initGUI() {		
		initTimer();
		timer.start();
		
		loadImages();
	}
	
	void loadImages() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		MediaTracker mt = new MediaTracker(this);
		
		imgCarroPlayer = tk.getImage("./img/carro.png");
		imgCannonePlayer = tk.getImage("./img/cannone.png");
		imgNemico = tk.getImage("./img/nemico.png");
		imgCannoneNemico = tk.getImage("./img/cannone_nemico.png");
		imgBullet = tk.getImage("./img/bullet.png");
		
		mt.addImage(imgCarroPlayer, 0);
		mt.addImage(imgCannonePlayer, 1);
		mt.addImage(imgNemico, 2);
		mt.addImage(imgCannoneNemico, 3);
		mt.addImage(imgBullet, 4);
		
		try {
			mt.waitForAll();
		} catch (InterruptedException e) {
			
		}
	}
	
	void initEH() {
		setFocusable(true);

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				mondo.orientaCannone(carroPlayer, e.getX(), e.getY());
			}
		});
		
		
		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_D)
					right = false;
				if(e.getKeyCode() == KeyEvent.VK_A)
					left = false;
				if(e.getKeyCode() == KeyEvent.VK_W)
					up = false;
				if(e.getKeyCode() == KeyEvent.VK_S)
					down = false;

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_D) {
					right = true;
					if(left)
						left = false;
				}
				if(e.getKeyCode() == KeyEvent.VK_A) {
					left = true;
					if(right)
						right = false;
				}
				if(e.getKeyCode() == KeyEvent.VK_W) {
					up = true;
					if(down)
						down = false;
				}
				if(e.getKeyCode() == KeyEvent.VK_S) {
					down = true;
					if(up)
						up = false;
				}
				if(e.getKeyCode() == KeyEvent.VK_SPACE)
					mondo.spara(carroPlayer);
			
			
		}
	});
}

@Override
protected void paintComponent(Graphics g) {
	super.paintComponent(g);
	
	for(CarroArmato c : enemies) {
		disegnaMacchina(g, c, imgNemico);
		disegnaCannone(g, c, imgCannoneNemico);
	}
	
	//macchina
	disegnaMacchina(g, carroPlayer, imgCarroPlayer);
	
	//Proiettili	
	if(!mondo.getBullets().isEmpty())
		disegnaProiettili(g);
	
	//Cannone
	disegnaCannone(g, carroPlayer, imgCannonePlayer);
	
	//Area di gioco
	g.drawRect(0, 0, 600, 600);
	
}

void disegnaMacchina(Graphics g, CarroArmato c, Image img) {
	Graphics2D g2d = (Graphics2D)g;
	AffineTransform tr = g2d.getTransform();
	AffineTransform trOld = (AffineTransform)tr.clone();
	
	tr.rotate(Math.toRadians(c.getDirection().getAngle()), c.getX() + c.getMacchina().getWidth() / 2, c.getY() + c.getMacchina().getHeight() / 2);
	g2d.setTransform(tr);
	g2d.drawImage(img, c.getX(), c.getY(), null);
	
	g2d.setTransform(trOld);
}

void disegnaProiettili(Graphics g) {
	Graphics2D g2d = (Graphics2D)g;
	AffineTransform tr = g2d.getTransform();
	AffineTransform trOld = (AffineTransform)tr.clone();	
	
	for(Bullet b : mondo.getBullets()) {
		//g.drawLine(b.getX() + b.getWidth() / 2, b.getY() + b.getHeight() / 2, carroPlayer.getCannone().getcX(), carroPlayer.getCannone().getcY());
		tr.rotate(b.getAngle(), b.getX() + b.getWidth() / 2, b.getY() + b.getHeight() / 2);
		g2d.setTransform(tr);
		g2d.drawImage(imgBullet, b.getX(), b.getY(), null);
		
		tr = (AffineTransform)trOld.clone();
		g2d.setTransform(trOld);
	}
}

void disegnaCannone(Graphics g, CarroArmato c, Image img) {
	Graphics2D g2d = (Graphics2D)g;
	AffineTransform tr = g2d.getTransform();
	AffineTransform trOld = (AffineTransform)tr.clone();	
	
	tr.rotate(c.getCannone().getAngle(), c.getCannone().getcX(), c.getCannone().getcY());
	g2d.setTransform(tr);
	g2d.drawImage(img, c.getCannone().getX(), c.getCannone().getY(), null);
	g2d.setTransform(trOld);
}

void initTimer() {
	timer = new Timer(1000 / 30, new ActionListener() {
		int count = 0;
		@Override
		public void actionPerformed(ActionEvent e) {
			count++;
			
			if(up && right)
				mondo.muoviPlayer(Direction.NE);
			else if(up && left)
				mondo.muoviPlayer(Direction.NW);
			else if(down && right)
				mondo.muoviPlayer(Direction.SE);
			else if(down && left)
				mondo.muoviPlayer(Direction.SW);
			else if(right) {
				mondo.muoviPlayer(Direction.E);
			}
			else if(left) {
				mondo.muoviPlayer(Direction.W);
			}
			else if(up) {
				mondo.muoviPlayer(Direction.N);
			}
			else if(down) {
				mondo.muoviPlayer(Direction.S);
			}
			
			mondo.checkCollisions();
		
		//IL NEMICO SI MUOVE QUANDO COUNT ARRIVA A 20	
			if (count >= 20) {
					mondo.muoviNemici();
			}
						
			if(count >= 30) {
				count = 0;
				for(CarroArmato c : enemies) {
					mondo.spara(c);
					
				}
			}
			
			Toolkit.getDefaultToolkit().sync();
			repaint();			
		}
	});
}
}
