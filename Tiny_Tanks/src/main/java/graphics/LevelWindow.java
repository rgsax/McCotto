package graphics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class LevelWindow extends GridPane{
	WindowManager windowManager;
	public static final int numLevels = 5;
	int unlockedLevels;
	Button[] levels = new Button[numLevels];
	
	public LevelWindow(WindowManager windowManager) {
		this.windowManager = windowManager;
		
		loadSettings();
		
		initGUI();
		initEH();
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
		
		this.add(pane, 0, 1);
		
		for(int i = 0 ; i < numLevels ; ++i) {
			levels[i] = new Button("" + (i + 1));
			levels[i].getStyleClass().add("menuButton");
			if(i + 1 > unlockedLevels)
				levels[i].setDisable(true);
			
			pane.add(levels[i], i, 0);
		}
	}
	
	void initEH() {
		for(Button b : levels) {
			b.setOnMouseMoved(event -> b.requestFocus());
			b.setOnMouseClicked(event -> {
				windowManager.startGame(1, "level" + b.getText());
				windowManager.goToScene(new Client("127.0.0.1", 8182, windowManager));
			});
		}
	}

}
