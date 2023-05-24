import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class EnergyBall {

    public Point Position;
    public Vector velocity;
    public int damage = -1;// placeholder for real damage
    public int width = 10;
    public int height = 10;
    public Rectangle hitbox;
    public BufferedImage sprite;
    public int ID;

    public EnergyBall(Point Position, int mouseX, int mouseY) {
        this.Position = Position;
        velocity = Vector.subtractVectors(new Vector(Position), new Vector(mouseX, mouseY));
        this.hitbox = new Rectangle(Position.x, Position.y, width, height);
    }

    public void update() {
        this.Position.x += velocity.getXDirection();
        this.Position.y += velocity.getYDirection();
        this.hitbox.x = Position.x;
        this.hitbox.y = Position.y;
    }

    public void setPostion(Point p) {
        this.Position = p;
    }

    public void setVelocity(Vector v) {
        this.velocity = v;
    }

    public void setDamage(int d) {
        this.damage = d;
    }

    public void setHitbox(Rectangle r) {
        this.hitbox = r;
    }

    public void setSprite(BufferedImage b) {
        this.sprite = b;
    }

    public void setID(int i) {
        this.ID = i;
    }

    public void draw(Graphics g) {
        if (sprite != null) {
            g.drawImage(sprite, Position.x, Position.y, null);
        } else {
            System.out.println("sprite is null");
            g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        }

    }

}