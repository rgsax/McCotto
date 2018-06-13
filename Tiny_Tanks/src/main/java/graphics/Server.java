package graphics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import core.Mondo;
import core.entities.CarroArmato;

public class Server {	
	ServerSocket server = null;
	ArrayList<Socket> clients = new ArrayList<>();
	
	public Server(int port) {
		try {
			server = new ServerSocket(port); //Creo il server
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void init(HashMap<Integer, CarroArmato> players, Mondo mondo) {
		
		while(clients.size() < 1) { //Mi metto in ascolto e accetto le richieste di connessione
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
			
			players.put(id, new CarroArmato(550, 550, mondo, id));
			
			out.println(clients.indexOf(incoming)); //Assegno e mando l'id al giocatore
			out.flush();
		}
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
		
		System.out.println("sending " + s);
		
		return s;
	}	
	
	public void close() {
		try {
			send("EXIT");
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
