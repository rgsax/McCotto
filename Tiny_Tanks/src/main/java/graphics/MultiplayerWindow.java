package graphics;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.DEFAULT;

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

public class MultiplayerWindow extends GridPane {
	WindowManager windowManager;
	Text title = new Text("Tiny Tanks");
	Button hostButton = new Button("HOST A GAME");
	Button joinGameButton = new Button("JOIN GAME");
	CheckBox useYourIP = new CheckBox("use your IP");
	String defaultIP = "192.168.0.100";
	TextField ipField = new TextField(defaultIP);
	Spinner<Integer> spinner = new Spinner<>(2, 10, 2);
	
	public MultiplayerWindow(WindowManager windowManager) {
		this.windowManager = windowManager;
		initGUI();
		initEH();
	}
	
	void initGUI() {
		this.getStylesheets().add("file.css");
		this.getStyleClass().add("menu");
		hostButton.getStyleClass().add("menuButton");
		joinGameButton.getStyleClass().add("menuButton");
		title.getStyleClass().add("title");
		ipField.getStyleClass().add("ip_field");
		
		
		this.setMinSize(800,  800);
		this.setPadding(new Insets(100, 100, 100, 100));
		this.setVgap(50);
		this.setHgap(50);
		this.add(title, 0, 0);
		this.add(hostButton, 0, 1);
		this.add(spinner, 1, 1);
		this.add(ipField, 0, 3);
		this.add(useYourIP, 1, 3);
		this.add(joinGameButton, 0, 5);		
	}
	
	void initEH() {
		hostButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				windowManager.startGame(spinner.getValue());
				windowManager.scene = new Scene(new Client(ipField.getText(), 8182));
				windowManager.initEH();
				windowManager.stage.setScene(windowManager.scene);
			}
		});
		
		useYourIP.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(useYourIP.isSelected()) {
					ipField.setDisable(true);
					ipField.setText(defaultIP);
				}
				else {
					ipField.setDisable(false);
				}
					
			}
		});
		
		hostButton.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				hostButton.requestFocus();				
			}
		});
		
		joinGameButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				windowManager.scene = new Scene(new Client(ipField.getText(), 8182));
				windowManager.initEH();
				windowManager.stage.setScene(windowManager.scene);
			}
		});
		
		joinGameButton.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				joinGameButton.requestFocus();				
			}
		});
		
		ipField.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ipField.requestFocus();	
				ipField.deselect();
			}
		});
	}
}
