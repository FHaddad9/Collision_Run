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
		x = 100;
		y = 100;	
		
		// All characters uses the same speed
		speed = 4;
		
		// Defailt direction is down
		direction = "down";
	}
	
	public void update() {
		
		// Player sprite changes only when key is pressed
		if(keyH.up || keyH.down || keyH.left || keyH.right) {
			
			// When user presses key, player position moves by 4 (speed) pixels
			if(keyH.up) {
				direction = "up";
				y -= speed;
			} else if(keyH.down) {
				direction = "down";
				y += speed;
			} else if(keyH.left) {
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
			
			// Gets images of sprites into IO and displays
			up1 = ImageIO.read(getClass().getResourceAsStream("/player/oldman_up_1.png"));
			up2 = ImageIO.read(getClass().getResourceAsStream("/player/oldman_up_2.png"));
			down1 = ImageIO.read(getClass().getResourceAsStream("/player/oldman_down_1.png"));
			down2 = ImageIO.read(getClass().getResourceAsStream("/player/oldman_down_2.png"));
			left1 = ImageIO.read(getClass().getResourceAsStream("/player/oldman_left_1.png"));
			left2 = ImageIO.read(getClass().getResourceAsStream("/player/oldman_left_2.png"));
			right1 = ImageIO.read(getClass().getResourceAsStream("/player/oldman_right_1.png"));
			right2 = ImageIO.read(getClass().getResourceAsStream("/player/oldman_right_2.png"));
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g2) {
		
		BufferedImage image = null;
		
		// Get walking animation between 2 sprites
		switch(direction) {
		case "up":
			if(spriteNum == 1) {
				image = up1;
			}
			
			if(spriteNum == 2) {
				image = up2;
			}
			
			break;
		case "down":
			if(spriteNum == 1) {
				image = down1;
			}
			
			if(spriteNum == 2) {
				image = down2;
			}
			
			break;
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
		
		g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
	}
}
