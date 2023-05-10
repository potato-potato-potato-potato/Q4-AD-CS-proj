import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Point;

public class Player {

    public Vector velocity = new Vector(0, 0);

    public String name;
    public boolean isTouchingGround;
    public boolean isTouchingWall;

    public Rectangle HITBOX = new Rectangle(0, 0, 32, 32);
    // means player get 2 jumps
    public int jumpCount = 2;

    // percent multiplier for knockback
    public double knockBackStrength = 1.0;

    public Player(String name) {
        this.name = name;
    }

    public void update() {
        HITBOX.translate((int) velocity.getXDirection(), (int) velocity.getYDirection());
    }

    public void drawMe(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(HITBOX.x, HITBOX.y, HITBOX.width, HITBOX.height);
    }

}
