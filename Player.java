import java.awt.Rectangle;
import HashMap.MyHashMap;

public class Player extends GameObjectStatus {
    public static final int PLAYER_WIDTH = 30;// player width
    public static final int PLAYER_HEIGHT = 64;// player height
    public static final int PLAYER_IMGWIDTH = 64;// player width
    public static final int PLAYER_IMGHEIGHT = 64;// player height
    public static final int IMG_OFFSET = -15;// player height
    public static final int FIREBALL_SPEED = 5;// Fireball speed
    public static final double FIREBALL_MULTIPLIER = 2;
    private String name;
    private int fireCooldown;
    private int[] imgNum;

    public Player(String name, ManagerThread managerThread) {
        super(managerThread);
        this.name = name;
        imgNum = new int[] { 0, 0, 0 };// hundreds value is file, tens is frame, ones value even is left, ones value
                                       // odd is right 000 is first frame of attack one facing right
        fireCooldown = 0;
    }

    public void run() {
        Vector v = super.getVector();
        double pX = super.getXpos();// player X
        double pY = super.getYpos();// player Y

        v.setYDirection(v.getYDirection() + ManagerThread.GRAVITY);
        if (pY > 800) {// out of bounds and DIED
            super.setYpos(50);
            v.setYDirection(0);
            v.setXDirection(0);
            super.getManagerThread().broadcast(new Pair<String, Object>("PlayerDies", name));
        }
        if (super.isUp()) {// up
            imgNum[0] = 7;
            if (super.isTouchingGround() == true) {
                v.setYDirection(v.getYDirection() - ManagerThread.JUMPLAYER_HEIGHT);
                super.setTouchingGround(false);
            } else {
                v.setYDirection(v.getYDirection() - ManagerThread.SMASH);
            }
            if (v.getYDirection() <= 0) {
                imgNum[1] = 1;
            } else {
                imgNum[1] = 2;
            }
        }
        if (super.isDown()) {// down
            if (super.isTouchingGround() == false) {
                v.setYDirection(v.getYDirection() + ManagerThread.SMASH);
            }
        }
        if (super.isLeft()) {// left
            if (super.isTouchingGround()) {
                v.setXDirection(v.getXDirection() - ManagerThread.GROUNDMOVEMENT);
                imgNum[0] = 8;
            }
            v.setXDirection(v.getXDirection() - ManagerThread.AIRMOVEMENT);
            imgNum[2] = 0;
        }
        if (super.isRight()) {// right
            if (super.isTouchingGround()) {
                v.setXDirection(v.getXDirection() + ManagerThread.GROUNDMOVEMENT);
                imgNum[0] = 8;
            }
            v.setXDirection(v.getXDirection() + ManagerThread.AIRMOVEMENT);
            imgNum[2] = 1;
        }
        if (super.isDash()) {// dash

        }
        if (fireCooldown > 0) {
            fireCooldown--;
        }
        if (super.isLeftMouseState()) {// fire
            if (fireCooldown <= 0) {
                double deltaX = super.getMouseX() - (pX + PLAYER_WIDTH / 2);
                double deltaY = super.getMouseY() - (pY + PLAYER_WIDTH / 4);
                double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                if (deltaX > 0) {
                    super.getManagerThread().summonFireBall(pX + PLAYER_WIDTH, pY + PLAYER_HEIGHT / 4,
                            new Vector(deltaX / distance * FIREBALL_SPEED + v.getXDirection(),
                                    deltaY / distance * FIREBALL_SPEED + v.getYDirection()));
                } else {// 20 is ball width
                    super.getManagerThread().summonFireBall(pX - 20, pY + PLAYER_HEIGHT / 4,
                            new Vector(deltaX / distance * FIREBALL_SPEED + v.getXDirection(),
                                    deltaY / distance * FIREBALL_SPEED + v.getYDirection()));
                }
                fireCooldown = 100;
            }
        }
        if (v.getXDirection() != 0) {
            if (v.getXDirection() > ManagerThread.MINXVELOCITY) {
                if (super.isTouchingGround() == false) {
                    v.setXDirection(v.getXDirection() - ManagerThread.AIRRESISTANCE);
                } else if (!(isLeft() || isRight())) {
                    v.setXDirection(v.getXDirection() - ManagerThread.FRICTION);
                    if (super.isDown()) {// slow down more if down arrow is pressed
                        v.setXDirection(v.getXDirection() - 2 * ManagerThread.FRICTION);
                    }
                }
            } else if (v.getXDirection() < -ManagerThread.MINXVELOCITY) {
                if (super.isTouchingGround() == false) {
                    v.setXDirection(v.getXDirection() + ManagerThread.AIRRESISTANCE);
                } else if (!(isLeft() || isRight())) {
                    v.setXDirection(v.getXDirection() + ManagerThread.FRICTION);
                    if (super.isDown()) {// slow down more if down arrow is pressed
                        v.setXDirection(v.getXDirection() + 2 * ManagerThread.FRICTION);
                    }
                }
            } else {
                v.setXDirection(0);
            }

        }
        if (v.getYDirection() > ManagerThread.MAXVELOCITY) {
            v.setYDirection(ManagerThread.MAXVELOCITY);
        }
        if (v.getXDirection() > ManagerThread.MAXVELOCITY) {
            v.setXDirection(ManagerThread.MAXVELOCITY);
        }
        if (v.getXDirection() < -ManagerThread.MAXVELOCITY) {
            v.setXDirection(-ManagerThread.MAXVELOCITY);
        }
        for (Platform r : ManagerThread.walls) {// collision with walls
            // if touching side, xDirection = 0, x pos subtract or add

            int wX = (int) r.getX();// wall X
            int wY = (int) r.getY();// wall Y
            int wW = (int) r.getWidth();// wall Width
            int wH = (int) r.getHeight();// wall Height

            if (pY + PLAYER_HEIGHT < wY + wH && pY + PLAYER_HEIGHT >= wY - .1 && pX + PLAYER_WIDTH > wX
                    && pX < wX + wW) {
                // touching top edgPLAYER_HEIGHT
                super.setYpos(wY - PLAYER_HEIGHT);
                super.setTouchingGround(true);
                if (v.getYDirection() >= 0) {
                    v.setYDirection(0);
                }

            } else if (pY < wY + wH && pY > wY && pX + PLAYER_WIDTH > wX && pX < wX + wW) {
                // touching bottom edge
                if (r.hasCeiling()) {
                    super.setYpos(wY + wH);
                    if (v.getYDirection() < 0) {
                        v.setYDirection(0);
                    }

                }
            } else if (pY < wY + wH && pY + PLAYER_HEIGHT > wY + 2 && pX < wX && pX + PLAYER_WIDTH > wX) {
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

            }
        }

        for (String s : ManagerThread.balls.keySet()) {
            double[] d = ManagerThread.balls.get(s);
            double wX = d[0];
            double wY = d[1];
            double wW = Projectile.PLAYER_WIDTH;
            double wH = Projectile.PLAYER_HEIGHT;
            // if touching fireball, add partial velocity, delete fireball
            if (d[4] > Projectile.INVINCIBILITY) {
                if ((pY < wY + wH && pY + PLAYER_HEIGHT > wY + 2 && pX < wX && pX + PLAYER_WIDTH > wX) || (pY < wY + wH
                        && pY + PLAYER_HEIGHT > wY + 2 && pX < wX + wW && pX + PLAYER_WIDTH > wX + wW)) {
                    // TODO: touching left edge
                    v.setXDirection(v.getXDirection() + (d[2] - v.getXDirection()) * FIREBALL_MULTIPLIER);
                    super.getManagerThread().deleteBall(s);// remove ball
                }
            }
        }
        super.setImgStatus(imgNum[0] * 100 + imgNum[1] * 10 + imgNum[2]);
        super.translateXpos(v.getXDirection());
        super.translateYpos(v.getYDirection());

    }
}
