import java.awt.*;
import java.io.Serializable;

public class Map implements Serializable {
    public Rectangle[] walls = new Rectangle[4];
    public Platform[] islands = new Platform[1];

    public Map() {
        walls[0] = new Rectangle(0, 0, 1200, 32);
        walls[1] = new Rectangle(0, 0, 32, 678);
        walls[2] = new Rectangle(0, 678, 1232, 32);
        walls[3] = new Rectangle(1200, 0, 32, 678);

        islands[0] = new Platform(100, 600, 1600, 20, true);
        // islands[1] = new Platform(300, 600, 100, 20, true);
        // islands[2] = new Platform(500, 600, 100, 20, true);
        // islands[3] = new Platform(350, 300, 20, 100, true);

    }

    public void drawMe(Graphics g) {

        for (Rectangle r : islands) {
            // hitBoxes
            g.setColor(Color.BLACK);
            g.fillRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(), (int) r.getHeight());
            // images

        }

    }

    public void drawBackground(Graphics g) {
        for (Rectangle r : walls) {
            // hitBoxes
            g.setColor(Color.BLACK);
            g.fillRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(), (int) r.getHeight());
            // images

        }
    }

    public Platform[] getIslands() {// return all hitboxes
        return islands;
    }

}