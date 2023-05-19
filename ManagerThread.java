import java.net.*;
import java.awt.*;
import HashMap.MyHashMap;

import java.io.*;

public class ManagerThread implements Runnable {
    private static final double GRAVITY = .15; // , , , , ,
    private static final double JUMPHEIGHT = 3.5;
    private static final double AIRRESISTANCE = .02;
    private static final double FRICTION = .04;
    private static final double GROUNDMOVEMENT = .2;
    private static final double AIRMOVEMENT = .1;
    private static final double MAXVELOCITY = 15;
    private static final double MINXVELOCITY = .05;// MUST BE GREATER THAN FRICTION AND AIRRESISTANCE
    private static final double SMASH = .1;

    private Manager manager;
    private MyHashMap<Thread, ServerThread> threadList;
    private boolean running = true;
    private MyHashMap<String, GameObjectStatus> gameObjects;// [Name], {Vector, [Xpos, Ypos, up, down, left,
    // right, dash, mouseState, mouseX, mouseY,
    // touchingGround]}
    private MyHashMap<String, int[]> sendData;
    private Map map;
    private Rectangle[] walls;
    private int pWidth, pHeight, timer;

    public ManagerThread(Manager manager) {
        this.manager = manager;
        map = new Map();
        walls = map.getWalls();
        pWidth = 10;// player width
        pHeight = 50;// player height
        gameObjects = new MyHashMap<String, GameObjectStatus>();
        sendData = new MyHashMap<String, int[]>();
        timer = 0;

    }

    // this class is the main game calculations, it will run in a loop and update
    public void run() {
        while (running) {
            // each player
            timer++;
            for (String each : gameObjects.keySet()) {
                GameObjectStatus data = gameObjects.get(each);
                Vector v = data.getVector();
                double pX = data.getXpos();
                double pY = data.getYpos();
                v.setYDirection(v.getYDirection() + GRAVITY);
                if (pY > 800) {// out of bounds
                    data.setYpos(50);
                    v.setYDirection(0);
                    v.setXDirection(0);
                }
                if (data.isUp()) {// up
                    if (data.isTouchingGround() == true) {
                        v.setYDirection(v.getYDirection() - JUMPHEIGHT);
                        data.setTouchingGround(false);
                    } else {
                        v.setYDirection(v.getYDirection() - SMASH);
                    }
                }
                if (data.isDown()) {// down
                    if (data.isTouchingGround() == false) {
                        v.setYDirection(v.getYDirection() + SMASH);
                    }
                }
                if (data.isLeft()) {// left
                    if (data.isTouchingGround()) {
                        v.setXDirection(v.getXDirection() - GROUNDMOVEMENT);

                    }
                    v.setXDirection(v.getXDirection() - AIRMOVEMENT);
                }
                if (data.isRight()) {// right
                    if (data.isTouchingGround()) {
                        v.setXDirection(v.getXDirection() + GROUNDMOVEMENT);

                    }
                    v.setXDirection(v.getXDirection() + AIRMOVEMENT);
                }
                if (data.isDash()) {// dash

                }
                if (data.isLeftMouseState()) {// fire
                }
                if (v.getXDirection() != 0) {
                    if (v.getXDirection() > MINXVELOCITY) {
                        if (data.isTouchingGround() == false) {
                            v.setXDirection(v.getXDirection() - AIRRESISTANCE);
                        } else {
                            v.setXDirection(v.getXDirection() - FRICTION);
                            if (data.isUp()) {// slow down more if down arrow is pressed
                                v.setXDirection(v.getXDirection() - 2 * FRICTION);
                            }

                        }
                    } else if (v.getXDirection() < -MINXVELOCITY) {
                        if (data.isTouchingGround() == false) {
                            v.setXDirection(v.getXDirection() + AIRRESISTANCE);
                        } else {
                            v.setXDirection(v.getXDirection() + FRICTION);
                            if (data.isUp()) {// slow down more if down arrow is pressed
                                v.setXDirection(v.getXDirection() + 2 * FRICTION);
                            }
                        }
                    } else {
                        v.setXDirection(0);
                    }

                }
                if (v.getYDirection() > MAXVELOCITY) {
                    v.setYDirection(MAXVELOCITY);
                }
                if (v.getXDirection() > MAXVELOCITY) {
                    v.setXDirection(MAXVELOCITY);
                }
                if (v.getXDirection() < -MAXVELOCITY) {
                    v.setXDirection(-MAXVELOCITY);
                }
                for (Rectangle r : walls) {
                    // if touching side, xDirection = 0, x pos subtract or add
                    int wX = (int) r.getX();
                    int wY = (int) r.getY();
                    int wW = (int) r.getWidth();
                    int wH = (int) r.getHeight();

                    if (pY < wY + wH && pY + pHeight > wY + 1 && pX < wX && pX + pWidth > wX) {
                        // TODO: touching left edge
                        if (v.getXDirection() > 0) {
                            v.setXDirection(0);
                            data.setXpos(wX - pWidth);
                        }
                    } else if (pY < wY + wH && pY + pHeight > wY + 1 && pX < wX + wW && pX + pWidth > wX + wW) {
                        // TODO: touching right edge
                        if (v.getXDirection() < 0) {
                            v.setXDirection(0);
                            data.setXpos(wX + wW);
                        }
                    } else if (pY + pHeight < wY + wH && pY + pHeight >= wY - .1 && pX + pWidth > wX && pX < wX + wW) {
                        // touching top edge
                        data.setYpos(wY - pHeight);
                        data.setTouchingGround(true);
                        if (timer % 10 == 0) {

                        }
                        if (v.getYDirection() > 0) {
                            v.setYDirection(0);
                        }
                    } else if (pY < wY + wH && pY > wY && pX + pWidth > wX && pX < wX + wW) {
                        // touching top edge
                        data.setYpos(wY + wH);
                        if (v.getYDirection() < 0) {
                            v.setYDirection(0);
                        }
                    }
                }
                data.translateXpos(v.getXDirection());
                data.translateYpos(v.getYDirection());

            }

            // send out all information
            for (String each : gameObjects.keySet()) {
                sendData.put(each,
                        new int[] { (int) gameObjects.get(each).getXpos(), (int) gameObjects.get(each).getYpos() });
            }
            manager.broadcast(new Pair<String, Object>("gameData", sendData));
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
            num++;
            gameObjects.put(each.getName(), new GameObjectStatus());
            gameObjects.get(each.getName()).setXpos(num * 50);
            gameObjects.get(each.getName()).setYpos(10);
        }
        System.out.println("GameObjects:" + gameObjects.keySet());
    }

    public void updateThread(int[] keys, Thread thread) {
        gameObjects.get(thread.getName()).updateKeyStatus(keys);
    }
}