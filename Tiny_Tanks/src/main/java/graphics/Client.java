package graphics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import core.entities.AbstractBox;
import core.parts.Direction;
import graphics.levelEditor.ObjectInfo;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public class Client extends GridPane{
	static int width = 1000;
	static int height = 1000;
	int id;
	PrintWriter out = null;
	BufferedReader in;
	WindowManager windowManager = null;

	boolean up = false, down = false, right = false, left = false, shoot = false;

	Socket client;
	Canvas canvas;
	GraphicsContext gc;

	static String defaultCmd = "null\n";
	String mouseCmd = null;
	String keyboardCmd = null;

	ArrayList<ObjectInfo> bouncyBoxes = new ArrayList<>();
	Image imgCarroPlayer;
	Image imgCannonePlayer;
	Image imgNemico;
	Image imgCannoneNemico;
	Image imgBullet;
	Image imgBouncyBox;
	Image imgDestructibleBox;
	
	int numPlayers;

	public Client(String address, int port, WindowManager windowManager) {
		this.windowManager = windowManager;
		try {
			client = new Socket(address, port);
			System.out.println("connesso");
		} catch (IOException e) {
			e.printStackTrace();
		}

		initConnection();
		initGUI();		
		initEH();
		initTimer();
	}

	void initConnection() {
		if(!client.isClosed()) {
			try {
				out = new PrintWriter(client.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				numPlayers = Integer.parseInt(in.readLine());
				id = Integer.parseInt(in.readLine());
				System.out.println("with id " + id);
			} catch (NumberFormatException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	void initGUI() {
		canvas = new Canvas(1000, 1000);
		this.getChildren().add(canvas);		
		gc = canvas.getGraphicsContext2D();

		loadImages();
	}

	private void generateKeyboardCmd() {
		if(up || down || left || right) {
			if(keyboardCmd == null)
				keyboardCmd = new String();

			Direction direction = null;

			if(up && right)
				direction = Direction.NE;
			else if(up && left)
				direction = Direction.NW;
			else if(down && right)
				direction = Direction.SE;
			else if(down && left)
				direction = Direction.SW;
			else if(right) {
				direction = Direction.E;
			}
			else if(left) {
				direction = Direction.W;
			}
			else if(up) {
				direction = Direction.N;
			}
			else if(down) {
				direction = Direction.S;
			}

			keyboardCmd = keyboardCmd.concat(id + "_MOVE_" + direction.toString() + "\n");

		}
	}

	void initEH() {
		this.setOnKeyPressed(event -> {
			if(event.getCode() == KeyCode.D) {
				right = true;
				if(left)
					left = false;
			}
			else if(event.getCode() == KeyCode.A) {
				left = true;
				if(right)
					right = false;
			}
			else if(event.getCode() == KeyCode.W) {
				up = true;
				if(down)
					down = false;
			}
			else if(event.getCode() == KeyCode.S) {
				down = true;
				if(up)
					up = false;
			}
			else if(event.getCode() == KeyCode.SPACE) {
				shoot = true;
			}

		});

		this.setOnKeyReleased(event -> {
			if(event.getCode() == KeyCode.D)
				right = false;
			if(event.getCode() == KeyCode.A)
				left = false;
			if(event.getCode() == KeyCode.W)
				up = false;
			if(event.getCode() == KeyCode.S)
				down = false;				
		});

		this.setOnMouseMoved(event -> {
			if(mouseCmd == null)
				mouseCmd = new String();
			mouseCmd = mouseCmd.concat(id + "_ROTATE_" + event.getX() + "_" + event.getY() + "\n");				
		});
	}

	void initTimer() {
		receiveBouncyBoxes();

		new AnimationTimer() {

			@Override
			public void handle(long now) {

				send(generateCmd());

				gc.clearRect(0, 0, width, height);
				gc.setFill(Color.BISQUE);
				gc.fillRect(0,  0,  width, height);					

				for(ObjectInfo box : bouncyBoxes)
					disegnaBox(gc, box, imgBouncyBox);

				String signal = receive();
				if(signal.contains("WIN") || signal.contains("CLOSE") || signal.contains("EXIT")) {
					this.stop();
					String saveName = "src/main/resources/levelCounter";
					File saveFile = new File(saveName);
					Scanner inSave = null;
					try {
						inSave = new Scanner(saveFile);
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
					
					int lastLevelAvailable = inSave.nextInt();
					inSave.close();
					
					try {
						saveFile.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					int currentLevel = Integer.parseInt(windowManager.currentLevel.replace("level", ""));
					
					System.out.println(currentLevel + " " + lastLevelAvailable);
					
					if(currentLevel + 1 > lastLevelAvailable) {
						try {
							PrintWriter outSave = new PrintWriter(saveFile);
							outSave.println(currentLevel + 1);
							outSave.close();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
					
					windowManager.goToScene(new EndLevelWindow(windowManager, "WIN", numPlayers));
				}
				else {
					int nEnemy = Integer.parseInt(signal);
					disegnaCarri(gc, imgNemico, imgCannoneNemico, nEnemy);
	
					int nPlayers = Integer.parseInt(receive());					
					disegnaCarri(gc, imgCarroPlayer, imgCannonePlayer, nPlayers);
	
					int nDBoxes = Integer.parseInt(receive());
					for(int i = 0 ; i < nDBoxes ; i++) {
						String[] line = receive().split(" ");
						double x = Double.parseDouble(line[0]);
						double y = Double.parseDouble(line[1]);
	
						gc.drawImage(imgDestructibleBox, x, y);
					}
	
					gc.setFill(Color.RED);
					int nBullets = Integer.parseInt(receive());
					for(int i = 0 ; i < nBullets ; i++) {
						String[] line = receive().split(" ");
						double x = Double.parseDouble(line[0]);
						double y = Double.parseDouble(line[1]);
						int width = Integer.parseInt(line[2]);
						int height = Integer.parseInt(line[3]);
						gc.fillOval(x, y, width, height);
					}		
				}

			}

			void disegnaCarri(GraphicsContext gc, Image imgCarro, Image imgCannone, int N) {
				for(int i = 0 ; i < N ; i++) {
					String[] carro = receive().split(" ");
					String[] cannone = receive().split(" ");

					double carroX = Double.parseDouble(carro[0]);
					double carroY = Double.parseDouble(carro[1]);
					double pivotXCarro = Double.parseDouble(carro[2]);
					double pivotYCarro = Double.parseDouble(carro[3]);
					double angoloCarro = Double.parseDouble(carro[4]);

					double cannoneX = Double.parseDouble(cannone[0]);
					double cannoneY = Double.parseDouble(cannone[1]);
					double pivotXCannone = Double.parseDouble(cannone[2]);
					double pivotYCannone = Double.parseDouble(cannone[3]);
					double angoloCannone = Double.parseDouble(cannone[4]);

					drawRotatedImage(gc, imgCarro, carroX, carroY, pivotXCarro, pivotYCarro, angoloCarro);
					drawRotatedImage(gc, imgCannone, cannoneX, cannoneY, pivotXCannone, pivotYCannone, angoloCannone);
				}
			}

			void drawRotatedImage(GraphicsContext gc, Image img, double x, double y, double pivotX, double pivotY, double angle) {
				gc.save();
				Rotate rotate = new Rotate(angle, pivotX, pivotY);
				gc.setTransform(rotate.getMxx(), rotate.getMyx(), rotate.getMxy(), rotate.getMyy(), rotate.getTx(), rotate.getTy());
				gc.drawImage(img, x, y);
				gc.restore();				
			}
		}.start();
	}

	public void send(String command) {	
		if(!client.isClosed()) {
			out.println(command);
			out.flush();
		}
	}

	public void close() {
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String receive() {
		String received = "";
		try {
			received = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return received;
	}

	void receiveBouncyBoxes() {
		int nBoxes = Integer.parseInt(receive());
		for(int i = 0 ; i < nBoxes ; i++) {
			String[] line = receive().split(" ");
			int width = Integer.parseInt(line[0]);
			int height = Integer.parseInt(line[1]);
			double x = Double.parseDouble(line[2]);
			double y = Double.parseDouble(line[3]);
			bouncyBoxes.add(new ObjectInfo(width, height, x, y));
		}
	}

	void disegnaBox(GraphicsContext gc, ObjectInfo box, Image img) {
		for(int i = 0 ; i < box.getWidth() / AbstractBox.minWidth ; i++) {
			for(int j = 0 ; j< box.getHeight() / AbstractBox.minHeight ; j++)
				gc.drawImage(img, box.getX() + i * AbstractBox.minWidth, box.getY() + j * AbstractBox.minHeight);
		}
	}

	void loadImages() {
		imgCarroPlayer = new Image("carro.png");
		imgCannonePlayer = new Image("cannone.png");
		imgNemico = new Image("nemico.png");
		imgCannoneNemico = new Image("cannone_nemico.png");
		imgBullet = new Image("bullet.png");
		imgBouncyBox = new Image("BouncyBox.png");
		imgDestructibleBox = new Image("DestructibleBox.png");
	}

	public void sendCloseRequest() {
		send(id + "_" + "CLOSE\n");
	}
	
	String generateCmd() {
		int count = 0;
		
		generateKeyboardCmd();

		String cmd = "";
		if(keyboardCmd != null) {
			++count;
			cmd = cmd.concat(keyboardCmd);
			keyboardCmd = null;
		}
		
		if(mouseCmd != null) {
			++count;
			cmd = cmd.concat(mouseCmd);
			mouseCmd = null;
		}
		
		if(shoot) {
			shoot = false;
			cmd = cmd.concat(id + "_SHOOT\n");
			++count;
		}
		
		cmd = Integer.toString(count).concat("\n" + cmd);	
		return cmd;
	}
}
