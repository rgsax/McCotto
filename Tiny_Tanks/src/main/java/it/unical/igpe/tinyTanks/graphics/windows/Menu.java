package it.unical.igpe.tinyTanks.graphics.windows;

import java.io.File;
import java.util.ArrayList;

import it.unical.igpe.tinyTanks.graphics.WindowManager;
import it.unical.igpe.tinyTanks.graphics.levelEditor.LevelEditor;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class Menu extends GridPane {
	Button playButton = new Button("PLAY");
	Button levelEditorButton = new Button("LEVEL EDITOR");
	Button multiplayerButton = new Button("MULTIPLAYER");
	Text title = new Text("Tiny Tanks");
	ComboBox<String> plugins = new ComboBox<>();
	WindowManager windowManager;
	
	public Menu(WindowManager windowManager) {
		this.windowManager = windowManager;
		
		populatePluginList();
		
		initGUI();
		initEH();		
	}
	
	void initGUI() {
		title.getStyleClass().add("title");
		plugins.setValue(WindowManager.NO_PLUGINS);
		plugins.setStyle("-fx-focus-color: transparent;	-fx-faint-focus-color: transparent; -fx-font-size: 20pt");
		
		this.setMinSize(800,  800);
		this.setPadding(new Insets(100, 100, 100, 100));
		this.setVgap(50);
		this.setHgap(50);
		this.add(title, 0, 0);
		this.add(playButton, 0, 1);
		GridPane pane = new GridPane();
		Text pluginTitle = new Text("PLUGIN");
		pluginTitle.setStyle("-fx-font-size: 20pt;");
		pane.add(pluginTitle, 0, 0);
		pane.add(plugins, 0, 1);
		this.add(pane, 1, 1);
		this.add(multiplayerButton, 0, 3);
		this.add(levelEditorButton, 0, 5);
		this.getStyleClass().add("menu");
		this.getStylesheets().add("menuGraphics.css");
		playButton.getStyleClass().add("menuButton");
		multiplayerButton.getStyleClass().add("menuButton");
		levelEditorButton.getStyleClass().add("menuButton");
	}
	
	void initEH() {
		plugins.setOnAction(event -> windowManager.setCurrentPlugin(plugins.getValue()));
		
		playButton.setOnMouseClicked(event -> {
			windowManager.goToScene(new LevelWindow(windowManager));
		});
		
		multiplayerButton.setOnMouseClicked(event -> windowManager.goToScene(new MultiplayerWindow(windowManager)));
		
		levelEditorButton.setOnMouseClicked(event -> windowManager.goToScene(new LevelEditor()));

		playButton.setOnMouseMoved(event -> playButton.requestFocus());
		
		multiplayerButton.setOnMouseMoved(event -> multiplayerButton.requestFocus());
		
		levelEditorButton.setOnMouseMoved(event -> levelEditorButton.requestFocus());
	}
	
	void populatePluginList() {
		String[] fileNames = new File("src/main/resources").list();
		
		ArrayList<String> elems = new ArrayList<>();
		elems.add(WindowManager.NO_PLUGINS);
		
		for(String file : fileNames)
			if(file.matches(".+Plugin\\.class"))
				elems.add(file.substring(0, file.lastIndexOf('.')));
				
		plugins.setItems(FXCollections.observableArrayList(elems));
	}
	
}
