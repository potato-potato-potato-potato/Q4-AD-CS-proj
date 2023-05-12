import java.awt.*;
import java.io.Serializable;

public class Map implements Serializable {
    public Rectangle[] walls = new Rectangle[4];

    public Map() {
        walls[0] = new Rectangle(0, 0, 1200, 32);
        walls[1] = new Rectangle(0, 0, 32, 678);
        walls[2] = new Rectangle(0, 678, 1232, 32);
        walls[3] = new Rectangle(1200, 0, 32, 678);
    }

    public void drawMe(Graphics g) {
        
        for(Rectangle r : walls){
            //hitBoxes
            g.setColor(Color.BLACK);
            g.fillRect((int)r.getX(), (int)r.getY(), (int)r.getWidth(), (int)r.getHeight());
            //images
            
        }


    }

    public Rectangle[] getWalls(){
        return walls;
    }
}