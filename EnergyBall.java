import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class EnergyBall extends pro{

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

    @Override
    public void update() {
        this.Position.x += velocity.getXDirection();
        this.Position.y += velocity.getYDirection();
        this.hitbox.x = Position.x;
        this.hitbox.y = Position.y;
    }

    @Override
    public void setPostion(Point p) {
        this.Position = p;
    }

    @Override
    public void setVelocity(Vector v) {
        this.velocity = v;
    }

    @Override
    public void setDamage(int d) {
        this.damage = d;
    }

    @Override
    public void setHitbox(Rectangle r) {
        this.hitbox = r;
    }

    @Override
    public void setSprite(BufferedImage b) {
        this.sprite = b;
    }

    @Override
    public void setID(int i) {
        this.ID = i;
    }

    @Override
    public void draw(Graphics g) {
        if (sprite != null) {
            g.drawImage(sprite, Position.x, Position.y, null);
        } else {
            System.out.println("sprite is null");
            g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        }

    }

}