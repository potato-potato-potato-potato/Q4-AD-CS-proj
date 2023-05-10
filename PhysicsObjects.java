import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class PhysicsObjects<V> {

    private V object;
    private Vector velocity = new Vector(0, 0);
    private Point position = new Point(0, 0);

    public PhysicsObjects(V object) {
        this.object = object;
    }

    public void update() {
        position.translate((int) velocity.getXDirection(), (int) velocity.getYDirection());
    }

    public V getObject() {
        return object;
    }

    public void setObject(V object) {
        this.object = object;
    }

    public Vector getVector() {
        return velocity;
    }

    public void setVector(Vector velocity) {
        this.velocity = velocity;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void draw(Graphics g) {
        if (object instanceof BufferedImage) {
            g.drawImage((BufferedImage) object, position.x, position.y, null);
        }

    }

}
