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
        // this.nums = nums;
        this.playerNum = playerNum;
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
                        ImageIO.read(getClass().getResource("/assets/src/player/k1/Idle.png")));
                assets[i].put("Run",
                        ImageIO.read(getClass().getResource("/assets/src/player/k1//Run.png")));
                assets[i].put("Jump",
                        ImageIO.read(getClass().getResource("/assets/src/player/k1/Jump.png")));
                assets[i].put("Attack1",
                        ImageIO.read(getClass().getResource("/assets/src/player/k1/Attack1.png")));
                assets[i].put("Attack2",
                        ImageIO.read(getClass().getResource("/assets/src/player/k1/Attack2.png")));
                assets[i].put("Attack3",
                        ImageIO.read(getClass().getResource("/assets/src/player/k1/Attack3.png")));
                assets[i].put("Dead",
                        ImageIO.read(getClass().getResource("/assets/src/player/k1/Dead.png")));
                assets[i].put("Hurt",
                        ImageIO.read(getClass().getResource("/assets/src/player/k1/Hurt.png")));
                assets[i].put("Defend",
                        ImageIO.read(getClass().getResource("/assets/src/player/k1/Defend.png")));

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
        int action = number;
        if (action / 100 == 0) {// attack1
            currentBufferedImage = assets[1].get("Attack1");
            updateImg(currentBufferedImage, action % 100);
        } else if (action / 100 == 1) {// attack2
            currentBufferedImage = assets[1].get("Attack2");
            updateImg(currentBufferedImage, action % 100);
        } else if (action / 100 == 2) {// attack3
            currentBufferedImage = assets[1].get("Attack3");
            updateImg(currentBufferedImage, action % 100);
        } else if (action / 100 == 3) {// dead
            currentBufferedImage = assets[1].get("Dead");
            updateImg(currentBufferedImage, action % 100);
        } else if (action / 100 == 4) {// defend
            currentBufferedImage = assets[1].get("Defend");
            updateImg(currentBufferedImage, action % 100);
        } else if (action / 100 == 5) {// hurt
            currentBufferedImage = assets[1].get("Hurt");
            updateImg(currentBufferedImage, action % 100);
        } else if (action / 100 == 6) {// idle
            currentBufferedImage = assets[1].get("Idle");
            updateImg(currentBufferedImage, action % 100);
        } else if (action / 100 == 7) {// jump
            currentBufferedImage = assets[1].get("Jump");
            updateImg(currentBufferedImage, action % 100);
        } else if (action / 100 == 8) {// run
            currentBufferedImage = assets[1].get("Run");
            updateImg(currentBufferedImage, action % 100);
        } else if (action / 100 == 9) {// run + attack

        }

    }

    private void updateImg(BufferedImage img, int Frame) {
        img = assets[0].get("Idle");
        int currentFrame = Frame / 10;
        currentBufferedFramed = img.getSubimage(currentFrame * Player.PLAYER_WIDTH, 0, Player.PLAYER_WIDTH,
                Player.PLAYER_HEIGHT);
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
            drawImage(g, x, y);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
