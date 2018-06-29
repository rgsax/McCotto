package it.unical.igpe.tinyTanks.graphics;

import it.unical.igpe.tinyTanks.graphics.game.Client;
import it.unical.igpe.tinyTanks.graphics.game.ServerGame;
import it.unical.igpe.tinyTanks.graphics.windows.Menu;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class WindowManager extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	boolean ctrl = false, R = false, L = false;
	Stage stage;
	Scene scene;
	String currentLevel;
	
	public static final String NO_PLUGINS = "none";
	
	String currentPlugin = NO_PLUGINS;
	
	ServerGame game = null;
	
	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		initGUI();
		initEH();
		
		stage.show();
	}
	
	void initGUI() {
		Font.loadFont(this.getClass().getResource("/Colleged.ttf").toExternalForm(), 30);
		Font.loadFont(this.getClass().getResource("/armalite.ttf").toExternalForm(), 30);
		stage.setTitle("CLIENT");
		scene = new Scene(new Menu(this));
		stage.setScene(scene);
	}
	
	void initEH() {
		stage.setOnCloseRequest(event -> {
			if(scene.getRoot() instanceof Client) {
				((Client)scene.getRoot()).sendCloseRequest();
			}
			System.exit(0);
		});
		
		scene.setOnKeyPressed(event -> {
			@SuppressWarnings("unchecked")
			EventHandler<KeyEvent> paneEventHandler = (EventHandler<KeyEvent>) scene.getRoot().getOnKeyPressed();
			if(paneEventHandler != null)
				paneEventHandler.handle(event);
			if(event.getCode() == KeyCode.ESCAPE) {
				if(scene.getRoot() instanceof Client) {
					game.closeServer();
				}
				backToMenu();
			}			
		});
		
		scene.setOnKeyReleased(event -> {
			@SuppressWarnings("unchecked")
			EventHandler<KeyEvent> paneEventHandler = (EventHandler<KeyEvent>) scene.getRoot().getOnKeyReleased();
			if(paneEventHandler != null)
				paneEventHandler.handle(event);
		});
	}
	
	public void startGame(int numPlayers, String level) {
		setCurrentLevel(level);
		
		game = new ServerGame(numPlayers, level, currentPlugin);
		game.start();
	}

	public void backToMenu() {
		scene = new Scene(new Menu(this));
		initEH();
		stage.setScene(scene);
	}
	
	public void goToScene(Parent window) {
		scene = new Scene(window);
		initEH();
		stage.setScene(scene);
	}

	public String getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(String currentLevel) {
		this.currentLevel = currentLevel;
	}

	public void setCurrentPlugin(String plugin) {
		currentPlugin = plugin;
	}

}
