import HashMap.*;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.image.BufferedImage;
import HashMap.*;

import java.io.IOException;
import java.io.File;

import javax.imageio.ImageIO;

public class PlayerImages {

    private int[] nums;
    private int playerNum;
    private String name;
    private MyHashMap<String, BufferedImage> p1Assets;
    private MyHashMap<String, BufferedImage> p2Assets;
    private MyHashMap<String, BufferedImage> p3Assets;
    private MyHashMap<String, BufferedImage>[] assets;
    private DrawManipulation drawManipulation;

    private Thread clockThread;

    private BufferedImage currentBufferedImage;
    private BufferedImage currentBufferedFramed;

    // 1 = idle
    // 2 = running
    // 3 = jumping
    // 4 = temp
    // 5 = temp
    // private int currentImg;
    private int currentFrame;

    public PlayerImages(String name) {
        imageLoader();
        this.name = name;
        // this.nums = nums;
        if (name.equals("P1")) {
            playerNum = 1;
        } else if (name.equals("P2")) {
            playerNum = 2;
        } else if (name.equals("P3")) {
            playerNum = 3;
        } else {
            playerNum = 1;
            System.out.println("Invalid player number: " + name);
        }
        animationClockStart();

    }

    private void animationClockStart() {
        clockThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(150);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    currentFrame++;
                    if (currentFrame == 7) {
                        currentFrame = 0;
                    }
                }
            }
        });
        clockThread.start();

    }

    public void imageLoader() {
        p1Assets = new MyHashMap<String, BufferedImage>();
        p2Assets = new MyHashMap<String, BufferedImage>();
        p3Assets = new MyHashMap<String, BufferedImage>();
        assets = new MyHashMap[3];
        assets[0] = p1Assets;
        assets[1] = p2Assets;
        assets[2] = p3Assets;

        for (int i = 0; i < 3; i++) {
            try {

                assets[i].put("Idle",
                        ImageIO.read(getClass().getResource("/assets/src/player/Knight_1/Idle.png")));

                assets[i].put("Run",
                        ImageIO.read(getClass().getResource("/assets/src/player/Knight_1/Run.png")));

            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }

        }

    }

    public void update(int[] nums) {
        this.nums = nums;
    }

    // future implementation of this updateimage method
    /**
     * private void updateImg() throws IOException {
     * int img = nums[3];
     * if (img / 100 == 0) {// idle
     * currentBufferedImage = assets[playerNum - 1].get("Idle");
     * } else if (img / 100 == 1) {// running
     * 
     * } else if (img / 100 == 2) {// jumping
     * 
     * } else if (img / 100 == 3) {// temp
     * 
     * } else if (img / 100 == 4) {// temp
     * 
     * } else if (img / 100 == 5) {// temp\
     * 
     * }
     * }
     **/

    // temp implementation of updateimage method to make sure sprite atlas works
    private void updateimage(Graphics g, int x, int y) throws IOException {
        currentBufferedImage = p1Assets.get("Run");
        currentBufferedFramed = currentBufferedImage.getSubimage(currentFrame * Player.PLAYER_WIDTH, 0,
                Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(currentBufferedFramed, x, y, null);

    }

    public void draw(Graphics g, int x, int y) {
        try {
            updateimage(g, x, y);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
