import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.Point;

public class DrawUtils {

    public static void rotateDrawing(Drawable toDraw, double degrees, Graphics g, Vector OriginOfRotation) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();
        g2d.translate(OriginOfRotation.xDir, OriginOfRotation.yDir);
        g2d.rotate(Math.toRadians(degrees));

        toDraw.draw(g);

        g2d.setTransform(old);
    }

}

interface Drawable {
    public void draw(Graphics g);
}
