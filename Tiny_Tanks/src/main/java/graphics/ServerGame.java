package graphics;

import graphics.FXgraphics.GameWindow;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ServerGame extends Application{
	
	GameWindow server = new GameWindow();
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Scene scene = new Scene(server);
		primaryStage.setScene(scene);
		primaryStage.setTitle("SERVER");
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {			
			@Override
			public void handle(WindowEvent event) {
				System.exit(0);
			}
		});
		
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				scene.getRoot().getOnKeyPressed().handle(event);			
			}
		});
		
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				scene.getRoot().getOnKeyReleased().handle(event);			
			}
		});
		
		primaryStage.show();
	}
}
