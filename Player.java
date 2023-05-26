import java.awt.Rectangle;
import HashMap.MyHashMap;

public class Player extends GameObjectStatus {
    public static final int PLAYER_WIDTH = 30;// player width
    public static final int PLAYER_HEIGHT = 64;// player height
    public static final int PLAYER_IMGWIDTH = 64;// player width
    public static final int PLAYER_IMGHEIGHT = 64;// player height
    public static final int IMG_OFFSET = -15;// player height
    public static final int FIREBALL_SPEED = 7;// Fireball speed
    public static final int MELEE_COOLDOWN = 10;
    public static final double FIREBALL_MULTIPLIER = 2;

    public static final int ANIMATION_SPEED = 10;// play the next animation step every _ frames

    // 0=Attack1,1=Attack2,2=Attack3,3=Dead,4=Defend,5=hurt,6=idle,7=jump,8=run
    private int currentAnimation;
    private String name;

    public int frame = 0;

    private int fireCooldown, meleeCooldown;

    private int[] imgNum;
    private int timer;

    public Player(String name, ManagerThread managerThread) {
        super(managerThread);
        this.name = name;
        imgNum = new int[] { 0, 0, 0 };// index 0 is image, 1 is frame, 2 is direction
        fireCooldown = 0;
        meleeCooldown = 0;
        timer = 0;
    }

