import HashMap.*;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.io.File;

import javax.imageio.ImageIO;

public class PlayerImages {

    private int[] nums;
    private String name;
    private BufferedImage currentImage;
    public PlayerImages(String name) {
        this.name = name;
        nums = new int[3];
    }

    public void draw(Graphics g, int x, int y) {
        g.drawImage(currentImage, x, y, null);
    }

}
