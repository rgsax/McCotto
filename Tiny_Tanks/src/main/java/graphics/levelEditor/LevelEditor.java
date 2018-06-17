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
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
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
	
	Button playerButton = new  Button();
	Button enemyButton = new Button();
	Button bouncyBoxButton = new Button();
	Button destructibleBoxButton = new Button();
	Button saveButton = new Button("SAVE");
	
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
		for(int i = 0 ; i < box.getWidth() / AbstractBox.minWidth ; i++)
			for(int j = 0 ; j < box.getHeight() / AbstractBox.minHeight ; j++)
				gc.drawImage(img, box.getX() + i * AbstractBox.minWidth, box.getY() + j * AbstractBox.minHeight);
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
				(o1.getX() + o1.getWidth() >= o2.getX() && o1.getX() <= o2.getX() + o2.getWidth()) &&
				(o1.getY() + o1.getHeight() >= o2.getY() && o1.getY() <= o2.getY() + o2.getHeight());
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
		
		cursor = imgPlayer;
		
		canvas = new Canvas(width, height);
		this.add(canvas, 1, 0);
		GridPane pane = new GridPane();
		pane.getStylesheets().add("levelEditorGraphics.css");
		playerButton.getStyleClass().add("player");
		enemyButton.getStyleClass().add("enemy");
		bouncyBoxButton.getStyleClass().add("bouncyBox");
		destructibleBoxButton.getStyleClass().add("destructibleBox");
		saveButton.getStyleClass().add("save");
		pane.getStyleClass().add("itemSelector");
		pane.setPadding(new Insets(200, 50, 100, 50));
		pane.setVgap(50);
		pane.add(playerButton, 0, 0);
		pane.add(enemyButton, 0, 1);
		pane.add(bouncyBoxButton, 0, 2);
		pane.add(destructibleBoxButton, 0, 3);
		pane.add(saveButton, 0, 7);
		this.add(pane, 0, 0);
		gc = canvas.getGraphicsContext2D();
	}
	
	public void initEH() {
		saveButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				createLevelFile();
			}
		});
		
		playerButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				cursor = imgPlayer;
			}
		});
		
		enemyButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				cursor = imgEnemy;
			}
		});
		
		bouncyBoxButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				cursor = imgBouncyBox;
			}
		});
		
		destructibleBoxButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				cursor = imgDestructibleBox;
			}
		});
		
		this.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				if(event.getDeltaY() >= 0)
					cursor = selector.nextItem();
				else
					cursor = selector.previousItem();
			}
		});
		
		canvas.setOnMouseMoved(new EventHandler<MouseEvent>() {
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
	
	void createLevelFile() {
		if(player == null)
			System.out.println("Player must be placed");
		else {
			File level = new File("src/main/resources/level1.dat");
			try {
				level.createNewFile();
				PrintWriter levelOut = new PrintWriter(level);
				
				levelOut.println(width + " " + height);
				
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
	
	void initTimers() {
		new AnimationTimer() {			
			@Override
			public void handle(long now) {
				gc.clearRect(0,  0, width, height);
				gc.setFill(Color.BISQUE);
                gc.fillRect(0, 0, width, height);
				
				for(ObjectInfo bBox : bouncyBoxes) {
					drawBox(gc, bBox, imgBouncyBox);
				}
				
				for(ObjectInfo dBox : destructibleBoxes) {
					gc.drawImage(imgDestructibleBox, dBox.getX(), dBox.getY());
				}
				
				for(ObjectInfo enemy : enemies) {
					gc.drawImage(imgEnemy, enemy.getX(), enemy.getY());
				}
				
				if(player != null)
					gc.drawImage(imgPlayer, player.getX(), player.getY());
				
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