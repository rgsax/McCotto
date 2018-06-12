package graphics.FXgraphics;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

import core.Mondo;
import core.entities.AbstractBox;
import core.entities.BouncyBox;
import core.entities.Bullet;
import core.entities.CarroArmato;
import core.entities.DestructibleBox;
import core.entities.Enemy;
import core.entities.Entity;
import core.parts.Cannon;
import core.parts.Direction;
import graphics.Server;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public class GameWindow extends GridPane{
	
	/*public static void main(String[] args) {
		launch(args);
	}*/

	boolean up = false, down = false, left = false, right = false;
	Image imgCarroPlayer;
	Image imgCannonePlayer;
	Image imgNemico;
	Image imgCannoneNemico;
	Image imgBullet;
	Image imgBouncyBox;
	Image imgDestructibleBox;
	
	CarroArmato carroPlayer;
	ArrayList<Enemy> enemies = new ArrayList<>();
	ArrayList<AbstractBox> boxes = new ArrayList<>();
	HashMap<Integer, CarroArmato> players = new HashMap<>();
	Mondo mondo;
	
	Canvas canvas;
	GraphicsContext gc;
	
	Server server = new Server(8182);
	
	public GameWindow() {
		super();
		caricaMappa();
		
		initGUI();
		initEH();
		
		server.init(players, mondo);
		
		mondo.setPlayers(players);
		carroPlayer = players.get(new Integer(0));
		
		initTimers();
	}
	

	@SuppressWarnings("unused")
	void caricaMappa() {
		String level = "src/main/resources/level1.dat";
		try {
			Scanner fileIn = new Scanner(new FileReader(level));
			fileIn.useLocale(Locale.US);
			int width = fileIn.nextInt();
			int height = fileIn.nextInt();
			
			mondo = new Mondo(width, height);
			
			int numBBoxes = fileIn.nextInt();
			for(int i = 0 ; i < numBBoxes ; i++) {
				int bWidth = fileIn.nextInt();
				int bHeight = fileIn.nextInt();
				
				double x = fileIn.nextDouble();
				double y = fileIn.nextDouble();
				
				boxes.add(new BouncyBox(bWidth, bHeight, x, y));				
			}
			
			int numDBoxes = fileIn.nextInt();
			for(int i = 0 ; i < numDBoxes ; i++) {
				int dWidth = fileIn.nextInt();
				int dHeight = fileIn.nextInt();
				
				double x = fileIn.nextDouble();
				double y = fileIn.nextDouble();
				
				boxes.add(new DestructibleBox(dWidth, dHeight, x, y));				
			}
			
			mondo.setBoxes(boxes);
			
			int nEnemies = fileIn.nextInt();
			for(int i = 0 ; i < nEnemies ; i++) {
				int cWidth = fileIn.nextInt();
				int cHeight = fileIn.nextInt();
				
				double x = fileIn.nextDouble();
				double y = fileIn.nextDouble();
				
				enemies.add(new Enemy(x, y, mondo));
			}
			
			mondo.setEnemiesList(enemies);
			
			int cWidth = fileIn.nextInt();
			int cHeight = fileIn.nextInt();
			
			double playerX = fileIn.nextDouble();
			double playerY = fileIn.nextDouble();
			
			fileIn.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch(InputMismatchException e) {
			System.out.println("Invalid map format");
			System.exit(0);
		}
	}
	
	void loadImages() {
		imgCarroPlayer = new Image("carro.png");
		imgCannonePlayer = new Image("cannone.png");
		imgNemico = new Image("nemico.png");
		imgCannoneNemico = new Image("cannone_nemico.png");
		imgBullet = new Image("bullet.png");
		imgBouncyBox = new Image("BouncyBox.png");
		imgDestructibleBox = new Image("DestructibleBox.png");
	}
	
	void disegnaBox(GraphicsContext gc, AbstractBox box, Image img) {
		for(int i = 0 ; i < box.getWidth() / AbstractBox.minWidth ; i++) {
			for(int j = 0 ; j< box.getHeight() / AbstractBox.minHeight ; j++)
				gc.drawImage(img, box.getX() + i * AbstractBox.minWidth, box.getY() + j * AbstractBox.minHeight);
		}
	}
	
	void disegnaCarro(GraphicsContext gc, CarroArmato c, Image img) {
		drawRotatedImage(gc, img, c, c.getDirection().getAngle());
	      disegnaCannone(gc, c.getCannone(), imgCannonePlayer);
	}
	
	void disegnaCannone(GraphicsContext gc, Cannon c, Image img) {
		drawRotatedImage(gc, img, c, c.getAngle());
	}
	
	void drawRotatedImage(GraphicsContext gc, Image img, Entity e, double angle) {
		gc.save();
		double pivotX = e.getX() + e.getWidth() / 2;
		double pivotY = e.getY() + e.getHeight() / 2;
		if(e instanceof Cannon)
			pivotX = e.getX() + e.getHeight() / 2;
		//if(img.equals(imgCarroPlayer))
			//System.out.println("S " + e.getX() + " " + e.getY() + " " + pivotX + " " + pivotY+ " " + angle);
		//700.0 700.0 725.0 725.0 -150.80251395393555
		
		Rotate rotate = new Rotate(angle, pivotX, pivotY);
		gc.setTransform(rotate.getMxx(), rotate.getMyx(), rotate.getMxy(), rotate.getMyy(), rotate.getTx(), rotate.getTy());
		gc.drawImage(img, e.getX(), e.getY());
		gc.restore();
	}
	
	void initGUI() {
		loadImages();
		canvas = new Canvas(mondo.getWidth(), mondo.getHeight());
		this.add(canvas, 0, 0);		
		gc = canvas.getGraphicsContext2D();
	}
	
	void initEH() {
		this.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				mondo.orientaCannone(carroPlayer, e.getX(), e.getY());				
			}
		});
		
		this.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent e) {
				if(e.getCode() == KeyCode.D) {
					right = true;
					if(left)
						left = false;
				}
				if(e.getCode() == KeyCode.A) {
					left = true;
					if(right)
						right = false;
				}
				if(e.getCode() == KeyCode.W) {
					up = true;
					if(down)
						down = false;
				}
				if(e.getCode() == KeyCode.S) {
					down = true;
					if(up)
						up = false;
				}
				if(e.getCode() == KeyCode.SPACE) {
					mondo.sparaPlayer();		
					/*
					 * if(multiplayer)
					 * 	send("USE " + ID + " BULLET");
					 */
				}
			}
		});
		
		this.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				if(e.getCode() == KeyCode.D)
					right = false;
				if(e.getCode() == KeyCode.A)
					left = false;
				if(e.getCode() == KeyCode.W)
					up = false;
				if(e.getCode() == KeyCode.S)
					down = false;				
			}
		});
	}
	
	void initTimers() {
		new Thread() {
			int count = 0;
			@Override
			public void run() {
				sendBouncyBoxes();
				while(true) {
					elabora(server.receiveCommand());
					
					count++;					

					mondo.update();

					if(enemies.isEmpty()) {
						System.out.println("Bravo, hai vinto!!!");
						System.exit(0);
					}
					
					Direction direction = null;
					if(up && right)
						direction = Direction.NE;
					else if(up && left)
						direction = Direction.NW;
					else if(down && right)
						direction = Direction.SE;
					else if(down && left)
						direction = Direction.SW;
					else if(right) {
						direction = Direction.E;
					}
					else if(left) {
						direction = Direction.W;
					}
					else if(up) {
						direction = Direction.N;
					}
					else if(down) {
						direction = Direction.S;
					}
					
					if(direction != null) {
						mondo.muoviPlayer(direction);
						/*
						 *if(multiplayer)
						 *send("MOVE " + ID + direction.toString());
						 */
					}
					
				//IL NEMICO SI MUOVE QUANDO COUNT ARRIVA A 20	
					if (count >= 10) {
							mondo.muoviNemici();
					}
								
					if(count >= 30) {
						count = 0;
						for(CarroArmato c : enemies) {
							mondo.spara(c);
							
						}
					}
					
					server.send(getMap());
					
					drawAll();
					
					try {
						sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	void elabora(String command) {
		if(command.equals("NO_CLIENTS"))
			System.exit(0);
		String[] in = command.split("\\n");
		for(String cmd : in) {
			if(!cmd.equals("null")) {
				String[] objs = cmd.split("_");
				CarroArmato player = players.get(new Integer(Integer.parseInt(objs[0])));
				if(objs[1].equals("CLOSE")) {
					players.remove(new Integer(Integer.parseInt(objs[0])));
					server.removeClient(Integer.parseInt(objs[0]));
				}
				else if(objs[1].equals("SHOOT")) {
					mondo.spara(player);
				}
				else if(objs[1].equals("ROTATE")) {
						double mouseX = Double.parseDouble(objs[2]);
						double mouseY = Double.parseDouble(objs[3]);
						
						mondo.orientaCannone(player, mouseX, mouseY);
				}
				else if(objs[1].equals("MOVE")){
						Direction d = Direction.valueOf(objs[2]);
						player.muovi(d);
				}
			}
		}
	}
	
	void drawAll() {
		// Clear the canvas
        gc.clearRect(0, 0, mondo.getWidth(), mondo.getHeight());
        gc.setFill(Color.BISQUE);
        gc.fillRect(0, 0, mondo.getWidth(), mondo.getHeight());
        
        for(AbstractBox box : mondo.getBoxes()) {
        	if(box instanceof BouncyBox)
        		disegnaBox(gc, box, imgBouncyBox);
        	else if(box instanceof DestructibleBox)
        		disegnaBox(gc, box, imgDestructibleBox);
        }
        
        for(Enemy c : enemies) {
        	disegnaCarro(gc, c, imgNemico);
        	disegnaCannone(gc, c.getCannone(), imgCannoneNemico);
        }
        
        for(CarroArmato carro : players.values())
        	disegnaCarro(gc, carro, imgCarroPlayer);
       
       gc.setFill(Color.RED);
       for(Bullet b : mondo.getBullets()) {
    	   gc.fillOval(b.getX(), b.getY(), b.getWidth(), b.getHeight());
       }
	}
	
	public void closeServer() {
		server.close();
	}
	
	String getMap() {
		String map = new String();
		
		map = map.concat(enemies.size() + "\n");
		for(Enemy enemy : enemies) {
			map = map.concat(enemy.getX() + " " + enemy.getY() + " " + 
					(enemy.getX() + enemy.getWidth() / 2.0) + " " + (enemy.getY() + enemy.getHeight() / 2.0) + " " + 
					enemy.getDirection().getAngle() +"\n");
			Cannon cannone = enemy.getCannone();
			map = map.concat(cannone.getX() + " " + cannone.getY() + " " + 
					(cannone.getX() + cannone.getHeight() / 2.0) + " " + (cannone.getY() + cannone.getHeight() / 2.0) + " " +
					cannone.getAngle() + "\n");
		}
		
		map = map.concat(players.size() + "\n");
		for(CarroArmato player : players.values()) {
			map = map.concat(player.getX() + " " + player.getY() + " " + 
					(player.getX() + player.getWidth() / 2.0) + " " + (player.getY() + player.getHeight() / 2.0) + " " + 
					player.getDirection().getAngle() +"\n");
			Cannon cannone = player.getCannone();
			map = map.concat(cannone.getX() + " " + cannone.getY() + " " + 
					(cannone.getX() + cannone.getHeight() / 2.0) + " " + (cannone.getY() + cannone.getHeight() / 2.0) + " " +
					cannone.getAngle() + "\n");
		}

		ArrayList<DestructibleBox> dBoxes = new ArrayList<>();
		for(AbstractBox box : boxes) {
			if(box instanceof DestructibleBox)
				dBoxes.add((DestructibleBox)box);
		}
		
		map = map.concat(dBoxes.size() + "\n");
		for(DestructibleBox box : dBoxes) {
			map = map.concat(box.getX() + " " + box.getY() + "\n");
		}
		
		map = map.concat(mondo.getBullets().size() + "\n");
		for(Bullet bullet : mondo.getBullets()) {
			map = map.concat(bullet.getX() + " " + bullet.getY() + " " + bullet.getWidth() + " " + bullet.getHeight() + "\n");
		}
	
		
		return map;
		
	}
	
	void sendBouncyBoxes() {
		ArrayList<BouncyBox> bBoxes = new ArrayList<>();
		for(AbstractBox box : boxes) {
			if(box instanceof BouncyBox)
				bBoxes.add((BouncyBox)box);
		}
		
		String map = bBoxes.size() + "\n";
		
		for(BouncyBox box : bBoxes)
			map = map.concat(box.getWidth() + " " + box.getHeight() + " " +  box.getX() + " " + box.getY() + "\n");
		
		server.send(map);
	}

}
