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
	
	volatile boolean stop;
	int port;
	ClientHandler ch;
	public Server(int port, ClientHandler ch) {
		stop=false;
		this.port = port;
		this.ch = ch;
	}
	
	
	private void startServer() {
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
	public void start() {
		stop = false;
		new Thread(()->startServer()).start();
	}
	
	public void stop() {
		stop=true;
	}
}
