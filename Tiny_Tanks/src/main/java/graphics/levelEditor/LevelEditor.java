package graphics.levelEditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import core.entities.AbstractBox;
import core.entities.CarroArmato;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class LevelEditor extends GridPane {
	
	/*public static void main(String[] args) {
		launch(args);
	}*/
	
	int width, height;
	double mouseX, mouseY;
	Image cursor;
	Image imgEnemy;
	Image imgBouncyBox;
	Image imgDestructibleBox;
	Image imgPlayer;
	
	Canvas canvas;
	GraphicsContext gc;
	
	ObjectSelector selector;
	
	boolean ctrl = false, S = false;
	
	ArrayList<ObjectInfo> bouncyBoxes = new ArrayList<>();
	ArrayList<ObjectInfo> destructibleBoxes = new ArrayList<>();
	ArrayList<ObjectInfo> enemies = new ArrayList<>();
	ObjectInfo player;
	int currentBoxWidth = AbstractBox.minWidth;
	int currentBoxHeight = AbstractBox.minHeight;
	

	public LevelEditor() {
		super();
		
		loadTemplate();
		initGUI();
		
		initEH();		
		initTimers();
	}
	
	
	void loadImages() {
		imgBouncyBox = new Image("BouncyBox.png");
		imgDestructibleBox = new Image("DestructibleBox.png");
		imgEnemy = new Image("nemico.png");
		imgPlayer = new Image("carro.png");
		cursor = null;
	}
	
	void drawBox(GraphicsContext gc, ObjectInfo box, Image img) {
		for(int i = 0 ; i < box.width / AbstractBox.minWidth ; i++)
			for(int j = 0 ; j < box.height / AbstractBox.minHeight ; j++)
				gc.drawImage(img, box.x + i * AbstractBox.minWidth, box.y + j * AbstractBox.minHeight);
	}
	
	boolean findCollision(ObjectInfo o) {
		for(ObjectInfo bBox : bouncyBoxes) {
			if(collided(o, bBox))
				return true;
		}
		
		for(ObjectInfo dBox : destructibleBoxes) {
			if(collided(o, dBox))
				return true;
		}
		
		for(ObjectInfo enemy : enemies) {
			if(collided(o, enemy))
				return true;
		}
		
		if(player != null && collided(o, player))
			return true;
		
		return false;
	}
	
	boolean collided(ObjectInfo o1, ObjectInfo o2) {
		return !o1.equals(o2) && 
				(o1.x + o1.width >= o2.x && o1.x <= o2.x + o2.width) &&
				(o1.y + o1.height >= o2.y && o1.y <= o2.y + o2.height);
	}
	
	void loadTemplate() {
		String level = "src/main/resources/template.dat";
		System.out.println(level);
		try {
			Scanner fileIn = new Scanner(new FileReader(level));
			fileIn.useLocale(Locale.US);
			width = fileIn.nextInt();
			height = fileIn.nextInt();
			
			int numBBoxes = fileIn.nextInt();
			for(int i = 0 ; i < numBBoxes ; i++) {
				int bWidth = fileIn.nextInt();
				int bHeight = fileIn.nextInt();
				
				double x = fileIn.nextDouble();
				double y = fileIn.nextDouble();
				
				bouncyBoxes.add(new ObjectInfo(bWidth, bHeight, x, y));				
			}
			
			int numDBoxes = fileIn.nextInt();
			for(int i = 0 ; i < numDBoxes ; i++) {
				int dWidth = fileIn.nextInt();
				int dHeight = fileIn.nextInt();
				
				double x = fileIn.nextDouble();
				double y = fileIn.nextDouble();
				
				destructibleBoxes.add(new ObjectInfo(dWidth, dHeight, x, y));				
			}
			
			int nEnemies = fileIn.nextInt();
			for(int i = 0 ; i < nEnemies ; i++) {
				double x = fileIn.nextDouble();
				double y = fileIn.nextDouble();
				
				enemies.add(new ObjectInfo(CarroArmato.baseWidth, CarroArmato.baseHeight, x, y));
			}
			
			double playerX = fileIn.nextDouble();
			double playerY = fileIn.nextDouble();
			
			player = new ObjectInfo(CarroArmato.baseWidth, CarroArmato.baseHeight, playerX, playerY);
			fileIn.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	void initGUI() {
		loadImages();
		Image[] imgs = {	
				imgEnemy,
				imgBouncyBox,
				imgDestructibleBox,
				imgPlayer
		};
		selector = new ObjectSelector(imgs);
		
		canvas = new Canvas(width, height);
		this.getChildren().add(canvas);
		
		gc = canvas.getGraphicsContext2D();
	}
	
	public void initEH() {
		this.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				if(event.getDeltaY() >= 0)
					cursor = selector.nextItem();
				else
					cursor = selector.previousItem();
			}
		});
		
		this.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				mouseX = event.getX();
				mouseY = event.getY();
			}
		});
		
		this.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {				
				if(event.getCode() == KeyCode.B) {
					cursor = imgBouncyBox;
				}
				else if(event.getCode() == KeyCode.D) {
					cursor = imgDestructibleBox;
				}
				else if(event.getCode() == KeyCode.P) {
					cursor = imgPlayer;
				}
				else if(event.getCode() == KeyCode.E) {
					cursor = imgEnemy;
				}
				else if(event.getCode() == KeyCode.CONTROL)
					ctrl = true;
				else if(event.getCode() == KeyCode.S)
					S = true;
				else if(event.getCode() == KeyCode.UP &&
						(cursor == imgBouncyBox || cursor == imgDestructibleBox)) {
					if(currentBoxHeight > AbstractBox.minHeight)
						currentBoxHeight -= AbstractBox.minHeight;
				}
				else if(event.getCode() == KeyCode.DOWN &&
						(cursor == imgBouncyBox || cursor == imgDestructibleBox)) {
					currentBoxHeight += AbstractBox.minHeight;
				}
				else if(event.getCode() == KeyCode.LEFT &&
						(cursor == imgBouncyBox || cursor == imgDestructibleBox)) {
					if(currentBoxWidth > AbstractBox.minWidth)
						currentBoxWidth -= AbstractBox.minWidth;
				}
				else if(event.getCode() == KeyCode.RIGHT &&
						(cursor == imgBouncyBox || cursor == imgDestructibleBox)) {
					currentBoxWidth += AbstractBox.minWidth;
				}
				else if(event.getCode() == KeyCode.R) {
					currentBoxHeight = AbstractBox.minHeight;
					currentBoxWidth = AbstractBox.minWidth;
				}
				
				if(ctrl && S) {
					createLevelFile();					
				}
				
			}

			void createLevelFile() {
				if(player == null)
					System.out.println("Player must be placed");
				else {
					File level = new File("src/main/resources/level1.dat");
					try {
						level.createNewFile();
						PrintWriter levelOut = new PrintWriter(level);
						
						levelOut.println("800 800");
						
						levelOut.println(bouncyBoxes.size());
						for(ObjectInfo bBox : bouncyBoxes)
							levelOut.println(bBox.toString());
						
						levelOut.println(destructibleBoxes.size());
						for(ObjectInfo dBox : destructibleBoxes)
							levelOut.println(dBox.toString());
						
						levelOut.println(enemies.size());
						for(ObjectInfo enemy : enemies) 
							levelOut.println(enemy.toString());
						
						
						levelOut.println("\n" + player.toString());
						
						levelOut.close();
						
						System.out.println("file salvato con successo");
					} catch (IOException e) {
						e.printStackTrace();
					}				
				}
			}
		});
		
		this.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.CONTROL)
					ctrl = false;
				else if(event.getCode() == KeyCode.S)
					S = false;
				
			}
		});
		
		canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(cursor != null) {
					if(cursor == imgBouncyBox) {
						ObjectInfo o = new ObjectInfo(currentBoxWidth, currentBoxHeight, mouseX, mouseY);
						if(!findCollision(o))
							bouncyBoxes.add(o);
					}
					else if(cursor == imgDestructibleBox) {
						ObjectInfo o = new ObjectInfo(currentBoxWidth, currentBoxHeight, mouseX, mouseY);
						if(!findCollision(o)) {
							for(int i = 0 ; i < currentBoxWidth / AbstractBox.minWidth ; i++)
								for(int j = 0 ; j < currentBoxHeight / AbstractBox.minHeight ; j++)
									destructibleBoxes.add(new ObjectInfo(AbstractBox.minWidth, AbstractBox.minHeight, mouseX + i * AbstractBox.minWidth, mouseY + j * AbstractBox.minHeight));
						}
					}
					else if(cursor == imgEnemy) {
						ObjectInfo o = new ObjectInfo(CarroArmato.baseWidth, CarroArmato.baseHeight, mouseX, mouseY);
						if(!findCollision(o))
							enemies.add(o);						
					}
					else if(cursor == imgPlayer) {
						ObjectInfo o = new ObjectInfo(CarroArmato.baseWidth, CarroArmato.baseHeight, mouseX, mouseY);
						if(!findCollision(o))
							player = o;
					}
				}
			}
		});
	}
	
	void initTimers() {
		new AnimationTimer() {			
			@Override
			public void handle(long now) {
				gc.clearRect(0,  0, 800, 800);
				gc.setFill(Color.BISQUE);
                gc.fillRect(0, 0, 800, 800);
				
				for(ObjectInfo bBox : bouncyBoxes) {
					drawBox(gc, bBox, imgBouncyBox);
				}
				
				for(ObjectInfo dBox : destructibleBoxes) {
					gc.drawImage(imgDestructibleBox, dBox.x, dBox.y);
				}
				
				for(ObjectInfo enemy : enemies) {
					gc.drawImage(imgEnemy, enemy.x, enemy.y);
				}
				
				if(player != null)
					gc.drawImage(imgPlayer, player.x, player.y);
				
				if(cursor != null) {
					if(cursor == imgBouncyBox || cursor == imgDestructibleBox) {
						drawBox(gc, new ObjectInfo(currentBoxWidth, currentBoxHeight, mouseX, mouseY), cursor);
					}
					else
						gc.drawImage(cursor, mouseX, mouseY);
				}
				
			}
		}.start();
	}
}