import java.net.*;
import java.awt.*;
import HashMap.MyHashMap;

import java.io.*;

public class ManagerThread implements Runnable {
    private Manager manager;
    private MyHashMap<Thread, ServerThread> threadList;
    private boolean running = true;
    private MyHashMap<String, Pair<Vector, Integer[]>> gameObjects;// [Name], {Vector, [Xpos, Ypos, up, down, left,
                                                                   // right, mouseState, mouseX, mouseY]}
    private MyHashMap<String, Integer[]> sendData;
    private Map map;
    private Rectangle[] walls;
    private int pWidth, pHeight;

    public ManagerThread(Manager manager) {
        this.manager = manager;
        map = new Map();
        walls = map.getWalls();
        pWidth = 10;// player width
        pHeight = 50;// player height
        gameObjects = new MyHashMap<String, Pair<Vector, Integer[]>>();
    }

    public void run() {
        while (running) {
            System.out.println("Running");
            // each player
            for (String each : gameObjects.keySet()) {
                Pair<Vector, Integer[]> pair = gameObjects.get(each);
                Vector v = pair.getKey();
                Integer[] nums = pair.getValue();
                // check if touching hitbox
                for (Rectangle r : walls) {
                    // if touching side, xDirection = 0, x pos subtract or add
                    int pX = nums[0];
                    int pY = nums[1];
                    int wX = (int) r.getX();
                    int wY = (int) r.getY();
                    int wW = (int) r.getWidth();
                    int wH = (int) r.getHeight();
                    if (pY < wY && pY + pHeight > wY + wH && wX > pX && wX < pX + pWidth) {
                        // touching left edge
                    }
                }

                v.setYDirection(v.getYDirection() + .1);
                System.out.println("Gravitating " + each + " " + v.getYDirection());
                pair.getValue()[0] += (int) v.getXDirection();
            }
            // send out all information
            manager.broadcast(new Pair<String, Object>("GameData", gameObjects), Thread.currentThread());
            try {
                Thread.sleep(15);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void setThreads(MyHashMap<Thread, ServerThread> threadList) {
        this.threadList = threadList;
        for (Thread each : threadList.keySet()) {// setup gameObjects (hashmap)
            System.out.println("Thread: " + each.getName());
            gameObjects.put(each.getName(),
                    new Pair<Vector, Integer[]>(new Vector(0, 0), new Integer[] { 50, 0, 0, 0, 0, 0, 0, 0, 0 }));
        }
        System.out.println("Ending loop");
    }

    public void updateThread(int[] keys, Thread thread) {
        for (int i = 0; i < keys.length; i++) {
            gameObjects.get(thread.getName()).getValue()[i + 2] = keys[i];
        }
        // test
        for (String each : gameObjects.keySet()) {
            sendData.put(each,
                    new Integer[] { gameObjects.get(each).getValue()[0], gameObjects.get(each).getValue()[1] });
        }
        manager.broadcast(new Pair<String, Object>("Test", sendData), Thread.currentThread());
    }
}