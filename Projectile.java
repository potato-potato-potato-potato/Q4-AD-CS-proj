import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

//abstract class for all projectiles
public interface Projectile {

    Point Position = new Point();
    Vector velocity = new Vector(0, 0);
    int damage = 0;
    Rectangle hitbox = new Rectangle();
    BufferedImage sprite = null;
    int ID = -1;

    public abstract void update();

    public abstract void setPostion(Point p);

    public abstract void setVelocity(Vector v);

    public abstract void setDamage(int d);

    public abstract void setHitbox(Rectangle r);

    public abstract void setSprite(BufferedImage b);

    public abstract void setID(int i);

    public abstract void draw(Graphics g);

}
