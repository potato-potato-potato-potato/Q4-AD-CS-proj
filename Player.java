import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Point;

public class Player extends PhysicsObjects {

    public String name;
    public int xVelocity, yVelocity;
    public boolean isTouchingGround;
    // means player get 2 jumps
    public int jumpCount = 2;

    // percent multiplier for knockback
    public double knockBackStrength = 1.0;

    public Player() {
        super(new Rectangle(0, 0, 32, 32));
    }

    public void drawMe(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(HITBOX.x, HITBOX.y, HITBOX.width, HITBOX.height);
    }

}
