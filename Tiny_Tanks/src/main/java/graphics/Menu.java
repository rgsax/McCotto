package graphics;

import graphics.levelEditor.LevelEditor;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class Menu extends GridPane {
	Button playButton = new Button("PLAY");
	Button levelEditorButton = new Button("LEVEL EDITOR");
	Button multiplayerButton = new Button("MULTIPLAYER");
	Text title = new Text("Tiny Tanks");
	Spinner<Integer> spinner = new Spinner<>(2, 10, 2);
	WindowManager windowManager;
	
	public Menu(WindowManager windowManager) {
		this.windowManager = windowManager;
		
		initGUI();
		initEH();		
	}
	
	void initGUI() {
		this.setMinSize(800,  800);
		this.setPadding(new Insets(100, 100, 100, 100));
		this.setVgap(50);
		this.add(title, 0, 0);
		this.add(playButton, 0, 1);
		this.add(multiplayerButton, 0, 3);
		this.add(levelEditorButton, 0, 5);
		this.add(spinner, 1, 3);
		this.setStyle("-fx-background-color: purple;");
		this.getStylesheets().add("file.css");
		playButton.getStyleClass().add("menuButton");
		multiplayerButton.getStyleClass().add("menuButton");
		levelEditorButton.getStyleClass().add("menuButton");
		title.getStyleClass().add("title");
	}
	
	void initEH() {
		playButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				windowManager.startGame(1);
				windowManager.scene = new Scene(new Client("127.0.0.1", 8182));
				windowManager.initEH();
				windowManager.stage.setScene(windowManager.scene);
			}
		});
		
		multiplayerButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				windowManager.startGame(spinner.getValue());
				windowManager.scene = new Scene(new Client("192.168.0.101", 8182));
				windowManager.initEH();
				windowManager.stage.setScene(windowManager.scene);
			}
		});
		
		levelEditorButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				windowManager.scene = new Scene(new LevelEditor());
				windowManager.initEH();
				windowManager.stage.setScene(windowManager.scene);
			}
		});

		playButton.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				playButton.requestFocus();				
			}
		});
		
		multiplayerButton.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				multiplayerButton.requestFocus();				
			}
		});
		
		levelEditorButton.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				levelEditorButton.requestFocus();				
			}
		});
	}
}
