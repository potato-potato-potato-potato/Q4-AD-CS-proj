import java.net.*;

import HashMap.MyHashMap;

import java.io.*;

public class ManagerThread implements Runnable {
    private Manager manager;
    private MyHashMap<Thread, ServerThread> threadList;
    private boolean running = true;
    private MyHashMap<String, Pair<Vector, Integer[]>> clients;// [Name], {Vector, [Xpos, Ypos, up, down, left, right,
                                                               // fire, mouseState, mouseX, mouseY]}
    private Map map;

    public ManagerThread(Manager manager) {
        this.manager = manager;
        map = manager.getMap();
    }

    @SuppressWarnings("unchecked")
    public void run() {
        while (running) {
            for (String each : clients.keySet()) {
                Pair<Vector, Integer[]> pair = clients.get(each);
                Vector v = pair.getKey();
                v.setYDirection(v.getYDirection() + .1);
                System.out.println("Gravitating " + each + " " + v.getYDirection());
            }
            // send out all information
            manager.broadcast(new Pair<String, Object>("GameData", clients), Thread.currentThread());
            try {
                Thread.sleep(15);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void setThreads(MyHashMap<Thread, ServerThread> threadList) {
        this.threadList = threadList;
        for (Thread each : threadList.keySet()) {// setup clients (hashmap)
            ServerThread cur = threadList.get(each);
            clients.put(cur.getName(),
                    new Pair<Vector, Integer[]>(new Vector(0, 0), new Integer[] { 50, 0, 0, 0, 0, 0, 0, 0, 0, 0 }));
        }
    }
}
