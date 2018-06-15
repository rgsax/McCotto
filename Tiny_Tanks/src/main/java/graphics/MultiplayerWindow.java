package graphics;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

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
	String defaultIP = null;
	TextField ipField;
	Spinner<Integer> spinner = new Spinner<>(2, 10, 2);
	
	public MultiplayerWindow(WindowManager windowManager) {
		this.windowManager = windowManager;
		defaultIP = getCurrentIP();
		initGUI();
		initEH();
	}
	
	void initGUI() {
		ipField = new TextField(defaultIP);
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
	
	String getCurrentIP() {
		String ip = "127.0.0.1";
		Enumeration<NetworkInterface> interfaces = null;
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			return ip;
		}
		while (interfaces.hasMoreElements()){
		    NetworkInterface current = interfaces.nextElement();
		    try {
				if (!current.isUp() || current.isLoopback() || current.isVirtual()) continue;
				Enumeration<InetAddress> addresses = current.getInetAddresses();
			    while (addresses.hasMoreElements()){
			        InetAddress current_addr = addresses.nextElement();
			        if (current_addr.isLoopbackAddress()) continue;
			        if(current_addr instanceof Inet4Address && !current_addr.getHostAddress().equals("127.0.0.1"))
			        	return current_addr.getHostAddress();
			    }
		    } catch (SocketException e) {
				e.printStackTrace();
			}
		}
		
		return ip;
	}
}
