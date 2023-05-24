import java.awt.Rectangle;
import HashMap.MyHashMap;
public class Player extends GameObjectStatus {
    public static final int PLAYER_WIDTH = 128;// player width
    public static final int PLAYER_HEIGHT = 128;// player height
    public static final int FIREBALL_SPEED = 10;// player width
    private String name;
    private MyHashMap<String, int[]> projectiles;//projectiles stored in hashmap (used for collisions), int[] array contains [x, y, xVel, yVel]
    private int imgNum, fireCooldown;

    public Player(String name, ManagerThread managerThread) {
        super(managerThread);
        this.name = name;
        imgNum = 0;//hundreds value is file, tens is frame, ones value even is left, ones value odd is right
        //000 is first frame of attack one facing right
        fireCooldown = 0;
    }

    public void run() {
        Vector v = super.getVector();
        double pX = super.getXpos();// player X
        double pY = super.getYpos();// player Y
        projectiles = new MyHashMap<String, int[]>();

        v.setYDirection(v.getYDirection() + ManagerThread.GRAVITY);
        if (pY > 800) {// out of bounds
            super.setYpos(50);
            v.setYDirection(0);
            v.setXDirection(0);
        }
        if (super.isUp()) {// up
            if (super.isTouchingGround() == true) {
                v.setYDirection(v.getYDirection() - ManagerThread.JUMPLAYER_HEIGHT);
                super.setTouchingGround(false);
            } else {
                v.setYDirection(v.getYDirection() - ManagerThread.SMASH);
            }
            if(v.getYDirection()<=0){
                imgNum = 710;
            }else{
                imgNum = 720;
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
                imgNum = 800;
            }
            v.setXDirection(v.getXDirection() - ManagerThread.AIRMOVEMENT);
        }
        if (super.isRight()) {// right
            if (super.isTouchingGround()) {
                v.setXDirection(v.getXDirection() + ManagerThread.GROUNDMOVEMENT);
                imgNum = 800;
            }
            v.setXDirection(v.getXDirection() + ManagerThread.AIRMOVEMENT);
            imgNum +=1;
        }
        if (super.isDash()) {// dash

        }
        if(fireCooldown>0){
            fireCooldown--;
        }
        if (super.isLeftMouseState()) {// fire
            System.out.println(fireCooldown);
            if(fireCooldown<=0){
                super.getManagerThread().summonFireBall(pX, pY, new Vector(v.getXDirection()+FIREBALL_SPEED, v.getYDirection()));
                fireCooldown = 100;
            }
        }
        if (v.getXDirection() != 0) {
            if (v.getXDirection() > ManagerThread.MINXVELOCITY) {
                if (super.isTouchingGround() == false) {
                    v.setXDirection(v.getXDirection() - ManagerThread.AIRRESISTANCE);
                } else {
                    v.setXDirection(v.getXDirection() - ManagerThread.FRICTION);
                    if (super.isDown()) {// slow down more if down arrow is pressed
                        v.setXDirection(v.getXDirection() - 2 * ManagerThread.FRICTION);
                    }
                }
            } else if (v.getXDirection() < -ManagerThread.MINXVELOCITY) {
                if (super.isTouchingGround() == false) {
                    v.setXDirection(v.getXDirection() + ManagerThread.AIRRESISTANCE);
                } else {
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
        for (Platform r : ManagerThread.walls) {//collision with walls
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
        super.setImgStatus(imgNum);
        super.translateXpos(v.getXDirection());
        super.translateYpos(v.getYDirection());

    }

    public void setProjectiles(MyHashMap<String, int[]> projectiles) {
        this.projectiles = projectiles;
    }
}
