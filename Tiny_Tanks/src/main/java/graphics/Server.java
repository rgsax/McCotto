package graphics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

import core.Mondo;
import core.entities.CarroArmato;

public class Server {	
	ServerSocket server = null;
	ArrayList<Socket> clients = new ArrayList<>();
	int numPlayers;
	String level;
	
	public Server(int port, int numPlayers, String level) {
		try {
			server = new ServerSocket(port); //Creo il server
		} catch (IOException e) {
			e.printStackTrace();
		}		
		this.numPlayers = numPlayers;
		this.level = level;
	}
	
	@SuppressWarnings("unused")
	public void init(HashMap<Integer, CarroArmato> players, Mondo mondo) {
		Scanner posIn = null;
		try {
			posIn = new Scanner(new File("src/main/resources/" + level + ".pos"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		posIn.useLocale(Locale.US);
		
		while(clients.size() < numPlayers) { //Mi metto in ascolto e accetto le richieste di connessione
			Socket incoming = null;
			try {
				incoming = server.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
			catch (NullPointerException e) {
				e.printStackTrace();
			}
			
			clients.add(incoming);
			PrintWriter out = null;
			try {
				out = new PrintWriter(incoming.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Integer id = new Integer(clients.indexOf(incoming));
			
			int playerWidth = posIn.nextInt();
			int playersHeight = posIn.nextInt();
			double playerX = posIn.nextDouble();
			double playerY = posIn.nextDouble();
			
			players.put(id, new CarroArmato(playerX, playerY, mondo, id));
			
			out.println(numPlayers);
			out.println(clients.indexOf(incoming)); //Assegno e mando l'id al giocatore
			out.flush();
		}
		
		mondo.setPlayers(players);
		posIn.close();
	}
	
	public void removeClient(int i) {
		Socket client = clients.get(i);
		
		PrintWriter out = null;
		try {
			out = new PrintWriter(client.getOutputStream());
			out.println("EXIT");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		clients.remove(i);
	}
	
	public void send(String map) {
		for(Socket client : clients) {
			Thread t = new Thread() {
				@Override
				public void run() {
					PrintWriter out = null;
					try {
						out = new PrintWriter(client.getOutputStream());
					} catch (IOException e) {
						e.printStackTrace();
					}

					out.print(map);
					out.flush();
				}
			};
			
			t.start();
		}
	}
	
	public String receiveCommand() {
		String s = "NO_CLIENTS";
		if(!clients.isEmpty())
			s = "";
		
		ArrayList<Socket> toDelete = new ArrayList<>();
		
		for(Socket client : clients) {
			if(client != null) {
				BufferedReader in = null;
				try {
					in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					int cmds = Integer.parseInt(in.readLine());
					if(cmds == 0)
						s = s.concat("null\n");
					for(int i = 0 ; i < cmds ; ++i)
						s = s.concat(in.readLine() + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		for(Socket client : toDelete) {
			try {
				PrintWriter out = new PrintWriter(client.getOutputStream());
				out.println("CLOSE");
				out.flush();
				client.close();
				clients.remove(client);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return s;
	}	
	
	public void close() {
		try {
			send("CLOSE");
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
