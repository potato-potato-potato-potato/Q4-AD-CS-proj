import java.awt.Rectangle;

public class Player extends GameObjectStatus {
    private int pWidth = 10;// player width
    private int pHeight = 50;// player height
    private String name;

    public Player(String name) {
        super();
        this.name = name;
    }
    
    public void run() {
        Vector v = super.getVector();
        double pX = super.getXpos();//player X
        double pY = super.getYpos();//player Y

        v.setYDirection(v.getYDirection() + ManagerThread.GRAVITY);
        if (pY > 800) {// out of bounds
            super.setYpos(50);
            v.setYDirection(0);
            v.setXDirection(0);
        }
        if (super.isUp()) {// up
            if (super.isTouchingGround() == true) {
                v.setYDirection(v.getYDirection() - ManagerThread.JUMPHEIGHT);
                super.setTouchingGround(false);
            } else {
                v.setYDirection(v.getYDirection() - ManagerThread.SMASH);
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

            }
            v.setXDirection(v.getXDirection() - ManagerThread.AIRMOVEMENT);
        }
        if (super.isRight()) {// right
            if (super.isTouchingGround()) {
                v.setXDirection(v.getXDirection() + ManagerThread.GROUNDMOVEMENT);

            }
            v.setXDirection(v.getXDirection() + ManagerThread.AIRMOVEMENT);
        }
        if (super.isDash()) {// dash

        }
        if (super.isLeftMouseState()) {// fire
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
}
