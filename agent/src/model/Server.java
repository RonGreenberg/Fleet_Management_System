package model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server {	
	public interface ClientHandler{
		void handleClient(InputStream inFromClient);
	}
	
	int port;
	ClientHandler ch;
	public Server(int port, ClientHandler ch) {
		this.port = port;
		this.ch = ch;
	}
	
	
	private void startServer() {
		try {
			ServerSocket server=new ServerSocket(port);
			server.setSoTimeout(1000);
			while (true){
				try{
					Socket aClient=server.accept(); // blocking call
					System.out.println("FlightGear connected to agent!");
					ch.handleClient(aClient.getInputStream());
					aClient.close();
					server.close();
					return;
				}catch(SocketTimeoutException e) {}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// runs the server in its own thread
	public void start() {
		new Thread(()->startServer()).start();
	}
}
