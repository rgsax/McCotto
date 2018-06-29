package it.unical.igpe.tinyTanks.graphics.windows;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import it.unical.igpe.tinyTanks.graphics.WindowManager;
import it.unical.igpe.tinyTanks.graphics.game.Client;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class LevelWindow extends GridPane{
	WindowManager windowManager;
	public static final int numLevels = 5;
	int unlockedLevels;
	Button[] levels = new Button[numLevels];
	Button playButton = new Button("PLAY");
	ListView<String> customLevels = new ListView<>();
	
	public LevelWindow(WindowManager windowManager) {
		this.windowManager = windowManager;
		populateLevelList();
		
		loadSettings();
		
		initGUI();
		initEH();
	}
	
	void populateLevelList() {
		String[] fileNames = new File("src/main/resources").list();
		
		ArrayList<String> elems = new ArrayList<>();
		
		for(String file : fileNames)
			if(file.matches("\\d+-.+.\\.level") || file.matches("custom-.+.\\.level"))
				elems.add(file.substring(0, file.lastIndexOf('.')));
				
		customLevels.setItems(FXCollections.observableArrayList(elems));
	}
	
	void loadSettings() {
		Scanner in = null;
		try {
			in = new Scanner(new File("src/main/resources/levelCounter"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		unlockedLevels = in.nextInt();
		in.close();
	}
	
	void initGUI() {
		playButton.setDisable(true);
		customLevels.setMaxWidth(200);
		customLevels.setMaxHeight(200);
		
		this.setPadding(new Insets(100));
		this.setHgap(50);
		this.getStylesheets().add("menuGraphics.css");
		this.getStyleClass().add("menu");
		
		Text title = new Text("Levels");
		title.getStyleClass().add("title");
		this.add(title, 0, 0);
		
		GridPane pane = new GridPane();
		pane.setPadding(new Insets(100));
		pane.setHgap(50);
		pane.getStylesheets().add("menuGraphics.css");
		pane.getStyleClass().add("menu");
		
		GridPane custom = new GridPane();
		custom.setPadding(new Insets(100));
		custom.setHgap(50);
		custom.getStylesheets().add("menuGraphics.css");
		custom.getStyleClass().add("menu");
		
		this.add(pane, 0, 1);
		this.add(custom, 0, 2);
		
		for(int i = 0 ; i < numLevels ; ++i) {
			levels[i] = new Button("" + (i + 1));
			levels[i].getStyleClass().add("menuButton");
			if(i + 1 > unlockedLevels)
				levels[i].setDisable(true);
			
			pane.add(levels[i], i, 0);
		}
		custom.add(customLevels, 0, 0);
		custom.add(playButton, 1, 0);
		
		playButton.getStyleClass().add("menuButton");
	}
	
	void initEH() {
		for(Button b : levels) {
			b.setOnMouseMoved(event -> b.requestFocus());
			b.setOnMouseClicked(event -> {
				windowManager.startGame(1, "level" + b.getText());
				windowManager.goToScene(new Client("127.0.0.1", 8182, windowManager));
			});
		}
		
		playButton.setOnMouseClicked(event -> {
			windowManager.startGame(1, customLevels.getSelectionModel().getSelectedItem());
			windowManager.goToScene(new Client("127.0.0.1", 8182, windowManager));
		});
		
		customLevels.setOnMouseClicked(event -> playButton.setDisable(false));
	}

}
