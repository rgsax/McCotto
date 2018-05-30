package graphics;

import graphics.FXgraphics.GameWindow;
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
	
	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		initGUI();
		initEH();
		
		stage.show();
	}
	
	void initGUI() {
		scene = new Scene(new LevelEditor());
		stage.setScene(scene);
	}
	
	void initEH() {
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {			
			@Override
			public void handle(WindowEvent event) {
				System.exit(0);				
			}
		});
		
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				scene.getRoot().getOnKeyPressed().handle(event);
				if(event.getCode() == KeyCode.CONTROL)
					ctrl = true;
				else if(event.getCode() == KeyCode.R)
					R = true;
				else if(event.getCode() == KeyCode.L)
					L = true;
				
				if(ctrl && R) {
					scene = new Scene(new GameWindow());
					initEH();
					stage.setScene(scene);
				}
				else if(ctrl && L) {
					scene = new Scene(new LevelEditor());
					initEH();
					stage.setScene(scene);
				}				
			}
		});
		
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				scene.getRoot().getOnKeyReleased().handle(event);
				if(event.getCode() == KeyCode.CONTROL)
					ctrl = false;
				else if(event.getCode() == KeyCode.R)
					R = false;
				else if(event.getCode() == KeyCode.L)
					L = false;
			}
		});
	}

}
