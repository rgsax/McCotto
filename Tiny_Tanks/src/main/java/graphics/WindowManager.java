package graphics;

import graphics.levelEditor.LevelEditor;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class WindowManager extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	boolean ctrl = false, R = false, L = false;
	Stage stage;
	Scene scene;
	
	ServerGame game = null;
	
	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		initGUI();
		initEH();
		
		stage.show();
	}
	
	void initGUI() {
		stage.setTitle("CLIENT");
		scene = new Scene(new Menu(this));
		stage.setScene(scene);
	}
	
	void initEH() {
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {			
			@Override
			public void handle(WindowEvent event) {
				if(scene.getRoot() instanceof Client) {
					((Client)scene.getRoot()).sendCloseRequest();
				}
				System.exit(0);
			}
		});
		
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				@SuppressWarnings("unchecked")
				EventHandler<KeyEvent> paneEventHandler = (EventHandler<KeyEvent>) scene.getRoot().getOnKeyPressed();
				if(paneEventHandler != null)
					paneEventHandler.handle(event);
				if(event.getCode() == KeyCode.ESCAPE) {
					if(game != null) {
						game.closeServer();
					}
					scene = new Scene(new Menu(WindowManager.this));
					initEH();
					stage.setScene(scene);
				}			
			}
		});
		
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				@SuppressWarnings("unchecked")
				EventHandler<KeyEvent> paneEventHandler = (EventHandler<KeyEvent>) scene.getRoot().getOnKeyReleased();
				if(paneEventHandler != null)
					paneEventHandler.handle(event);
			}
		});
	}
	
	public void startGame(int numPlayers) {
		game = new ServerGame(numPlayers);
		game.start();
	}

}
