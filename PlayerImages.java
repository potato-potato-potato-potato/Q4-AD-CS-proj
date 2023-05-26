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
    private MyHashMap<String, BufferedImage> p1Assets;
    private MyHashMap<String, BufferedImage> p2Assets;
    private MyHashMap<String, BufferedImage> p3Assets;
    private MyHashMap<String, BufferedImage>[] assets;
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
        p1Assets = new MyHashMap<String, BufferedImage>();
        p2Assets = new MyHashMap<String, BufferedImage>();
        p3Assets = new MyHashMap<String, BufferedImage>();
        assets = new MyHashMap[3];

        assets[0] = p1Assets;
        assets[1] = p2Assets;
        assets[2] = p3Assets;

        for (int i = 0; i < 3; i++) {
            System.out.println("Loading player " + (i) + " assets");
            try {
                path = "/assets/src/player/k" + (i + 1);
                System.out.println(path);
                assets[i].put("Idle",
                        ImageIO.read(getClass().getResource(path + "/Idle.png")));
                assets[i].put("Run",
                        ImageIO.read(getClass().getResource(path + "/Run.png")));
                assets[i].put("Jump",
                        ImageIO.read(getClass().getResource(path + "/Jump.png")));
                assets[i].put("Attack1",
                        ImageIO.read(getClass().getResource(path + "/Attack1.png")));
                assets[i].put("Attack2",
                        ImageIO.read(getClass().getResource(path + "/Attack2.png")));
                assets[i].put("Attack3",
                        ImageIO.read(getClass().getResource(path + "/Attack3.png")));
                assets[i].put("Dead",
                        ImageIO.read(getClass().getResource(path + "/Dead.png")));
                assets[i].put("Hurt",
                        ImageIO.read(getClass().getResource(path + "/Hurt.png")));
                assets[i].put("Defend",
                        ImageIO.read(getClass().getResource(path + "/Defend.png")));

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
            if (action / 100 == 0) {// attack1
                currentBufferedImage = assets[playerNum].get("Attack1");
                updateImg(currentBufferedImage, action % 100);
            } else if (action / 100 == 1) {// attack2
                currentBufferedImage = assets[playerNum].get("Attack2");
                updateImg(currentBufferedImage, action % 100);
            } else if (action / 100 == 2) {// attack3
                currentBufferedImage = assets[playerNum].get("Attack3");
                updateImg(currentBufferedImage, action % 100);
            } else if (action / 100 == 3) {// dead
                currentBufferedImage = assets[playerNum].get("Dead");
                updateImg(currentBufferedImage, action % 100);
            } else if (action / 100 == 4) {// defend
                currentBufferedImage = assets[playerNum].get("Defend");
                updateImg(currentBufferedImage, action % 100);
            } else if (action / 100 == 5) {// hurt
                currentBufferedImage = assets[playerNum].get("Hurt");
                updateImg(currentBufferedImage, action % 100);
            } else if (action / 100 == 6) {// idle
                currentBufferedImage = assets[playerNum].get("Idle");
                updateImg(currentBufferedImage, action % 100);
            } else if (action / 100 == 7) {// jump
                currentBufferedImage = assets[playerNum].get("Jump");
                updateImg(currentBufferedImage, action % 100);
            } else if (action / 100 == 8) {// run
                currentBufferedImage = assets[playerNum].get("Run");
                updateImg(currentBufferedImage, action % 100);
            } else if (action / 100 == 9) {// run + attack

            }

        } catch (Exception e) {
            System.out.println("++++++++++++++++++++++++++++++++++++");
            e.printStackTrace();
            System.out.println("++++++++++++++++++++++++++++++++++++");
            System.exit(1);
        }

    }

    private void updateImg(BufferedImage img, int Frame) {
        img = assets[playerNum].get("Idle");
        int currentFrame = Frame / 10;
        currentBufferedFramed = img.getSubimage(currentFrame * Player.PLAYER_IMGWIDTH, 0, Player.PLAYER_IMGWIDTH, Player.PLAYER_IMGHEIGHT);
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
            drawImage(g, x+Player.IMG_OFFSET, y);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
