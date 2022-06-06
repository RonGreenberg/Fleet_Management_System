package model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server {
	static private Integer clientLimit;
	
	//treat a client that is already connected
	public interface ClientHandler{//using CLI
		// define...
		void handleClient(InputStream inFromClient);
	}
	
	volatile boolean stop;
	public Server() {
		stop=false;
	}
	
	
	private void startServer(int port, ClientHandler ch) {
		// implement here the server...
		try {
			ServerSocket server=new ServerSocket(port);
			server.setSoTimeout(1000);
			while(!stop){
				try{
					Socket aClient=server.accept(); // blocking call
					ch.handleClient(aClient.getInputStream());
					aClient.close();
				}catch(SocketTimeoutException e) {}
			}
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// runs the server in its own thread
	public void start(int port, ClientHandler ch) {
		stop = false;
		new Thread(()->startServer(port,ch)).start();
	}
	
	public void stop() {
		stop=true;
	}
}
