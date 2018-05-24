package graphics.FXgraphics;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import core.entities.AbstractBox;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LevelEditor extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	double mouseX, mouseY;
	Image cursor;
	Image imgEnemy;
	Image imgBouncyBox;
	Image imgDestructibleBox;
	Image imgPlayer;
	
	boolean ctrl = false, S = false;
	
	ArrayList<ObjectInfo> bouncyBoxes = new ArrayList<>();
	ArrayList<ObjectInfo> destructibleBoxes = new ArrayList<>();
	ArrayList<ObjectInfo> enemies = new ArrayList<>();
	ObjectInfo player = null;
	int currentBoxWidth = AbstractBox.minWidth;
	int currentBoxHeight = AbstractBox.minHeight;
	

	@Override
	public void start(Stage stage) throws Exception {
		loadImages();
		GridPane root = new GridPane();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		Canvas canvas = new Canvas(800, 800);
		root.getChildren().add(canvas);
		
		scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				mouseX = event.getX();
				mouseY = event.getY();
			}
		});
		
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
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
					File level = new File("levels/level1.dat");
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
		
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
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
						bouncyBoxes.add(new ObjectInfo(currentBoxWidth, currentBoxHeight, mouseX, mouseY));
					}
					else if(cursor == imgDestructibleBox) {
						destructibleBoxes.add(new ObjectInfo(currentBoxWidth, currentBoxHeight, mouseX, mouseY));
					}
					else if(cursor == imgEnemy) {
						enemies.add(new ObjectInfo(mouseX, mouseY));						
					}
					else if(cursor == imgPlayer) {
						player = new ObjectInfo(mouseX, mouseY);
					}
				}
			}
		});
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		new AnimationTimer() {
			
			@Override
			public void handle(long now) {
				gc.clearRect(0,  0, 800, 800);
				
				for(ObjectInfo bBox : bouncyBoxes) {
					drawBox(gc, bBox, imgBouncyBox);
				}
				
				for(ObjectInfo dBox : destructibleBoxes) {
					drawBox(gc, dBox, imgDestructibleBox);
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
		
		stage.show();
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

}


class ObjectInfo {
	Integer width = null, height = null;
	Double x, y;
	public ObjectInfo(double x, double y) {
		this.x = new Double(x);
		this.y = new Double(y);
	}
	
	public ObjectInfo(int width, int height, double x, double y) {
		this.width = new Integer(width);
		this.height = new Integer(height);
		this.x = new Double(x);
		this.y = new Double(y);
	}
	
	@Override
	public String toString() {
		String dims = "";
		if(width != null)
			dims = width.toString() + " " + height.toString() + " ";
		return dims + x.toString() + " " + y.toString();
	}
}