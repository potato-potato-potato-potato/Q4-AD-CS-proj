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

public class PlayerImages {

    private int[] nums;
    private int playerNum;
    private String name;
    private BufferedImage currentImage;
    public PlayerImages(String name, int[] nums) {
        this.name = name;
        this.nums = nums;
        if(name.equals("P1")){
            playerNum = 1;
        }else if(name.equals("P2")){
            playerNum = 2;
        }else if(name.equals("P3")){
            playerNum = 3;
        }else{
            playerNum = 1;
            System.out.println("Invalid player number: " + name);
        }
    }

    public void update(int[] nums) {
        this.nums = nums;
    }

    private void updateImg() throws IOException{
        int img = nums[3];
        if(img/100==0){//idle
            if(img/10==0){//first idle
                currentImage = ImageIO.read(new File("assets/src/craftpix-net-440863-free-knight-character-sprites-pixel-art/Knight_" + playerNum + "/Idle.png"));
            }
        }else if(img/100==1){//running
        
        }else if(img/100==2){//jumping
        
        }else if(img/100==3){//running
        
        }else if(img/100==4){//running
        
        }else if(img/100==5){//running
        
        }
    }

    public void drawMe(Graphics g) {
        g.drawImage(currentImage, nums[0], nums[1], null);
        g.drawRect(nums[0], nums[1], 10, 50);//hitbox
        g.drawString(name, nums[0], nums[1]);
    }
}
