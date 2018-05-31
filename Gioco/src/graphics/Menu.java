package graphics;

import graphics.FXgraphics.GameWindow;
import graphics.levelEditor.LevelEditor;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class Menu extends GridPane {
	Button playButton = new Button("PLAY");
	Button levelEditorButton = new Button("LEVEL EDITOR");
	WindowManager windowManager;
	
	public Menu(WindowManager windowManager) {
		this.setMinSize(800,  800);
		this.setPadding(new Insets(100, 100, 100, 100));
		this.setVgap(50);
		this.windowManager = windowManager;
		this.add(playButton, 0, 0);
		this.add(levelEditorButton, 0, 2);
		this.setStyle("-fx-background-color: purple;");
		

		playButton.getStyleClass().add("menuButton");
		levelEditorButton.getStyleClass().add("menuButton");
		this.getStylesheets().add("file.css");
		
		playButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				windowManager.scene = new Scene(new GameWindow());
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
	}
}
