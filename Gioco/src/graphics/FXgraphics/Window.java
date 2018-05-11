package graphics.FXgraphics;

import java.util.ArrayList;

import core.Mondo;
import core.entities.Bullet;
import core.entities.CarroArmato;
import core.entities.Enemy;
import core.entities.Entity;
import core.parts.Cannon;
import core.parts.Direction;
import javafx.animation.AnimationTimer;
import javafx.application.*;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Window extends Application{
	
	public static void main(String[] args) {
		launch(args);
	}

	boolean up = false, down = false, left = false, right = false;
	Image imgCarroPlayer;
	Image imgCannonePlayer;
	Image imgNemico;
	Image imgCannoneNemico;
	Image imgBullet;
	
	CarroArmato carroPlayer;
	ArrayList<Enemy> enemies = new ArrayList<>();
	Mondo mondo;
	
	public Window() {
		mondo = new Mondo();
		carroPlayer = new CarroArmato(300, 300, mondo);
		mondo.setPlayer(carroPlayer);
		mondo.setEnemiesList(enemies);
		Enemy enemy = new Enemy(100, 100, mondo);
		enemies.add(enemy);
		mondo.orientaCannone(enemy, carroPlayer);
	}
	
	void loadImages() {
		imgCarroPlayer = new Image("carro.png");
		imgCannonePlayer = new Image("cannone.png");
		imgNemico = new Image("nemico.png");
		imgCannoneNemico = new Image("cannone_nemico.png");
		imgBullet = new Image("bullet.png");
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		GridPane root = new GridPane();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		
		scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				mondo.orientaCannone(carroPlayer, e.getX(), e.getY());				
			}
		});
		
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

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
				if(e.getCode() == KeyCode.SPACE)
					mondo.spara(carroPlayer);				
			}
		});
		
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
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
		

		Canvas canvas = new Canvas(800, 800);
		root.add(canvas, 0, 0);
		
		loadImages();
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		new Thread() {
			int count = 0;
			@Override
			public void run() {
				while(true) {
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
					
					
					try {
						sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
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
                gc.clearRect(0, 0, 800, 800);
                gc.setFill(Color.BISQUE);
                gc.fillRect(0, 0, mondo.getWidth(), mondo.getHeight());
                
                for(Enemy c : enemies) {
                	disegnaCarro(gc, c, imgNemico);
                	disegnaCannone(gc, c.getCannone(), imgCannoneNemico);
                }
                
               disegnaCarro(gc, carroPlayer, imgCarroPlayer);
               disegnaCannone(gc, carroPlayer.getCannone(), imgCannonePlayer);
               
               for(Bullet b : mondo.getBullets()) {
            	   drawRotatedImage(gc, imgBullet, b, b.getAngle());
               }
            }
        }.start();
        
        stage.show();
	}
	
	void disegnaCarro(GraphicsContext gc, CarroArmato c, Image img) {
		drawRotatedImage(gc, img, c, c.getDirection().getAngle());
	}
	
	void disegnaCannone(GraphicsContext gc, Cannon c, Image img) {
		drawRotatedImage(gc, img, c, c.getAngle());
	}
	
	void drawRotatedImage(GraphicsContext gc, Image img, Entity e, double angle) {
		gc.save();
		Rotate rotate = new Rotate(angle, e.getX() + e.getWidth() / 2, e.getY() + e.getHeight() / 2);
		gc.setTransform(rotate.getMxx(), rotate.getMyx(), rotate.getMxy(), rotate.getMyy(), rotate.getTx(), rotate.getTy());
		gc.drawImage(img, e.getX(), e.getY());
		gc.restore();
	}

}
