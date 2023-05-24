import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

//abstract class for all projectiles
public class Projectile extends GameObjectStatus{

    private int pWidth = 10;// player width
    private int pHeight = 50;// player height
    private String name;
    private Vector v;

    public Projectile(String name) {
        super();
        this.name = name;
        v = super.getVector();
    }

    public void run() {
        double pX = super.getXpos();//projectile X
        double pY = super.getYpos();//projectile Y

        v.setYDirection(v.getYDirection() + ManagerThread.GRAVITY/3);
        if (pY > 800) {// out of bounds
            super.setYpos(50);
            v.setYDirection(0);
            v.setXDirection(0);
        }
        
        if (v.getXDirection() != 0) {
            if (v.getXDirection() > ManagerThread.MINXVELOCITY) {
                if (super.isTouchingGround() == false) {
                    v.setXDirection(v.getXDirection() - ManagerThread.AIRRESISTANCE);
                } else {
                    v.setXDirection(v.getXDirection() - ManagerThread.FRICTION);
                }
            } else if (v.getXDirection() < -ManagerThread.MINXVELOCITY) {
                if (super.isTouchingGround() == false) {
                    v.setXDirection(v.getXDirection() + ManagerThread.AIRRESISTANCE);
                } else {
                    v.setXDirection(v.getXDirection() + ManagerThread.FRICTION);
                }
            } else {
                v.setXDirection(0);
            }
        }
        for (Rectangle r : ManagerThread.walls) {
            // if touching side, xDirection = 0, x pos subtract or add
            int wX = (int) r.getX();//wall X
            int wY = (int) r.getY();//wall Y
            int wW = (int) r.getWidth();//wall Width
            int wH = (int) r.getHeight();//wall Height

            if (pY < wY + wH && pY + pHeight > wY + 2 && pX < wX && pX + pWidth > wX) {
                // TODO: touching left edge
                if (v.getXDirection() >= 0) {
                    v.setXDirection(0);
                    super.setXpos(wX - pWidth);
                }
            } else if (pY < wY + wH && pY + pHeight > wY + 2 && pX < wX + wW && pX + pWidth > wX + wW) {
                // TODO: touching right edge
                if (v.getXDirection() <= 0) {
                    v.setXDirection(0);
                    super.setXpos(wX + wW);
                }
            } else if (pY + pHeight < wY + wH && pY + pHeight >= wY - .1 && pX + pWidth > wX && pX < wX + wW) {
                // touching top edge
                super.setYpos(wY - pHeight);
                super.setTouchingGround(true);
                if (v.getYDirection() >= 0) {
                    v.setYDirection(0);
                }
            } else if (pY < wY + wH && pY > wY && pX + pWidth > wX && pX < wX + wW) {
                // touching top edge
                super.setYpos(wY + wH);
                if (v.getYDirection() <= 0) {
                    v.setYDirection(0);
                }
            }
        }
        super.translateXpos(v.getXDirection());
        super.translateYpos(v.getYDirection());

    }

    public double getXDirection() {
        return v.getXDirection();
    }

}
