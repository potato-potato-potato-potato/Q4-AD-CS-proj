import java.net.*;
import java.awt.*;
import HashMap.MyHashMap;

import java.io.*;

public class ManagerThread implements Runnable {
    private Manager manager;
    private MyHashMap<Thread, ServerThread> threadList;
    private boolean running = true;
    private MyHashMap<String, Pair<Vector, int[]>> gameObjects;// [Name], {Vector, [Xpos, Ypos, up, down, left, right, dash, mouseState, mouseX, mouseY]}
    private MyHashMap<String, int[]> sendData;
    private Map map;
    private Rectangle[] walls;
    private int pWidth, pHeight;

    public ManagerThread(Manager manager) {
        this.manager = manager;
        map = new Map();
        walls = map.getWalls();
        pWidth = 10;// player width
        pHeight = 50;// player height
        gameObjects = new MyHashMap<String, Pair<Vector, int[]>>();
        sendData = new MyHashMap<String, int[]>();
    }

    // this class is the main game calculations, it will run in a loop and update
    public void run() {
        while (running) {
            // each player
            for (String each : gameObjects.keySet()) {
                Pair<Vector, int[]> pair = gameObjects.get(each);
                Vector v = pair.getKey();
                int[] nums = pair.getValue();
                int pX = nums[0];
                int pY = nums[1];
                for(int i = 0; i < 5; i++){
                    System.out.println(i + " : " + nums[i]);
                }
                System.out.println();
                // check if touching hitbox
                for (Rectangle r : walls) {
                    // if touching side, xDirection = 0, x pos subtract or add
                    
                    int wX = (int) r.getX();
                    int wY = (int) r.getY();
                    int wW = (int) r.getWidth();
                    int wH = (int) r.getHeight();
                    if (pY < wY && pY + pHeight > wY + wH && wX > pX && wX < pX + pWidth) {
                        // TODO: touching left edge
                    }
                }
                if(pY>600){
                    v.setYDirection(0);
                }
                if(nums[2]==1){//up
                    v.setYDirection(v.getYDirection()-.2);
                }
                if(nums[3]==1){//down

                }
                if(nums[4]==1){//left
                    v.setXDirection(v.getXDirection()-.1);
                }
                if(nums[5]==1){//right
                    v.setXDirection(v.getXDirection()+.1);
                }
                if(nums[6]==1){//dash

                }
                if(nums[7]==1){//fire
                    System.out.println("Firing");
                }
                if(v.getYDirection()>15){
                    v.setYDirection(15);
                }
                if(v.getXDirection()>15){
                    v.setXDirection(15);
                }
                if(v.getXDirection()<-15){
                    v.setXDirection(-15);
                }
        

                v.setYDirection(v.getYDirection() + .1);
                nums[1] += (int) v.getYDirection();
                nums[2] += (int) v.getXDirection();

                gameObjects.get(each).setKey(v);
                gameObjects.get(each).setValue(nums);

            }

            // send out all information
            for (String each : gameObjects.keySet()) {
                sendData.put(each, new int[] { gameObjects.get(each).getValue()[0], gameObjects.get(each).getValue()[1] });
            }
            manager.broadcast(new Pair<String, Object>("gameData", sendData));
            sendData = new MyHashMap<String, int[]>();//reset sendData
            try {
                Thread.sleep(15);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    // this is needed to pass all the player information to the managerThread
    // it should only be called once, when the game starts
    public void setThreads(MyHashMap<Thread, ServerThread> threadList) {
        this.threadList = threadList;
        int num = 1;
        for (Thread each : threadList.keySet()) {// setup gameObjects (hashmap)
            num++;
            gameObjects.put(each.getName(), new Pair<Vector, int[]>(new Vector(0, 0), new int[] { num*50, 10, 0, 0, 0, 0, 0, 0, 0, 0 }));
        }
        System.out.println("GameObjects:" + gameObjects.keySet());
    }

    public void updateThread(int[] keys, Thread thread) {
        for (int i = 0; i < keys.length; i++) {
            System.out.println(i + " Updated to : " + keys[i] + " in " + thread.getName());
        }
        System.out.println("Updating keys");
        for (int i = 0; i < keys.length; i++) {
            gameObjects.get(thread.getName()).getValue()[i + 2] = keys[i];
        }
    }
}