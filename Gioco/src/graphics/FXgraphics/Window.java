package graphics.FXgraphics;

import java.util.ArrayList;

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
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
	Image imgBouncyBox;
	Image imgDestructibleBox;
	
	CarroArmato carroPlayer;
	ArrayList<Enemy> enemies = new ArrayList<>();
	//ArrayList<AbstractBox> boxes = new ArrayList<>();
	Mondo mondo;
	
	public Window() {
		mondo = new Mondo();
		carroPlayer = new CarroArmato(300, 300, mondo);
		mondo.setPlayer(carroPlayer);
		mondo.setEnemiesList(enemies);
		Enemy enemy = new Enemy(600, 600, mondo);
		//enemies.add(enemy);
		
		BouncyBox box = new BouncyBox(40, 40, 200, 200);
		DestructibleBox box2 = new DestructibleBox(40, 40, 100,100);
		mondo.addBox(box);
		mondo.addBox(box2);
		
		//mondo.setBoxes(boxes);
		
		mondo.orientaCannone(enemy, carroPlayer);
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
				if(e.getCode() == KeyCode.SPACE) {
					mondo.sparaPlayer();		
					/*
					 * if(multiplayer)
					 * 	send("USE " + ID + " BULLET");
					 */
				}
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
		
		scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(event.getButton() == MouseButton.PRIMARY)	{
					mondo.addBox(new DestructibleBox(40, 40, event.getX() - 20, event.getY() - 20));
				}
				else if(event.getButton() == MouseButton.SECONDARY) {
					mondo.addBox(new BouncyBox(40, 40, event.getX() - 20, event.getY() - 20));
				}
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

					mondo.update();
					
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
                gc.clearRect(0, 0, 800, 800);
                gc.setFill(Color.BISQUE);
                gc.fillRect(0, 0, mondo.getWidth(), mondo.getHeight());
                
                for(AbstractBox box : mondo.getBoxes()) {
                	if(box instanceof BouncyBox)
                		gc.drawImage(imgBouncyBox, box.getX(), box.getY());
                	else if(box instanceof DestructibleBox)
                		gc.drawImage(imgDestructibleBox, box.getX(), box.getY());
                }
                
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
        
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
        	public void handle(WindowEvent event) {
        		System.exit(0);
        	};
		});
        
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
