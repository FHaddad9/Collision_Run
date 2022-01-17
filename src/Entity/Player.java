package Entity;

import main.KeyHandler;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Player extends Entity{

	GamePanel gp;
	KeyHandler keyH;
	
	
	public Player(GamePanel gp, KeyHandler keyH) {
		
		this.gp = gp;
		this.keyH = keyH;
		
		setDefaultValues();
		getPlayerImage();
	}
	
	public void setDefaultValues() {
		
		// Default position of character
		x = 350;
		y = 500;	
		
		// All characters uses the same speed
		speed = 5;
		
		// Defailt direction is down
		direction = "right";
	}
	
	public void update() {
		
		// Player sprite changes only when key is pressed
		if(keyH.left || keyH.right) {
			
			// When user presses key, player position moves by 4 (speed) pixels
			if(keyH.left) {
				direction = "left";
				x -= speed;
			} else if(keyH.right) {
				direction = "right";
				x += speed;
			}
			
			spriteCounter++;
			
			// Player sprite transitions every 10 frames
			if(spriteCounter > 12) {
				if(spriteNum == 1) {
					spriteNum = 2;
				} else if(spriteNum == 2) {
					spriteNum = 1;
				}
				
				spriteCounter = 0;
			}
		}
	}
	
	public void getPlayerImage() {
		
		try {
			
			// Gets images of sprites into IO and display
			left1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_1.png"));
			left2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_2.png"));
			right1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_1.png"));
			right2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_2.png"));
			fire = ImageIO.read(getClass().getResourceAsStream("/player/Fireball.png"));
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g2) {
		
		BufferedImage image = null;
		BufferedImage image2 = fire;
				
		// Get walking animation between 2 sprites
		switch(direction) {
		case "left":
			if(spriteNum == 1) {
				image = left1;
			}
			
			if(spriteNum == 2) {
				image = left2;
			}
			
			break;
		case "right":
			if(spriteNum == 1) {
				image = right1;
			}
			
			if(spriteNum == 2) {
				image = right2;
			}
			
			break;
		}
		
		g2.drawImage(image, gp.posX, gp.posY, gp.tileSize, gp.tileSize, null);
		g2.drawImage(image2, gp.colX, gp.colY, gp.tileSize, gp.tileSize, null);
		
		update();
	}
}
