import java.awt.*;
import java.io.Serializable;

public class Map implements Serializable {
    public Rectangle[] walls = new Rectangle[4];

    public Map() {
        walls[0] = new Rectangle(0, 0, 800, 32);
        walls[1] = new Rectangle(0, 0, 32, 600);
        walls[2] = new Rectangle(0, 568, 800, 32);
        walls[3] = new Rectangle(768, 0, 32, 600);
    }

    public void drawMe(Graphics g) {
        
    }
}