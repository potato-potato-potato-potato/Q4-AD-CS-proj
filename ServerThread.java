import java.net.*;
import java.io.*;
import HashMap.MyHashMap;

public class ServerThread implements Runnable {
    private Socket clientSocket;

    private Manager manager;
    public ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean close, isHost;
    private String name;
    private Pair<String, Object> input;
    private Map map;
    private boolean isUserinput;
    private int[] userInput;

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
                System.out.println("thread name:" + Thread.currentThread());
                out.reset();
                out.writeObject(new Pair<String, Boolean>("isHost", true));
                isHost = true;
            } else {
                out.reset();
                out.writeObject(new Pair<String, Boolean>("isHost", false));
                isHost = false;
            }
            while (true) {
                isUserinput = false;
                if (in.readObject() instanceof Pair) {
                    input = (Pair<String, Object>) in.readObject();
                } else if (in.readObject() instanceof int[]) {
                    userInput = (int[]) in.readObject();
                    isUserinput = true;
                    System.out.println("User input received");
                }

                if (isUserinput) {// remove this thread if client disconnects, reassign host
                    // if nessescary
                    manager.updateThread(userInput, Thread.currentThread());
                } else if (input.getKey().equals("threadname")) {
                    name = (String) input.getValue();
                    System.out.println("Received name: " + name);
                } else if (input.getKey().equals("game")) {
                    manager.broadcast(input, Thread.currentThread());
                } else if (input.getKey().equals("StartGame")) {
                    System.out.println(Thread.currentThread().getName() + ": Starting Game");
                    manager.broadcast(input, Thread.currentThread());
                    manager.start();
                } else if (input.getKey().equals("Quit")) {// remove this thread if client disconnects, reassign host if
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

    public void send(Pair<String, Object> s) {
        try {
            if (s.getKey().equals("gameData")) {
                for (String each : ((MyHashMap<String, int[]>) s.getValue()).keySet()) {
                }
            }
            out.reset();
            out.writeObject(s);
        } catch (IOException e) {
            System.out.println("Error listening for a connection");
            e.printStackTrace();
        }
    }

    public Map getMap() {
        return map;
    }

    public void broadcast(Pair<String, Object> s) {
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