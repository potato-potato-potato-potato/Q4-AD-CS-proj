import java.net.*;
import java.awt.*;
import HashMap.MyHashMap;

import java.io.*;

public class ManagerThread implements Runnable {
    private Manager manager;
    private MyHashMap<Thread, ServerThread> threadList;
    private boolean running = true;
    private MyHashMap<String, Pair<Vector, double[]>> gameObjects;// [Name], {Vector, [Xpos, Ypos, up, down, left, right, dash, mouseState, mouseX, mouseY, touchingGround]}
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
        gameObjects = new MyHashMap<String, Pair<Vector, double[]>>();
        sendData = new MyHashMap<String, int[]>();
    }

    // this class is the main game calculations, it will run in a loop and update
    public void run() {
        while (running) {
            // each player
            for (String each : gameObjects.keySet()) {
                Pair<Vector, double[]> pair = gameObjects.get(each);
                Vector v = pair.getKey();
                double[] nums = pair.getValue();
                double pX = nums[0];
                double pY = nums[1];
                v.setYDirection(v.getYDirection() + .1);
                if(pY>800){//out of bounds
                    nums[1] = 50;
                    v.setYDirection(0);
                    v.setXDirection(0);
                }
                if(nums[2]==1){//up
                    if(nums[10]==1){
                        System.out.println("Jumping");
                        v.setYDirection(v.getYDirection()-5);
                    }
                }
                if(nums[3]==1){//down

                }
                if(nums[4]==1){//left
                    if(nums[10]==1){
                        v.setXDirection(v.getXDirection()-.2);
                    }
                    v.setXDirection(v.getXDirection()-.08);
                }
                if(nums[5]==1){//right
                    if(nums[10]==1){
                        v.setXDirection(v.getXDirection()+.2);
                    }
                    v.setXDirection(v.getXDirection()+.08);                
                }
                if(nums[6]==1){//dash

                }
                if(nums[7]==1){//fire
                }
                if(v.getXDirection()!=0){
                    if(v.getXDirection()>.1){
                        v.setXDirection(v.getXDirection()-.05);
                    } else if(v.getXDirection()<-.1){
                        v.setXDirection(v.getXDirection()+.05);
                    }
                    else{
                        v.setXDirection(0);
                    }
                    
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
                for (Rectangle r : walls) {
                    // if touching side, xDirection = 0, x pos subtract or add
                    
                    int wX = (int) r.getX();
                    int wY = (int) r.getY();
                    int wW = (int) r.getWidth();
                    int wH = (int) r.getHeight();
                    

                    if (pY < wY+wH && pY + pHeight > wY+1 && wX > pX && wX < pX + pWidth) {
                        // TODO: touching left edge
                        if(v.getXDirection()>0){
                            v.setXDirection(0);
                            nums[0] = wX;
                        }
                    }else if (pY < wY+wH && pY + pHeight > wY+1 &&  pX<wX+wW &&  pX + pWidth>wX+wW) {
                        // TODO: touching right edge
                        if(v.getXDirection()<0){
                            v.setXDirection(0);
                            nums[0] = wX+wW;
                        }
                    } else if(pY+pHeight<wY+wH && pY+pHeight>=wY-.1 && pX+pWidth>wX && pX<wX+wW){
                        //touching top edge
                        nums[1] = wY-pHeight;
                        nums[10] = 1;
                        if(v.getYDirection()>0){
                            v.setYDirection(0);
                        }
                    }
                    else if(v.getYDirection()!=0){
                        nums[10]=0;
                    }
                }

                nums[1] += v.getYDirection();
                nums[0] += v.getXDirection();
                
                gameObjects.get(each).setKey(v);
                gameObjects.get(each).setValue(nums);

            }

            // send out all information
            for (String each : gameObjects.keySet()) {
                sendData.put(each, new int[] { (int)gameObjects.get(each).getValue()[0], (int)gameObjects.get(each).getValue()[1] });
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
            gameObjects.put(each.getName(), new Pair<Vector, double[]>(new Vector(0, 0), new double[] { num*50, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
        }
        System.out.println("GameObjects:" + gameObjects.keySet());
    }

    public void updateThread(int[] keys, Thread thread) {
        System.out.println("Up: " + keys[0]);
        System.out.println("down: " + keys[1]);
        System.out.println("left: " + keys[2]);
        System.out.println("right: " + keys[3]);
        System.out.println("dash: " + keys[4]);
        System.out.println("mouseState: " + keys[5]);
        System.out.println("mouseX: " + keys[6]);
        System.out.println("mouseY: " + keys[7]);

        for (int i = 0; i < keys.length; i++) {
            gameObjects.get(thread.getName()).getValue()[i + 2] = keys[i];
        }
    }
}