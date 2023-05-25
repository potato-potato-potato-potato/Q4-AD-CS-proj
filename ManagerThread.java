import java.net.*;
import java.awt.*;
import HashMap.MyHashMap;

import java.io.*;

public class ManagerThread implements Runnable {
    public static final double GRAVITY = .15; // , , , , ,
    public static final double JUMPLAYER_HEIGHT = 3.5;
    public static final double AIRRESISTANCE = .02;
    public static final double FRICTION = .25;
    public static final double GROUNDMOVEMENT = .2;
    public static final double AIRMOVEMENT = .1;
    public static final double MAXVELOCITY = 15;
    public static final double MINXVELOCITY = .05;// MUST BE GREATER THAN FRICTION AND AIRRESISTANCE
    public static final double SMASH = .05;
    public static final double MAXBALLCOUNT = 20;

    private Manager manager;
    private MyHashMap<Thread, ServerThread> threadList;
    private boolean running = true;
    private MyHashMap<String, GameObjectStatus> gameObjects;
    // right, dash, mouseState, mouseX, mouseY,
    // touchingGround]}
    private MyHashMap<String, int[]> sendData;
    public static MyHashMap<String, double[]> balls;
    private static final Map map = new Map();;

    public static final Platform[] walls = map.getIslands();
    private int timer;
    private int numBalls;

    public ManagerThread(Manager manager) {
        this.manager = manager;

        gameObjects = new MyHashMap<String, GameObjectStatus>();
        sendData = new MyHashMap<String, int[]>();
        balls = new MyHashMap<String, double[]>();
        timer = 0;
        numBalls = 0;// number of balls created
    }

    // this class is the main game calculations, it will run in a loop and update
    public void run() {
        while (running) {
            // each player
            balls.clear();
            for (String each : gameObjects.keySet()) {
                GameObjectStatus data = gameObjects.get(each);
                if (data instanceof Projectile) {
                    balls.put(each, new double[] { data.getXpos(), data.getYpos(), data.getVector().getXDirection(),
                            data.getVector().getYDirection(), ((Projectile) data).getLifetime() });
                }
            }
            for (String each : gameObjects.keySet()) {
                GameObjectStatus data = gameObjects.get(each);
                if (data instanceof Player) {
                    ((Player) data).run();
                }
                if (data instanceof Projectile) {
                    ((Projectile) data).run();
                }
            }

            // send out all information
            for (String each : gameObjects.keySet()) {
                sendData.put(each, new int[] { (int) gameObjects.get(each).getXpos(),
                        (int) gameObjects.get(each).getYpos(), (int) gameObjects.get(each).getImgStatus() });
            }
            broadcast(new Pair<String, Object>("gameData", sendData));
            sendData = new MyHashMap<String, int[]>();// reset sendData
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
            broadcast(new Pair<String, Object>("newPlayer", num - 1));// -1 is to fit index system in clientscreen
            num++;
            gameObjects.put(each.getName(), new Player(each.getName(), this));
            gameObjects.get(each.getName()).setXpos(num * 50);
            gameObjects.get(each.getName()).setYpos(10);
            gameObjects.get(each.getName()).setImgStatus(0);

        }
        System.out.println("GameObjects:" + gameObjects.keySet());
    }

    public void updateThread(int[] keys, Thread thread) {
        gameObjects.get(thread.getName()).updateKeyStatus(keys);
    }

    public void summonFireBall(double x, double y, Vector v) {
        if (numBalls < MAXBALLCOUNT) {
            gameObjects.put("Ball-" + numBalls, new Projectile("Ball", this));
            gameObjects.get("Ball-" + numBalls).setXpos(x);
            gameObjects.get("Ball-" + numBalls).setYpos(y);
            gameObjects.get("Ball-" + numBalls).setVector(v);
            numBalls++;
        }
    }

    public void deleteBall(String name) {
        gameObjects.remove(name);
        numBalls--;
    }

    public MyHashMap<String, double[]> getBalls() {
        return balls;
    }
    public void broadcast(Pair<String, Object> pair){
        manager.broadcast(pair);
    }
}