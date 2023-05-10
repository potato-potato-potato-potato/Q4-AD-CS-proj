
import java.lang.Math;
import java.awt.Graphics;
import java.awt.Point;

public class Vector implements Cloneable {
    public double xDir;
    public double yDir;

    public Vector(double xDir, double yDir) {
        this.xDir = xDir;
        this.yDir = yDir;
    }

    public Vector(Point point) {
        this.xDir = point.x;
        this.yDir = point.y;
    }

    public static Vector addVectors(Vector v1, Vector v2) {
        return new Vector(v1.getXDirection() + v2.getXDirection(), v1.getYDirection() + v2.getYDirection());
    }

    public static Vector subtractVectors(Vector v1, Vector v2) {
        return new Vector(v1.getXDirection() - v2.getXDirection(), v1.getYDirection() - v2.getYDirection());
    }

    public static Vector multiplyByScalar(Vector v1, double scalar) {
        return new Vector(v1.getXDirection() * scalar, v1.getYDirection() * scalar);
    }

    public static Vector multiplyBy(Vector v1, double scalar1, double scalar2) {
        return new Vector(v1.getXDirection() * scalar1, v1.getYDirection() * scalar2);
    }

    public double getMagnitude() {
        return Math.sqrt(Math.pow(xDir, 2) + Math.pow(yDir, 2));
    }

    public double getXDirection() {
        return xDir;
    }

    public double getYDirection() {
        return yDir;
    }

    public void setXDirection(double xDir) {
        this.xDir = xDir;
    }

    public void setYDirection(double yDir) {
        this.yDir = yDir;
    }

    public Point toPoint() {
        return new Point((int) getXDirection(), (int) getYDirection());
    }

    public void draw(Graphics g, Point point) {
        g.drawLine((int) point.getX(), (int) point.getY(), (int) point.getX(), (int) point.getY() + (int) yDir);
        g.drawLine((int) point.getX(), (int) point.getY(), (int) point.getX() + (int) xDir, (int) point.getY());
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;

    }

}