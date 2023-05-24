import java.awt.Rectangle;

public class Platform extends Rectangle {
    private boolean hasCeiling;

    public Platform(int x, int y, int w, int h, boolean hasCeiling) {
        super(x, y, w, h);
        this.hasCeiling = hasCeiling;
    }

    public boolean hasCeiling() {
        return hasCeiling;
    }
}