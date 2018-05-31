package graphics.FXgraphics;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
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
import javafx.animation.AnimationTimer;
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
	Mondo mondo;
	
	Canvas canvas;
	GraphicsContext gc;
	
	public GameWindow() {
		super();
		caricaMappa();
		
		initGUI();
		initEH();
		initTimers();
	}
	

	@SuppressWarnings("unused")
	void caricaMappa() {
		String level = "levels/level1.dat";
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
			
			carroPlayer = new CarroArmato(playerX, playerY, mondo);
			mondo.setPlayer(carroPlayer);
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
				while(true) {
					count++;					

					mondo.update();
					
					if(enemies.isEmpty()) {
						System.out.println("Bravo, hai vinto!!!");
						System.exit(0 );
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
					
					try {
						sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		
		new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
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
                
               disegnaCarro(gc, carroPlayer, imgCarroPlayer);
               disegnaCannone(gc, carroPlayer.getCannone(), imgCannonePlayer);
               
               gc.setFill(Color.RED);
               for(Bullet b : mondo.getBullets()) {
            	   gc.fillOval(b.getX(), b.getY(), b.getWidth(), b.getHeight());
               }
            }
        }.start();
	}

}
