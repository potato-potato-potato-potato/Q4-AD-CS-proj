import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import HashMap.MyHashMap;

//abstract class for all projectiles
public class Melee extends GameObjectStatus {

    public static final int PLAYER_WIDTH = 20;// Melee width
    public static final int PLAYER_HEIGHT = 20;// Melee height
    public static final int MELEE_TIME = 10;// Melee height
    private String name;
    private int lifetime;

    public Melee(String name, ManagerThread managerThread) {
        super(managerThread);
        this.name = name;
        lifetime = 0;
    }
    public void run() {
        lifetime++;
        if(lifetime==MELEE_TIME){
            super.getManagerThread().deleteBall(name);
        }
    }
    public int getLifetime() {
        return lifetime;
    }
}
