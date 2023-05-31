import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class EnergyBallDraw {

    private BufferedImage sprite;
    private BufferedImage energyBallExplosion;
    private boolean exploded = false;
    private boolean ended = false;
    private String name;
    private Thread timer;

    public EnergyBallDraw(String name) {
        this.name = name;
        try {
            sprite = ImageIO.read(getClass().getResource("/assets/EnergyBall.gif"));
            energyBallExplosion = ImageIO.read(getClass().getResource("/assets/EnergyBallExplosion.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        timer = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            exploded = false;
            ended = true;

        });
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

    public void setExploded(boolean exploded) {
        System.out.println("exploded");
        this.exploded = exploded;
        timer.start();

    }
}