    public void run() {
        frame++;
        Vector v = super.getVector();
        double pX = super.getXpos();// player X
        double pY = super.getYpos();// player Y
        timer++;

        v.setYDirection(v.getYDirection() + ManagerThread.GRAVITY);
        if (pY > 800) {// out of bounds and DIED
            super.setYpos(50);
            v.setYDirection(0);
            v.setXDirection(0);
            super.getManagerThread().broadcast(new Pair<String, Object>("PlayerDies", name));
        }
        if (isIdle()) {
            if (super.isTouchingGround() == true) {
                if (imgNum[0] != 6) {
                    frame = 0;
                }
                if (frame / Player.ANIMATION_SPEED == 3) {
                    frame = 0;
                }
                imgNum[0] = 6;
                imgNum[1] = frame / Player.ANIMATION_SPEED;
            }

        }
        if (super.isUp()) {// up
            imgNum[0] = 7;
            imgNum[1] = 0;
            if (super.isTouchingGround() == true) {
                v.setYDirection(v.getYDirection() - ManagerThread.JUMPLAYER_HEIGHT);
                super.setTouchingGround(false);
            } else {
                v.setYDirection(v.getYDirection() - ManagerThread.SMASH);
            }
            if (v.getYDirection() <= 0) {
                imgNum[2] = 1;
            } else {
                imgNum[2] = 2;
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
                if (imgNum[0] != 8) {
                    frame = 0;
                }
                if (frame / Player.ANIMATION_SPEED == 6) {
                    frame = 0;
                }
                imgNum[0] = 8;
                imgNum[1] = frame / Player.ANIMATION_SPEED;

            }
            v.setXDirection(v.getXDirection() - ManagerThread.AIRMOVEMENT);
            imgNum[2] = 0;
        }
        if (super.isRight()) {// right
            if (super.isTouchingGround()) {
                v.setXDirection(v.getXDirection() + ManagerThread.GROUNDMOVEMENT);
                if (imgNum[0] != 8) {
                    frame = 0;
                }
                if (frame / Player.ANIMATION_SPEED == 6) {
                    frame = 0;
                }
                imgNum[0] = 8;
                imgNum[1] = frame / Player.ANIMATION_SPEED;

            }
            v.setXDirection(v.getXDirection() + ManagerThread.AIRMOVEMENT);
            imgNum[2] = 1;
        }
        if (super.isDash()) {// dash

        }
        if (fireCooldown > 0) {
            fireCooldown--;
        }
        if (meleeCooldown > 0) {
            meleeCooldown--;
        }
        if (super.isRightMouseState()) {// fire
            if (fireCooldown <= 0) {
                double deltaX = super.getMouseX() - (pX + PLAYER_WIDTH / 2);
                double deltaY = super.getMouseY() - (pY + PLAYER_WIDTH / 4);
                double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                if (deltaX > 0) {
                    super.getManagerThread().summonFireBall(pX + PLAYER_WIDTH, pY + PLAYER_HEIGHT / 4,
                            new Vector(deltaX / distance * FIREBALL_SPEED, deltaY / distance * FIREBALL_SPEED));
                } else {// 20 is ball width
                    super.getManagerThread().summonFireBall(pX - 20, pY + PLAYER_HEIGHT / 4,
                            new Vector(deltaX / distance * FIREBALL_SPEED,
                                    deltaY / distance * FIREBALL_SPEED));
                }
                fireCooldown = 100;
            }
        }
        if (super.isLeftMouseState()) {// Melee (reused code from fireball thats why this is bad)
            if (meleeCooldown <= 0) {
                double deltaX = super.getMouseX() - (pX + PLAYER_WIDTH / 2);
                if (deltaX > 0) {
                    super.getManagerThread().summonMelee(pX + PLAYER_WIDTH, pY + PLAYER_HEIGHT / 4, new Vector(1, 0), this);// melee
                                                                                                                      // facing
                                                                                                                      // riht
                } else {// 20 is ball width
                    super.getManagerThread().summonMelee(pX - 20, pY + PLAYER_HEIGHT / 4,new Vector(-1, 0), this);
                }
                meleeCooldown = MELEE_COOLDOWN;
            }
        }
        if (v.getXDirection() != 0) {
            if (v.getXDirection() > ManagerThread.MINXVELOCITY) {
                if (super.isTouchingGround() == false) {
                    v.setXDirection(v.getXDirection() - ManagerThread.AIRRESISTANCE);
                } else if (!(isLeft() || isRight())) {
                    if (v.getXDirection() < ManagerThread.FRICTION) {// if movement is small enough, stop movement
                        v.setXDirection(0);
                    }
                    v.setXDirection(v.getXDirection() - ManagerThread.FRICTION);
                    if (super.isDown()) {// slow down more if down arrow is pressed
                        v.setXDirection(v.getXDirection() - 2 * ManagerThread.FRICTION);
                    }
                }
            } else if (v.getXDirection() < -ManagerThread.MINXVELOCITY) {
                if (super.isTouchingGround() == false) {
                    v.setXDirection(v.getXDirection() + ManagerThread.AIRRESISTANCE);
                } else if (!(isLeft() || isRight())) {
                    if (v.getXDirection() > -ManagerThread.FRICTION) {// if movement is small enough, stop movement
                        v.setXDirection(0);
                    }
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

            if (pY + PLAYER_HEIGHT < wY + ManagerThread.MAXVELOCITY && pY + PLAYER_HEIGHT >= wY - .1
                    && pX + PLAYER_WIDTH > wX && pX < wX + wW) {
                // touching top edgPLAYER_HEIGHT
                if (v.getYDirection() >= -.1) {
                    v.setYDirection(0);
                    super.setYpos(wY - PLAYER_HEIGHT);
                    super.setTouchingGround(true);
                }
            } else if (pY < wY + ManagerThread.MAXVELOCITY && pY > wY && pX + PLAYER_WIDTH > wX && pX < wX + wW) {
                // touching bottom edge
                if (r.hasCeiling()) {
                    super.setYpos(wY + wH);
                    if (v.getYDirection() < 0) {
                        v.setYDirection(0);
                    }

                }
            } else if (pY < wY + ManagerThread.MAXVELOCITY && pY + PLAYER_HEIGHT > wY + 2 && pX < wX
                    && pX + PLAYER_WIDTH > wX) {
                // TODO: touching left edge
                if (v.getXDirection() >= 0) {
                    v.setXDirection(0);
                    super.setXpos(wX - PLAYER_WIDTH);
                }
            } else if (pY < wY + ManagerThread.MAXVELOCITY && pY + PLAYER_HEIGHT > wY + 2 && pX < wX + wW
                    && pX + PLAYER_WIDTH > wX + wW) {
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
        for (String s : ManagerThread.melee.keySet()) {
            double[] d = ManagerThread.melee.get(s);
            double wX = d[0];
            double wY = d[1];
            double wW = Projectile.PLAYER_WIDTH;
            double wH = Projectile.PLAYER_HEIGHT;
            // if touching melee, add partial velocity, delete melee
            if ((pY < wY + wH && pY + PLAYER_HEIGHT > wY + 2 && pX < wX && pX + PLAYER_WIDTH > wX) || (pY < wY + wH
                    && pY + PLAYER_HEIGHT > wY + 2 && pX < wX + wW && pX + PLAYER_WIDTH > wX + wW)) {
                v.setXDirection(v.getXDirection() + d[2]*Melee.KNOCKBACK);
                System.out.println("Knocking back " + d[2]);
                super.getManagerThread().deleteMelee(s);// remove ball
            }
        }
        super.setImgStatus(imgNum[0] * 100 + imgNum[1] * 10 + imgNum[2]);
        super.translateXpos(v.getXDirection());
        super.translateYpos(v.getYDirection());

    }
}
