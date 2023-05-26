import java.net.*;
import java.awt.*;
import HashMap.MyHashMap;

import java.io.*;

public class ManagerThread implements Runnable {
    public static final double GRAVITY = .15; // , , , , ,
    public static final double JUMPLAYER_HEIGHT = 7;
    public static final double AIRRESISTANCE = .04;
    public static final double FRICTION = .8;
    public static final double GROUNDMOVEMENT = 1;
    public static final double AIRMOVEMENT = .2;
    public static final double MAXVELOCITY = 7;
    public static final double MINXVELOCITY = AIRMOVEMENT - .01;// MUST BE GREATER THAN FRICTION AND AIRRESISTANCE
    public static final double SMASH = .05;
    public static final double MAXBALLCOUNT = 20;

    private Manager manager;
    private MyHashMap<Thread, ServerThread> threadList;
    private boolean running = true;
    private MyHashMap<String, GameObjectStatus> gameObjects;
    // right, dash, mouseState, mouseX, mouseY,
    // touchingGround]}
    private MyHashMap<String, int[]> sendData;
    public static MyHashMap<String, double[]> balls, melee;
    private static final Map map = new Map();;

    public static final Platform[] walls = map.getIslands();
    private int timer;
    private int numBalls, numMelee;

    public ManagerThread(Manager manager) {
        this.manager = manager;

        gameObjects = new MyHashMap<String, GameObjectStatus>();
        sendData = new MyHashMap<String, int[]>();
        balls = new MyHashMap<String, double[]>();// x, y, xDirection, yDirection, lifetime
        melee = new MyHashMap<String, double[]>();// x, y, xDirection
        timer = 0;
        numBalls = 0;// number of balls created
        numMelee = 0;// number of melee attacks created

    }

    // this class is the main game calculations, it will run in a loop and update
    public void run() {
        while (running) {
            // each player
            balls.clear();
            melee.clear();
            for (String each : gameObjects.keySet()) {
                GameObjectStatus data = gameObjects.get(each);
                if (data instanceof Projectile) {
                    balls.put(each, new double[] { data.getXpos(), data.getYpos(), data.getVector().getXDirection(),
                            data.getVector().getYDirection(), ((Projectile) data).getLifetime() });
                }
                if (data instanceof Melee) {
                    
                    melee.put(each, new double[] { data.getXpos(), data.getYpos(), data.getVector().getXDirection() });
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
                if (data instanceof Melee) {
                    ((Melee) data).run();
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
            gameObjects.put("Ball-" + numBalls, new Projectile("Ball-" + numBalls, this));
            gameObjects.get("Ball-" + numBalls).setXpos(x);
            gameObjects.get("Ball-" + numBalls).setYpos(y);
            gameObjects.get("Ball-" + numBalls).setVector(v);
            numBalls++;
        }
    }

    public void summonMelee(double x, double y, Vector v, Player p) {
        gameObjects.put("M-" + numMelee, new Melee("M-" + numMelee,this));
        gameObjects.get("M-" + numMelee).setXpos(x);
        gameObjects.get("M-" + numMelee).setYpos(y);
        gameObjects.get("M-" + numMelee).setVector(v);
        numMelee++;
    }

    public void deleteBall(String name) {
        gameObjects.remove(name);
        numBalls--;
    }

    public void deleteMelee(String name) {
        gameObjects.remove(name);
        numMelee--;
    }

    public MyHashMap<String, double[]> getBalls() {
        return balls;
    }

    public MyHashMap<String, double[]> getMelee() {
        return melee;
    }

    public void broadcast(Pair<String, Object> pair) {
        manager.broadcast(pair);
    }
}