package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class FrontendServer {
    
    int port;
    boolean stop;
    FrontendHandler fh;
    
    public FrontendServer(int port, FrontendHandler fh) {
        this.port = port;
        this.fh = fh;
    }
    
    public void start() {
        stop = false;
        new Thread(()->startServer()).start();
    }
    
    public void startServer() {
        Controller.addTask("Frontend Server");
        try {
            ServerSocket server = new ServerSocket(port);
            server.setSoTimeout(1000);
            while (!stop) {
                try {
                    Socket client = server.accept();
                    System.out.println("Frontend connected!");
                    fh.handle(client.getInputStream(), client.getOutputStream());
                    System.out.println("Frontend disconnected");
                    fh.close();
                    client.close();   
                } catch (SocketTimeoutException e) {}
            }
            server.close();
            Controller.removeTask("Frontend Server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void stop() {
        stop = true;
    }
}
