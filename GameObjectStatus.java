import java.io.Serializable;

public class GameObjectStatus implements Serializable {
    // isPlayer|Identifier|ID|Vector|Xpos|Ypos|up|down|left|right|dash|left mouse
    // State|right mouse state|mouseX|mouseY|touchingGround|

    private boolean isPlayer;
    private String identifier;
    private int ID;
    private Vector vector;
    private double Xpos;
    private double Ypos;
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;
    private boolean dash;
    private boolean leftMouseState;
    private boolean rightMouseState;
    private double mouseX;
    private double mouseY;
    private boolean touchingGround;
    private int jumpCount;

    public GameObjectStatus() {
        vector = new Vector(0, 0);
    }

    // update in this order
    // [up, down, left, right, dash, leftmouseState,rightmouseState,mouseX,mouseY]
    public void updateKeyStatus(int[] keys) {
        up = keys[0] == 1;
        down = keys[1] == 1;
        left = keys[2] == 1;
        right = keys[3] == 1;
        dash = keys[4] == 1;
        leftMouseState = keys[5] == 1;
        rightMouseState = keys[6] == 1;
        mouseX = keys[7];
        mouseY = keys[8];
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    public void setPlayer(boolean player) {
        isPlayer = player;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String name) {
        this.identifier = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int id) {
        this.ID = id;
    }

    public Vector getVector() {
        return vector;
    }

    public void setVector(Vector v) {
        this.vector = v;
    }

    public double getXpos() {
        return Xpos;
    }

    public void setXpos(double xpos) {
        this.Xpos = xpos;
    }

    public void translateXpos(double x) {
        this.Xpos += x;
    }

    public double getYpos() {
        return Ypos;
    }

    public void setYpos(double ypos) {
        this.Ypos = ypos;
    }

    public void translateYpos(double y) {
        this.Ypos += y;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDash() {
        return dash;
    }

    public void setDash(boolean dash) {
        this.dash = dash;
    }

    public boolean isLeftMouseState() {
        return leftMouseState;
    }

    public void setLeftMouseState(boolean leftMouseState) {
        this.leftMouseState = leftMouseState;
    }

    public boolean isRightMouseState() {
        return rightMouseState;
    }

    public void setRightMouseState(boolean rightMouseState) {
        this.rightMouseState = rightMouseState;
    }

    public double getMouseX() {
        return mouseX;
    }

    public void setMouseX(double mouseX) {
        this.mouseX = mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public void setMouseY(double mouseY) {
        this.mouseY = mouseY;
    }

    public boolean isTouchingGround() {
        return touchingGround;
    }

    public void setTouchingGround(boolean touchingGround) {
        this.touchingGround = touchingGround;
    }

    public int getJumpCount() {
        return jumpCount;
    }

    public void setJumpCount(int jumpCount) {
        this.jumpCount = jumpCount;
    }

}
