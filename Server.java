import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.*;

public class Server {
    public static void main(String[] args) throws IOException {
        int portNumber = 1024;
        ServerSocket serverSocket = new ServerSocket(portNumber);
        Manager manager = new Manager();
        int n = 0;

        // This loop will run and wait for one connection at a time.
        while (true) {
            System.out.println("Run the while loop: " + n + " times.");
            System.out.println("Waiting for a connection");

            // Wait for a connection.
            Socket clientSocket = serverSocket.accept();
            ServerThread serverThread = new ServerThread(clientSocket, manager);
            Thread newThread = new Thread(serverThread);
            manager.add(newThread, serverThread);
            n++;

        }
    }
}
