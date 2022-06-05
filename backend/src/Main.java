import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import interpreter.Interpreter;
import server.AgentServer;

public class Main {

    public static void main(String[] args) {
        AgentServer server = new AgentServer(1000);
        server.start(); // starts in a separate thread
        System.out.println("Waiting for client...");
        
        // separate thread for enabling the user to stop the server
//        new Thread(()->{
//            Scanner s = new Scanner(System.in);
//            String input;
//            do {
//                input = s.next();
//            } while (!input.equals("stop"));
//            s.close();
//            server.stop();
//        }).start();
        
        Set<Integer> set;
        while ((set = server.getConnectedClients()).isEmpty()) {} // waiting until a client connects
        List<Integer> clients = set.stream().collect(Collectors.toList());
        int clientID = clients.get(0);
        System.out.println("Client connected. ID: " + clientID);
        
        Interpreter i = new Interpreter();
        try {
            List<String> lines = Files.readAllLines(Paths.get("resources/script.txt"));
            i.interpret(lines, clientID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        while (i.getStatus() == 1) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        File f = new File("resources/temp.txt");
        String response = AgentServer.send(clientID, "getFlightDataStart resources/mobydick.txt");
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(f));
            do {
                response = AgentServer.send(clientID, "getFlightDataNext");
                if (response != null) {
                    writer.write(response);
                    writer.newLine();
                }
            } while (response != null);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        server.disconnect(clientID);
        System.out.println("Disconnected client");
        server.stop();
    }

}
