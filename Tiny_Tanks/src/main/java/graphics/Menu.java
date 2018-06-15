package graphics;

import graphics.levelEditor.LevelEditor;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class Menu extends GridPane {
	Button playButton = new Button("PLAY");
	Button levelEditorButton = new Button("LEVEL EDITOR");
	Button hostButton = new Button("HOST GAME");
	Button multiplayerButton = new Button("MULTIPLAYER");
	CheckBox useDefaultIP = new CheckBox("use default IP");
	TextField ipField = new TextField(defaultIP);
	Text title = new Text("Tiny Tanks");
	Spinner<Integer> spinner = new Spinner<>(2, 10, 2);
	WindowManager windowManager;
	static String defaultIP = "192.168.0.100";
	
	public Menu(WindowManager windowManager) {
		this.windowManager = windowManager;
		
		initGUI();
		initEH();		
	}
	
	void initGUI() {
		this.setMinSize(800,  800);
		this.setPadding(new Insets(100, 100, 100, 100));
		this.setVgap(50);
		this.setHgap(50);
		this.add(title, 0, 0);
		this.add(playButton, 0, 1);
		this.add(hostButton, 0, 3);
		this.add(ipField, 0, 4);
		this.add(useDefaultIP, 1, 4);
		this.add(multiplayerButton, 0, 5);
		this.add(levelEditorButton, 0, 7);
		this.add(spinner, 1, 3);
		this.setStyle("-fx-background-color: purple;");
		this.getStylesheets().add("file.css");
		playButton.getStyleClass().add("menuButton");
		multiplayerButton.getStyleClass().add("menuButton");
		hostButton.getStyleClass().add("menuButton");
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
		
		hostButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				windowManager.startGame(spinner.getValue());
				windowManager.scene = new Scene(new Client(defaultIP, 8182));
				windowManager.initEH();
				windowManager.stage.setScene(windowManager.scene);
			}
		});
		
		multiplayerButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				windowManager.scene = new Scene(new Client(defaultIP, 8182));
				windowManager.initEH();
				windowManager.stage.setScene(windowManager.scene);
			}
		});
		
		useDefaultIP.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(useDefaultIP.isSelected()) {
					ipField.setDisable(true);
					ipField.setText(defaultIP);
				}
				else {
					ipField.setDisable(false);
				}
					
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
		
		hostButton.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				hostButton.requestFocus();				
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
