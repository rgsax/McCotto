package graphics;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

import core.Mondo;
import core.entities.AbstractBox;
import core.entities.BouncyBox;
import core.entities.Bullet;
import core.entities.CarroArmato;
import core.entities.DestructibleBox;
import core.entities.Enemy;
import core.parts.Cannon;
import core.parts.Direction;

public class ServerGame extends Thread{
	Server server = null;
	Mondo mondo;
	ArrayList<AbstractBox> boxes = new ArrayList<>();
	ArrayList<Enemy> enemies = new ArrayList<>();
	HashMap<Integer, CarroArmato> players = new HashMap<>();
	
	public ServerGame(int numPlayers, String level) {
		server = new Server(8182, numPlayers, level);
		loadMap(level);		
	}
	
	@Override
	public void run() {
		server.init(players, mondo);
		
		sendBouncyBoxes();
		
		int count = 0;
		
		while(true) {
			boolean exit = elabora(server.receiveCommand());
			if(exit)
				break;
			
			count++;					

			mondo.update();
			
			ArrayList<CarroArmato> diedPlayers = new ArrayList<>();
			
			for(CarroArmato player : players.values()) {
				if(player.getLife() <= 0) {
					diedPlayers.add(player);
				}
			}
			
			String diedIDs = getDiedIDs(diedPlayers);
			
			if(players.size() <= 1 && enemies.isEmpty()) {
				server.close();
			}
			
		//IL NEMICO SI MUOVE QUANDO COUNT ARRIVA A 20	
			if (count >= 10) {
				mondo.muoviNemici();
			}
						
			if(count >= 30) {
				count = 0;
				for(CarroArmato c : enemies) {
					mondo.spara(c);	
				}
			}
			
			server.send(getMap());
			server.send(getMap() + "\n" + diedIDs);
			
			try {
				sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	String getDiedIDs(ArrayList<CarroArmato> diedPlayers) {
		String diedSend = Integer.toString(diedPlayers.size()) + "\n";
		for(CarroArmato player : diedPlayers) {
			diedSend = diedSend.concat(player.getId().toString() + "\n");
			players.remove(player.getId());
			System.out.println("removing id " + player.getId().toString());
		}
		return diedSend;
	}
	
	@SuppressWarnings("unused")
	void loadMap(String l) {
		String level = "src/main/resources/" + l + ".level";
		try {
			Scanner fileIn = new Scanner(new FileReader(level));
			fileIn.useLocale(Locale.US);
			int width = fileIn.nextInt();
			int height = fileIn.nextInt();
			
			mondo = new Mondo(width, height);
			
			int numBBoxes = fileIn.nextInt();
			for(int i = 0 ; i < numBBoxes ; i++) {
				int bWidth = fileIn.nextInt();
				int bHeight = fileIn.nextInt();
				
				double x = fileIn.nextDouble();
				double y = fileIn.nextDouble();
				
				boxes.add(new BouncyBox(bWidth, bHeight, x, y));				
			}
			
			int numDBoxes = fileIn.nextInt();
			for(int i = 0 ; i < numDBoxes ; i++) {
				int dWidth = fileIn.nextInt();
				int dHeight = fileIn.nextInt();
				
				double x = fileIn.nextDouble();
				double y = fileIn.nextDouble();
				
				boxes.add(new DestructibleBox(dWidth, dHeight, x, y));				
			}
			
			mondo.setBoxes(boxes);
			
			int nEnemies = fileIn.nextInt();
			for(int i = 0 ; i < nEnemies ; i++) {
				int cWidth = fileIn.nextInt();
				int cHeight = fileIn.nextInt();
				
				double x = fileIn.nextDouble();
				double y = fileIn.nextDouble();
				
				enemies.add(new Enemy(x, y, mondo));
			}
			
			mondo.setEnemiesList(enemies);
			
			fileIn.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	boolean elabora(String command) {
		if(command.equals("NO_CLIENTS") || command.equals("CLOSE"))
			return true;
		String[] in = command.split("\\n");
		for(String cmd : in) {
			if(!cmd.equals("null")) {
				String[] objs = cmd.split("_");
				CarroArmato player = players.get(new Integer(Integer.parseInt(objs[0])));
				if(player != null) {
					if(objs[1].equals("CLOSE")) {
						players.remove(new Integer(Integer.parseInt(objs[0])));
						server.removeClient(Integer.parseInt(objs[0]));
					}
					else if(objs[1].equals("SHOOT")) {
						mondo.spara(player);
					}
					else if(objs[1].equals("ROTATE")) {
							double mouseX = Double.parseDouble(objs[2]);
							double mouseY = Double.parseDouble(objs[3]);
							
							mondo.orientaCannone(player, mouseX, mouseY);
					}
					else if(objs[1].equals("MOVE")){
							Direction d = Direction.valueOf(objs[2]);
							player.muovi(d);
					}
				}
			}
		}
		
		return false;
	}
	
	String getMap() {
		String map = new String();
		
		map = map.concat(enemies.size() + "\n");
		for(Enemy enemy : enemies) {
			map = map.concat(enemy.getX() + " " + enemy.getY() + " " + 
					(enemy.getX() + enemy.getWidth() / 2.0) + " " + (enemy.getY() + enemy.getHeight() / 2.0) + " " + 
					enemy.getDirection().getAngle() +"\n");
			Cannon cannone = enemy.getCannone();
			map = map.concat(cannone.getX() + " " + cannone.getY() + " " + 
					(cannone.getX() + cannone.getHeight() / 2.0) + " " + (cannone.getY() + cannone.getHeight() / 2.0) + " " +
					cannone.getAngle() + "\n");
		}
		
		map = map.concat(players.size() + "\n");
		for(CarroArmato player : players.values()) {
			map = map.concat(player.getX() + " " + player.getY() + " " + 
					(player.getX() + player.getWidth() / 2.0) + " " + (player.getY() + player.getHeight() / 2.0) + " " + 
					player.getDirection().getAngle() +"\n");
			Cannon cannone = player.getCannone();
			map = map.concat(cannone.getX() + " " + cannone.getY() + " " + 
					(cannone.getX() + cannone.getHeight() / 2.0) + " " + (cannone.getY() + cannone.getHeight() / 2.0) + " " +
					cannone.getAngle() + "\n");
		}

		ArrayList<DestructibleBox> dBoxes = new ArrayList<>();
		for(AbstractBox box : boxes) {
			if(box instanceof DestructibleBox)
				dBoxes.add((DestructibleBox)box);
		}
		
		map = map.concat(dBoxes.size() + "\n");
		for(DestructibleBox box : dBoxes) {
			map = map.concat(box.getX() + " " + box.getY() + "\n");
		}
		
		map = map.concat(mondo.getBullets().size() + "\n");
		for(Bullet bullet : mondo.getBullets()) {
			map = map.concat(bullet.getX() + " " + bullet.getY() + " " + bullet.getWidth() + " " + bullet.getHeight() + "\n");
		}
	
		
		return map;
		
	}
	
	void sendBouncyBoxes() {
		ArrayList<BouncyBox> bBoxes = new ArrayList<>();
		for(AbstractBox box : boxes) {
			if(box instanceof BouncyBox)
				bBoxes.add((BouncyBox)box);
		}
		
		String map = bBoxes.size() + "\n";
		
		for(BouncyBox box : bBoxes)
			map = map.concat(box.getWidth() + " " + box.getHeight() + " " +  box.getX() + " " + box.getY() + "\n");
		
		server.send(map);
	}
	
	public void closeServer() {
		server.close();
	}
}
