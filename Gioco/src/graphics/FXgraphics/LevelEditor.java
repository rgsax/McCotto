package graphics.FXgraphics;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

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
				
				if(ctrl && S) {
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
						bouncyBoxes.add(new ObjectInfo(40, 40, mouseX, mouseY));
					}
					else if(cursor == imgDestructibleBox) {
						destructibleBoxes.add(new ObjectInfo(40, 40, mouseX, mouseY));
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
					gc.drawImage(imgBouncyBox, bBox.x, bBox.y);
				}
				
				for(ObjectInfo dBox : destructibleBoxes) {
					gc.drawImage(imgDestructibleBox, dBox.x, dBox.y);
				}
				
				for(ObjectInfo enemy : enemies) {
					gc.drawImage(imgEnemy, enemy.x, enemy.y);
				}
				
				if(player != null)
					gc.drawImage(imgPlayer, player.x, player.y);
				
				if(cursor != null)
					gc.drawImage(cursor, mouseX, mouseY);
				
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