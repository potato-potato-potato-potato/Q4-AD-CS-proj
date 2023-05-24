import HashMap.*;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
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

    private BufferedImage currentBufferedImage;

    // 1 = idle
    // 2 = running
    // 3 = jumping
    // 4 = temp
    // 5 = temp
    private int currentImg;
    private int currentFrame;

    public PlayerImages(String name, int[] nums) {
        this.name = name;
        this.nums = nums;
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
        imageLoader();
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
                assets[i].put("Idle", ImageIO.read(new File("assets/src/player/Knight_" + i + "/Idle.png")));
                assets[i].put("Run", ImageIO.read(new File("assets/src/player/Knight_" + i + "/Run.png")));
                assets[i].put("Jump", ImageIO.read(new File("assets/src/player/Knight_" + i + "/Jump.png")));
            } catch (Exception e) {
                // TODO: handle exception
            }

        }

    }

    public void update(int[] nums) {
        this.nums = nums;
    }

    private void updateImg() throws IOException {
        int img = nums[3];
        if (img / 100 == 0) {// idle
            currentBufferedImage = assets[playerNum - 1].get("Idle");
        } else if (img / 100 == 1) {// running

        } else if (img / 100 == 2) {// jumping

        } else if (img / 100 == 3) {// temp

        } else if (img / 100 == 4) {// temp

        } else if (img / 100 == 5) {// temp\

        }
    }

    public void drawMe(Graphics g) {
        DrawManipulation.drawCropAndRotate(null, null, g, null, null);

    }
}
