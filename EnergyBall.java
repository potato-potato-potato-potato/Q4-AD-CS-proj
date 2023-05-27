import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class EnergyBall {

    private BufferedImage sprite;
    private int id;

    public EnergyBall(int id) {
        this.id = id;
        try {
            sprite = ImageIO.read(getClass().getResource("/assets/EnergyBall.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g, int x, int y, Vector v) {
        double deg = Math.toDegrees(Math.atan2(v.getYDirection(), v.getXDirection()));
        DrawUtils.rotateDrawing((g2) -> {
            g2.drawImage(sprite, -Projectile.PLAYER_WIDTH / 2, -Projectile.PLAYER_HEIGHT / 2, null);
        }, deg, g, new Vector(x + (Projectile.PLAYER_WIDTH / 2), y +
                (Projectile.PLAYER_HEIGHT / 2)));
        System.out.println("deg: " + deg);
        System.out.println(v.getXDirection() + " " + v.getYDirection());
    }
}
