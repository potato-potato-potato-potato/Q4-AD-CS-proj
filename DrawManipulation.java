import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Area;

public final class DrawManipulation {
    private DrawManipulation() {
        // Private constructor to prevent instantiation of the utility class.
    }

    /**
     * Draws a Drawable object, applies a rotation, and translates it to a specified
     * position.
     *
     * @param toDraw      The object to be drawn (implementing the Drawable
     *                    interface).
     * @param degrees     The rotation angle in degrees.
     * @param g           The Graphics object used for drawing.
     * @param translateTo The point to which the object should be translated.
     */
    public static void drawAndRotate(Drawable toDraw, double degrees, Graphics g, Point translateTo) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();
        g2d.translate(translateTo.x, translateTo.y);
        g2d.rotate(Math.toRadians(degrees));

        toDraw.draw(g);

        g2d.setTransform(old);
    }

    /**
     * Draws a Drawable object after applying a rotation, translation, and cropping
     * using a specified shape.
     *
     * @param toDraw      The object to be drawn (implementing the Drawable
     *                    interface).
     * @param degrees     The rotation angle in degrees.
     * @param g           The Graphics object used for drawing.
     * @param translateTo The point to which the object should be translated.
     * @param crop        The shape used for cropping the drawing area.
     */
    public static void drawCropAndRotate(Drawable toDraw, double degrees, Graphics g, Point translateTo, Shape crop) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();
        g2d.translate(translateTo.x, translateTo.y);
        g2d.rotate(Math.toRadians(degrees));

        g2d.clip(crop);
        toDraw.draw(g);

        g2d.setTransform(old);
    }

    /**
     * Cuts out a shape from another shape, modifying the original shape in place.
     *
     * @param shapeToCutOutFrom The shape from which the other shape will be cut
     *                          out.
     * @param shapeToCutOut     The shape to be cut out from the other shape.
     */
    public static void cutOutShapeFromShape(Shape shapeToCutOutFrom, Shape shapeToCutOut) {
        cutOutShapeFromShape(new Area(shapeToCutOutFrom), new Area(shapeToCutOut));
    }

    /**
     * Cuts out an area represented by a shape from another area, modifying the
     * original area in place.
     *
     * @param shapeToCutOutFrom The area from which the other area will be cut out
     *                          (represented by a Shape).
     * @param shapeToCutOut     The area to be cut out from the other area
     *                          (represented by a Shape).
     */
    public static void cutOutShapeFromShape(Area shapeToCutOutFrom, Area shapeToCutOut) {
        shapeToCutOutFrom.subtract(shapeToCutOut);
    }
}