package graphics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import core.entities.AbstractBox;
import core.entities.Bullet;
import core.parts.Direction;
import graphics.levelEditor.ObjectInfo;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public class Client extends GridPane{
	int id;
	PrintWriter out = null;
	BufferedReader in;
	
	boolean up = false, down = false, right = false, left = false;
	
	Socket client;
	Canvas canvas;
	GraphicsContext gc;
	
	static String defaultCmd = "null\n";
	String cmd = defaultCmd;
	
	ArrayList<ObjectInfo> bouncyBoxes = new ArrayList<>();
	Image imgCarroPlayer;
	Image imgCannonePlayer;
	Image imgNemico;
	Image imgCannoneNemico;
	Image imgBullet;
	Image imgBouncyBox;
	Image imgDestructibleBox;
	
	public Client(String address, int port) {
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
				id = Integer.parseInt(in.readLine());
				System.out.println("with id " + id);
			} catch (NumberFormatException | IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	void initGUI() {
		canvas = new Canvas(800, 800);
		this.getChildren().add(canvas);		
		gc = canvas.getGraphicsContext2D();
		
		loadImages();
	}
	
	void initEH() {
		this.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				if(e.getCode() == KeyCode.D) {
					right = true;
					if(left)
						left = false;
				}
				else if(e.getCode() == KeyCode.A) {
					left = true;
					if(right)
						right = false;
				}
				else if(e.getCode() == KeyCode.W) {
					up = true;
					if(down)
						down = false;
				}
				else if(e.getCode() == KeyCode.S) {
					down = true;
					if(up)
						up = false;
				}
				else if(e.getCode() == KeyCode.SPACE) {
					if(cmd.equals(defaultCmd))
						cmd = "";
					cmd = cmd.concat(id + "_SHOOT\n");
				}
				
				if(up || down || left || right) {
					if(cmd.equals(defaultCmd))
						cmd = "";
					
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
					
					cmd = cmd.concat(id + "_MOVE_" + direction.toString() + "\n");
					
				}
			}
		});
		
		this.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				if(e.getCode() == KeyCode.D)
					right = false;
				if(e.getCode() == KeyCode.A)
					left = false;
				if(e.getCode() == KeyCode.W)
					up = false;
				if(e.getCode() == KeyCode.S)
					down = false;				
			}
		});
		
		this.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				if(cmd.equals(defaultCmd))
					cmd = "";
				cmd = cmd.concat(id + "_ROTATE_" + e.getX() + "_" + e.getY() + "\n");				
			}
		});
	}
	
	void initTimer() {
		new Thread() {			
			@Override
			public void run() {
				receiveBouncyBoxes();
				while(true) {
					send(cmd);
					cmd = defaultCmd;
					
					gc.clearRect(0, 0, 800, 800);
					gc.setFill(Color.BISQUE);
					gc.fillRect(0,  0,  800, 800);					

					for(ObjectInfo box : bouncyBoxes)
						disegnaBox(gc, box, imgBouncyBox);
					
					String signal = receive();
					if(signal.equals("EXIT")) {
						try {
							client.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						break;
					}
					else if(signal.equals("CLOSE"))
						break;
					
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
					
					
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
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
		if(!client.isClosed())
		out.println(command);
		out.flush();
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
}
