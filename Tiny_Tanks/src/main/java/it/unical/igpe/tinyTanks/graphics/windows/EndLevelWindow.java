package it.unical.igpe.tinyTanks.graphics.windows;

import it.unical.igpe.tinyTanks.graphics.WindowManager;
import it.unical.igpe.tinyTanks.graphics.game.Client;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class EndLevelWindow extends GridPane {
	WindowManager windowManager;
	Button continueButton = new Button("CONTINUE");
	Button exitButton = new Button("EXIT");
	Text title;
	int numPlayers;
	
	public EndLevelWindow(WindowManager windowManager, String title, int numPlayers) {
		this.windowManager = windowManager;
		this.title = new Text(title);
		this.numPlayers = numPlayers;
		initGUI();
		initEH();
	}
	
	void initGUI() {
		this.setMinWidth(800);
		this.setPadding(new Insets(100));
		this.setHgap(50);
		this.getStylesheets().add("menuGraphics.css");
		this.getStyleClass().add("menu");
		continueButton.getStyleClass().add("menuButton");
		exitButton.getStyleClass().add("menuButton");
		
		title.getStyleClass().add("title");
		
		GridPane pane = new GridPane();
		pane.setPadding(new Insets(100));
		pane.setVgap(50);
		pane.getStylesheets().add("menuGraphics.css");
		pane.getStyleClass().add("menu");
		
		pane.add(exitButton, 0, 0);
		pane.add(continueButton, 0, 1);
		
		this.add(title, 0, 0);
		this.add(pane, 0, 1);
	}
	
	void initEH() {
		continueButton.setOnMouseMoved(event -> continueButton.requestFocus());
		continueButton.setOnMouseClicked(event -> {
			if(numPlayers > 1) {
				windowManager.goToScene(new MultiplayerWindow(windowManager));
			}
			else {
				windowManager.startGame(numPlayers, "level" + (Integer.parseInt(windowManager.getCurrentLevel().replace("level", "")) + 1));
				windowManager.goToScene(new Client("127.0.0.1", 8182, windowManager));
			}
		});
		
		exitButton.setOnMouseMoved(event -> exitButton.requestFocus());
		exitButton.setOnMouseClicked(event -> windowManager.backToMenu());
	}
}
