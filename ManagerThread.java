import java.net.*;
import java.awt.*;
import HashMap.MyHashMap;

import java.io.*;
public class ManagerThread implements Runnable {
    public static final double GRAVITY = .3; // , , , , ,
    public static final double JUMPLAYER_HEIGHT = 7;
    public static final double AIRRESISTANCE = .04;
    public static final double FRICTION = .8;
    public static final double GROUNDMOVEMENT = .2;
    public static final double AIRMOVEMENT = .15;
    public static final double MAXVELOCITY = 7;
    public static final double MINXVELOCITY = AIRMOVEMENT - .01;// MUST BE GREATER THAN FRICTION AND AIRRESISTANCE
    public static final double SMASH = .05;
    public static final double MAXBALLCOUNT = 20;

    private Manager manager;
    private boolean running = true;
    private MyHashMap<String, GameObjectStatus> gameObjects;
    // right, dash, mouseState, mouseX, mouseY,
    // touchingGround]}
    private MyHashMap<String, int[]> sendData;
    public static MyHashMap<String, Melee> melee;
    public static MyHashMap<String, Projectile> balls;
    public static MyHashMap<String, Player> playersAlive;

    private static final Map map = new Map();;

    public static final Platform[] walls = map.getIslands();
    private int numBalls, numMelee;

    public ManagerThread(Manager manager) {
        this.manager = manager;

        gameObjects = new MyHashMap<String, GameObjectStatus>();
        sendData = new MyHashMap<String, int[]>();
        balls = new MyHashMap<String, Projectile>();// x, y, xDirection, yDirection, lifetime
        melee = new MyHashMap<String, Melee>();// x, y, xDirection
        playersAlive = new MyHashMap<String, Player>();
        numBalls = 0;// number of balls created
        numMelee = 0;// number of melee attacks created

    }

    // this class is the main game calculations, it will run in a loop and update
    public void run() {
        while (running) {
            // each player
            balls.clear();
            melee.clear();
            playersAlive.clear();
            for (String each : gameObjects.keySet()) {
                GameObjectStatus data = gameObjects.get(each);
                if (data instanceof Projectile) {
                    balls.put(each, (Projectile) data);
                }
                if (data instanceof Melee) {

                    melee.put(each, (Melee) data);
                }
                if (data instanceof Player) {
                    if(!((Player) data).isDead()){
                        playersAlive.put(each, (Player) data);
                    }
                }
            }
            if(playersAlive.size()==1){
                for(String each:playersAlive.keySet()){
                    broadcast(new Pair<String, Object>("PlayerWon", Integer.valueOf(each.substring(7))+1));
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
            // vector is multiplied by 1000 to send it as an int instead of a double
            for (String each : gameObjects.keySet()) {
                sendData.put(each, new int[] { (int) gameObjects.get(each).getXpos(),
                        (int) gameObjects.get(each).getYpos(), (int) gameObjects.get(each).getImgStatus(),
                        (int) (gameObjects.get(each).getVector().xDir * 1000),
                        (int) (gameObjects.get(each).getVector().yDir * 1000) });
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
        int num = 1;
        gameObjects.clear();
        for (Thread each : threadList.keySet()) {// setup gameObjects (hashmap)
            broadcast(new Pair<String, Object>("newPlayer", num - 1));// -1 is to fit index system in clientscreen
            num++;
            gameObjects.put(each.getName(), new Player(each.getName(), this));
            gameObjects.get(each.getName()).setXpos(num * 50+400);
            gameObjects.get(each.getName()).setYpos(10);
            gameObjects.get(each.getName()).setImgStatus(0);

        }
    }

    public void updateThread(int[] keys, Thread thread) {
        gameObjects.get(thread.getName()).updateKeyStatus(keys);
    }

    public void summonFireBall(double x, double y, Vector v) {
        if (numBalls < MAXBALLCOUNT) {
            manager.broadcast(new Pair<String, Object>("newBall", "Ball-" + numBalls));
            gameObjects.put("Ball-" + numBalls, new Projectile("Ball-" + numBalls, numBalls, this, 0, 0));
            gameObjects.get("Ball-" + numBalls).setXpos(x);
            gameObjects.get("Ball-" + numBalls).setYpos(y);
            gameObjects.get("Ball-" + numBalls).setVector(v);
            numBalls++;
        }
    }

    public void summonMelee(double x, double y, Vector v, Player p) {
        manager.broadcast(new Pair<String, Object>("newMelee", "M-" + numMelee));
        gameObjects.put("M-" + numMelee, new Melee("M-" + numMelee, this, p));
        gameObjects.get("M-" + numMelee).setXpos(x);
        gameObjects.get("M-" + numMelee).setYpos(y);
        gameObjects.get("M-" + numMelee).setVector(v);
        numMelee++;
    }

    public void deleteBall(String name) {
        int b = Integer.parseInt(name.substring(4));
        gameObjects.remove(name);
        manager.broadcast(new Pair<String, Object>("deleteBall", b));
        if (numBalls >= 19) {
            numBalls = 0;
        }
    }

    public void deleteMelee(String name) {
        gameObjects.remove(name);
        if (numMelee > 20) {
            numMelee = 0;

        }
    }

    public MyHashMap<String, Projectile> getBalls() {
        return balls;
    }

    public MyHashMap<String, Melee> getMelee() {
        return melee;
    }

    public void broadcast(Pair<String, Object> pair) {
        manager.broadcast(pair);
    }

    public void reset() {
        for (String each : gameObjects.keySet()) {
            GameObjectStatus data = gameObjects.get(each);
            if (data instanceof Player) {
                ((Player)data).setXpos(400);
                ((Player)data).setYpos(10);
                ((Player)data).setVector(new Vector(0, 0));
                ((Player)data).setVector(new Vector(0, 0));
                broadcast(new Pair<String, Object>("GameReset", this));
            }
        }
    }
}