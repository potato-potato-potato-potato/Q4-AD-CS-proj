import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Point;

public abstract class PhysicsObjects {

    public final Rectangle HITBOX;
    public int xVelocity = 0, yVelocity = 0;

    public PhysicsObjects(Rectangle hitBox) {
        HITBOX = hitBox;

    }

    public void update() {
        HITBOX.x += xVelocity;
        HITBOX.y += yVelocity;
    }

    public void setVelocity(int x, int y) {
        xVelocity = x;
        yVelocity = y;
    }

    public void setXVelocity(int x) {
        xVelocity = x;
    }

    public void setYVelocity(int y) {
        yVelocity = y;
    }

    public int getXVelocity() {
        return xVelocity;
    }

    public int getYVelocity() {
        return yVelocity;
    }

    public void translateVelocity(int x, int y) {
        xVelocity += x;
        yVelocity += y;
    }

    abstract void drawMe(Graphics g);
}
