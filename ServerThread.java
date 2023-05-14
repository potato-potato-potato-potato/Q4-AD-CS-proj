import java.net.*;
import java.io.*;

public class ServerThread implements Runnable {
    private Socket clientSocket;

    private Manager manager;
    public ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean close, isHost;
    private String name;
    private Pair<String, Object> input;
    private Map map;

    public ServerThread(Socket clientSocket, Manager manager) {
        this.clientSocket = clientSocket;
        this.manager = manager;
        close = false;

    }

    @SuppressWarnings("unchecked")
    public void run() {
        System.out.println(Thread.currentThread().getName() + ": connection opened.");

        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream((clientSocket.getInputStream()));
            if (Thread.currentThread().getName().equals("Thread-0")) {
                out.writeObject(new Pair<String, Boolean>("isHost", true));
                isHost = true;
            } else {
                out.writeObject(new Pair<String, Boolean>("isHost", false));
                isHost = false;
            }
            while (true) {
                input = (Pair<String, Object>) in.readObject();
                if (input.getKey().equals("clientoutput")) {// remove this thread if client disconnects, reassign host
                    // if nessescary
                System.out.println("Received keystrokes");
                manager.updateThread((int[]) input.getValue(), Thread.currentThread());
                }
                else if (input.getKey().equals("threadname")) {
                    name = (String) input.getValue();
                }
                else if (input.getKey().equals("game")) {
                    manager.broadcast(input, Thread.currentThread());
                }
                else if (input.getKey().equals("StartGame")) {
                    manager.broadcast(input, Thread.currentThread());
                    manager.start();
                }
                else if (input.getKey().equals("Quit")) {// remove this thread if client disconnects, reassign host if
                                                    // nessescary
                    close = true;
                    manager.threadQuit(isHost, Thread.currentThread());
                }
                

                if (close) {
                    break;
                }
            }

            // Clears and close the output stream.
            System.out.println("Closing");
            out.flush();
            out.close();
            System.out.println(Thread.currentThread().getName() + ": connection closed.");
        } catch (IOException e) {
            System.out.println("Error listening for a connection");
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error listening for a connection");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void send(Pair s) {
        System.out.println("Sending s: " + s.toString());
        try {
            out.writeObject(s);
        } catch (IOException e) {
            System.out.println("Error listening for a connection");
            e.printStackTrace();
        }
    }

    public Map getMap() {
        return map;
    }

    public void broadcast(Pair s) {
        System.out.println(Thread.currentThread().getName() + ": broadcasting " + s);
        manager.broadcast(s, Thread.currentThread());
    }

    public void setHost() {
        isHost = true;
        try {
            out.writeObject(new Pair<String, Boolean>("isHost", true));
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}