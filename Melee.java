import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import HashMap.MyHashMap;

//abstract class for all projectiles
public class Melee extends GameObjectStatus {

    public static final int PLAYER_WIDTH = 50;// Melee width
    public static final int PLAYER_HEIGHT = 50;// Melee height
    public static final int MELEE_TIME = 10;// Duration of melee swing
    public static final int KNOCKBACK = 15;
    private String name;
    private int lifetime;
    private Player p;

    public Melee(String name, ManagerThread managerThread, Player p) {
        super(managerThread);
        this.name = name;
        lifetime = 0;
        this.p = p;
    }
    public boolean isPlayer(Player p){
        if(this.p == p){
            return true;
        }
        return false;
    }
    public void run() {
        lifetime++;
        if(lifetime==MELEE_TIME){
            super.getManagerThread().deleteMelee(name);
        }
    }
    public int getLifetime() {
        return lifetime;
    }
}
