import java.net.*;

import HashMap.MyHashMap;

import java.io.*;

public class ManagerThread implements Runnable {
    private Manager manager;
    private MyHashMap<Thread, ServerThread> threadList;
    private boolean running = true;
    private MyHashMap<String, Pair<Vector, Integer[]>> clients;// Name, Vector, Xpos, Ypos, up, down, left right, fire, mouseState, mouseX, mouseY
    private Map map;
    public ManagerThread(Manager manager) {
        this.manager = manager;
        map = manager.getMap();
    }

    @SuppressWarnings("unchecked")
    public void run() {
        while (running){
            for()
            try {
                Thread.sleep(15);
            }
            catch (Exception e) {
                System.out.println(e);
            }
        }
    }
    public void setThreads(MyHashMap<Thread, ServerThread> threadList){
        this.threadList = threadList;
    }
}
