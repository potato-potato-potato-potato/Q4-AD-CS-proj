import HashMap.*;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.Point;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import HashMap.*;

import java.io.IOException;
import java.net.URL;
import java.io.File;

import javax.imageio.ImageIO;

public class PlayerImages {

    private int[] nums;
    private int playerNum;
    private String name;
    private MyHashMap<Integer, BufferedImage> p1Assets;
    private MyHashMap<Integer, BufferedImage> p2Assets;
    private MyHashMap<Integer, BufferedImage> p3Assets;
    private MyHashMap<Integer, BufferedImage>[] assets;
    private DrawManipulation drawManipulation;
    private AffineTransform transform;
    private AffineTransformOp op;
    private String path;

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

    public PlayerImages(int playerNum) {
        imageLoader();

        this.playerNum = playerNum;
        System.out.println("PlayerImages: " + playerNum);

    }

    public void imageLoader() {
        p1Assets = new MyHashMap<Integer, BufferedImage>();
        p2Assets = new MyHashMap<Integer, BufferedImage>();
        p3Assets = new MyHashMap<Integer, BufferedImage>();
        assets = new MyHashMap[3];

        assets[0] = p1Assets;
        assets[1] = p2Assets;
        assets[2] = p3Assets;

        for (int i = 0; i < 3; i++) {
            System.out.println("Loading player " + (i) + " assets");
            try {
                path = "/assets/src/player/k" + (i + 1);
                System.out.println(path);
                // Attack1 =0
                // Attack2 =1
                // Attack3 =2
                // Dead =3
                // Defend =4
                // Hurt =5
                // Idle =6
                // Jump =7
                // Run =8

                assets[i].put(0, ImageIO.read(getClass().getResource(path + "/Attack1.png")));
                assets[i].put(1, ImageIO.read(getClass().getResource(path + "/Attack2.png")));
                assets[i].put(2, ImageIO.read(getClass().getResource(path + "/Attack3.png")));
                assets[i].put(3, ImageIO.read(getClass().getResource(path + "/Dead.png")));
                assets[i].put(4, ImageIO.read(getClass().getResource(path + "/Defend.png")));
                assets[i].put(5, ImageIO.read(getClass().getResource(path + "/Hurt.png")));
                assets[i].put(6, ImageIO.read(getClass().getResource(path + "/Idle.png")));
                assets[i].put(7, ImageIO.read(getClass().getResource(path + "/Jump.png")));
                assets[i].put(8, ImageIO.read(getClass().getResource(path + "/Run.png")));

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

    public void updateImg(int number) {
        try {
            int action = number;
            currentBufferedImage = assets[playerNum].get(action / 100);
            updateImg(currentBufferedImage, action % 100);

        } catch (Exception e) {
            System.out.println("++++++++++++++++++++++++++++++++++++");
            System.out.println("Current PlayerImages in render: " + number);
            e.printStackTrace();
            System.out.println("++++++++++++++++++++++++++++++++++++");
            System.exit(1);
        }

    }

    // public void updateImg(int number) {
    // try {
    // int action = number;
    // if (action / 100 == 6) {// idle
    // currentBufferedImage = assets[playerNum].get("Idle");
    // updateImg(currentBufferedImage, action % 100);
    // } else if (action / 100 == 8) {// run
    // currentBufferedImage = assets[playerNum].get("Run");
    // System.out.println("runing");
    // updateImg(currentBufferedImage, action % 100);
    // } else {
    // currentBufferedImage = assets[playerNum].get("Idle");
    // updateImg(currentBufferedImage, action % 100);
    // }

    // } catch (Exception e) {
    // System.out.println("++++++++++++++++++++++++++++++++++++");
    // System.out.println("Current PlayerImages in render: " + number);
    // System.out.println("Current Player in render: " + playerNum);
    // System.out.println(currentFrame);
    // System.out.println(
    // "Current atempted subimage dimension: x-pos:" + currentFrame *
    // Player.PLAYER_IMGWIDTH + " y-pos:"
    // + 0 + " width:" + Player.PLAYER_IMGWIDTH + " height:" +
    // Player.PLAYER_IMGHEIGHT);
    // System.out.println("Current buffered image dimension: width:" +
    // currentBufferedImage.getWidth(null)
    // + " height:" + currentBufferedImage.getHeight(null));
    // e.printStackTrace();
    // System.out.println("++++++++++++++++++++++++++++++++++++");
    // System.exit(1);
    // }

    // }

    private void updateImg(BufferedImage img, int Frame) {
        // img = assets[playerNum].get(8);
        int currentFrame = Frame / 10;
        System.out.println("Current Frame: " + currentFrame);
        currentBufferedFramed = img.getSubimage(currentFrame * Player.PLAYER_IMGWIDTH, 0, Player.PLAYER_IMGWIDTH,
                Player.PLAYER_IMGHEIGHT);
        int currentDirection = Frame % 10;
        if (currentDirection == 0) {
            transform = AffineTransform.getScaleInstance(-1, 1);
            transform.translate(-currentBufferedFramed.getWidth(null), 0);
            op = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            currentBufferedFramed = op.filter(currentBufferedFramed, null);
        }

    }

    // temp implementation of updateimage method to make sure sprite atlas works
    private void drawImage(Graphics g, int x, int y) throws IOException {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(currentBufferedFramed, x, y, null);

    }

    public void draw(Graphics g, int x, int y) {
        try {
            drawImage(g, x + Player.IMG_OFFSET, y);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
