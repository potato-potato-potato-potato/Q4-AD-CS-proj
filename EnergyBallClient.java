import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class EnergyBallClient {

    private BufferedImage sprite;
    private BufferedImage energyBallExplosion;
    private boolean exploded = false;
    private boolean ended = false;
    private String name;
    private Thread timer;
    private Sound fireSound;

    public EnergyBallClient(String name) {
        try {
            fireSound = new Sound(new File("/home/user/Documents/github/Q4-AD-CS-proj/assets/EnergyBallSound.wav"));
        } catch (UnsupportedAudioFileException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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

    public Sound getFireSound() {
        return fireSound;
    }
}
