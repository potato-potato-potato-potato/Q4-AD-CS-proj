import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.io.IOException;
import java.net.URL;
import java.io.File;

import javax.imageio.ImageIO;

public class Map implements Serializable {
    public Rectangle[] walls = new Rectangle[4];
    public Platform[] islands = new Platform[4];
    public BufferedImage background;

    public Map() {
        walls[0] = new Rectangle(0, 0, 1200, 32);
        walls[1] = new Rectangle(0, 0, 32, 678);
        walls[2] = new Rectangle(0, 678, 1232, 32);
        walls[3] = new Rectangle(1200, 0, 32, 678);

        islands[0] = new Platform(100, 550, 1000, 20, true);
        islands[1] = new Platform(212, 380, 270, 30, false);
        islands[2] = new Platform(780, 380, 270, 30, false);
        islands[3] = new Platform(505, 225, 250, 30, false);
        // islands[1] = new Platform(300, 600, 100, 20, true);
        // islands[2] = new Platform(500, 600, 100, 20, true);
        // islands[3] = new Platform(350, 300, 20, 100, true);

        try {
            background = ImageIO.read(getClass().getResource("/assets/map.jpg"));
        } catch (IOException e) {
            System.out.println("Error loading background");
        }

    }

    public void drawMe(Graphics g) {
        g.drawImage(background, 0, 0, null);

        for (Rectangle r : islands) {
            // images

            // hitBoxes
            g.setColor(Color.BLACK);
            g.drawRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(), (int) r.getHeight());

        }

    }

    public void drawBackground(Graphics g) {
        for (Rectangle r : walls) {
            // hitBoxes
            g.setColor(Color.BLACK);
            g.fillRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(), (int) r.getHeight());
            // images
            g.drawString("Instructions:", 100, 100);
            g.drawString("Arrow keys or WASD to move", 100, 140);
            g.drawString("Left click to melee attack, right click to shoot fireballs", 100, 180);
            g.drawString("Press shift while moving to boost in that direction", 100, 220);
            

        }
    }

    public Platform[] getIslands() {// return all hitboxes
        return islands;
    }

}