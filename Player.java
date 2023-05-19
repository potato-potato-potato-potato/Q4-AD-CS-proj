import HashMap.*;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.io.File;

import javax.imageio.ImageIO;

public class Player {

    public Vector velocity = new Vector(0, 0);

    public String name;
    public boolean isTouchingGround;
    public boolean isTouchingWall;

    public BufferedImage idleSpriteSheet;

    private MyHashMap<String, BufferedImage> playerSprites;

    public Rectangle HITBOX = new Rectangle(0, 0, 32, 32);
    // means player get 2 jumps
    public int jumpCount = 2;

    // percent multiplier for knockback
    public double knockBackStrength = 1.0;

    public Player(String name) {
        this.name = name;
    }

    public void playerSpritesSetUp() {
        playerSprites = new MyHashMap<String, BufferedImage>();
        try {
            idleSpriteSheet = ImageIO.read(new File("src\\main\\resources\\sprites\\player\\idle.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        playerSprites.put("idleSpriteSheet", idleSpriteSheet);

    }

    public void update() {
        HITBOX.translate((int) velocity.getXDirection(), (int) velocity.getYDirection());
    }

    public void draw(Graphics g, int x, int y) {
        g.drawImage(idleSpriteSheet, x, y, null);
    }

}
