import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import HashMap.MyHashMap;

//abstract class for all projectiles
public class Projectile extends GameObjectStatus {

    public static final int INVINCIBILITY = 5;
    public static final int PLAYER_WIDTH = 20;// ball width
    public static final int PLAYER_HEIGHT = 20;// ball height
    public static double GRAVITY = 0.15;
    public static double AIRRESISTANCE = 1;

    private BufferedImage sprite;
    private String name;
    private int lifetime;
    private int id;

    public Projectile(String name, int id, ManagerThread managerThread, double GRAVITY, double AIRRESISTANCE) {
        super(managerThread);
        this.id = id;
        this.name = name;
        this.GRAVITY = GRAVITY;
        lifetime = 0;
        try {
            sprite = ImageIO.read(getClass().getResource("/assets/EnergyBall.gif"));
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void run() {
        double pX = super.getXpos();// projectile X
        double pY = super.getYpos();// projectile Y
        Vector v = super.getVector();
        lifetime++;
        v.setYDirection(v.getYDirection() + Projectile.GRAVITY / 3);
        if (pY > 800) {// out of bounds
            super.setYpos(50);
            v.setYDirection(0);
            v.setXDirection(0);
        }

        if (v.getXDirection() != 0) {
            if (v.getXDirection() > ManagerThread.MINXVELOCITY) {
                if (super.isTouchingGround() == false) {
                    v.setXDirection(v.getXDirection());
                } else {
                    v.setXDirection(v.getXDirection() - ManagerThread.FRICTION);
                }
            } else if (v.getXDirection() < -ManagerThread.MINXVELOCITY) {
                if (super.isTouchingGround() == false) {
                    v.setXDirection(v.getXDirection());
                } else {
                    v.setXDirection(v.getXDirection() + ManagerThread.FRICTION);
                }
            } else {
                v.setXDirection(0);
            }
        }
        for (Rectangle r : ManagerThread.walls) {
            // if touching side, xDirection = 0, x pos subtract or add
            int wX = (int) r.getX();// wall X
            int wY = (int) r.getY();// wall Y
            int wW = (int) r.getWidth();// wall Width
            int wH = (int) r.getHeight();// wall Height

            if (pY < wY + wH && pY + PLAYER_HEIGHT > wY + 2 && pX < wX && pX + PLAYER_WIDTH > wX) {
                // TODO: touching left edge
                if (v.getXDirection() >= 0) {
                    v.setXDirection(0);
                    super.setXpos(wX - PLAYER_WIDTH);
                }
            } else if (pY < wY + wH && pY + PLAYER_HEIGHT > wY + 2 && pX < wX + wW && pX + PLAYER_WIDTH > wX + wW) {
                // TODO: touching right edge
                if (v.getXDirection() <= 0) {
                    v.setXDirection(0);
                    super.setXpos(wX + wW);
                }
            } else if (pY + PLAYER_HEIGHT < wY + wH && pY + PLAYER_HEIGHT >= wY - .1 && pX + PLAYER_WIDTH > wX
                    && pX < wX + wW) {
                // touching top edgPLAYER_HEIGHT
                super.getManagerThread().deleteBall(name);
                /*
                 * super.setYpos(wY - PLAYER_HEIGHT);
                 * super.setTouchingGround(true);
                 * if (v.getYDirection() >= 0) {
                 * v.setYDirection(0);
                 * }
                 */
            } else if (pY < wY + wH && pY > wY && pX + PLAYER_WIDTH > wX && pX < wX + wW) {
                // touching top edge
                super.getManagerThread().deleteBall(name);
            }
        }
        if (lifetime == 240) {
            super.getManagerThread().deleteBall(name);
        }
        super.translateXpos(v.getXDirection());
        super.translateYpos(v.getYDirection());
        System.out.println("x: " + v.getXDirection() + " y: " + v.getYDirection());

    }

    public double getXDirection() {
        return super.getVector().getXDirection();
    }

    public int getLifetime() {
        return lifetime;
    }

    public int getId() {
        return id;
    }

}
