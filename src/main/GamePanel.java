package main;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

import javax.swing.JPanel;

import Entity.Player;

public class GamePanel extends JPanel implements Runnable{
	
	// screen dimensions
	final int normalTileSize = 16;	// 16x16 tiles, basically since it's 16 bit game
	final int scale = 3;	// Enlarge the 16 pixels
	public final int tileSize = normalTileSize * scale;	// Scaled tile
	
	// Ratio 4:3 screen
	final int screenCol = 16;
	final int screenrow = 12;
	final int screenWidth = tileSize * screenCol;	// 768 pixel
	final int screenHeight = tileSize * screenrow;	// 576 pixel
	
	// Controls the time of the game; starting and stopping
	Thread gameThread;
	
	// Gets keys pressed by user
	KeyHandler key = new KeyHandler();
	
	// Get player class for entitys
	Player player = new Player(this, key);
	
	// Player's default position
	public int posX = 350;
	public int posY = 500;
	int speed = 5;
	
	// Collision's positions
	public int colX;
	public int colY;
	int colSpeed = 5;
	Random random;
	
	// Point's positions
	int pointX;
	int pointY;
	int pointObtained = 0;
	
	// Frames per second
	int fps = 60;
	
	boolean gameOver = false;
	
	public GamePanel() {
		
		// Constructor for the window
		this.random = new Random();
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(key);
		this.setFocusable(true);
	}

	public void startGameThread() {
		
		// Starts the game by calling the run method
		gameThread = new Thread(this);
		gameThread.start();
		
		newCollision();
		newPoint();int num = 0;
		System.out.println(num);
		num++;
	}
	
	@SuppressWarnings("deprecation")
	public void stopGameThread() {
		gameThread.stop();
	}
	
	public void run() {
		
		// Update: update information
		// Draw: draw screen with information
		
		// Interval is 1 second in 1 billion nanoseconds
		double interval = 1000000000 / fps;	// 0.01666 seconds
		double delta = 0;
		
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;
		
		// Delta gets the interval between the time when loop started and current time and repaints
		while(gameThread != null) {
			
			
			currentTime = System.nanoTime();
			
			delta += (currentTime - lastTime) / interval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;
			
			if(delta >= 1) {
				update();
				repaint();
				delta--;
				drawCount++;
			}		
			
			if(timer >= 1000000000) {
				//System.out.println("FPS: " + drawCount);
				drawCount = 0;
				timer = 0;
			}
		}
	}
	
	public void update() {
		
		// When user presses key, player position moves by 4 (speed) pixels
		if(key.left) {
			posX -= speed;
		} else if(key.right) {
			posX += speed;
		}
		
		dropCollision();
		checkPoint();
	}
	
	public void newCollision() {
		// set random collision of food in coordinates
		colX = random.nextInt( (int) (screenWidth / normalTileSize)) * normalTileSize;
		while((colX % 5) != 0) {
			colX = random.nextInt( (int) (screenWidth / normalTileSize)) * normalTileSize;
		}
		colY = 10;
	}
	
	public void newPoint() {
		pointX = random.nextInt( (int) (screenWidth / normalTileSize)) * normalTileSize;
		while((pointX % 5) != 0) {
			pointX = random.nextInt( (int) (screenWidth / normalTileSize)) * normalTileSize;
		}
		pointY = 515;
	}
	
	public void dropCollision() {
		
		if(((colX >= posX) && (colX <= (posX+tileSize)) && ((colY >= posY) && (colY <= (posY+tileSize))))) {
			gameOver = true;
		} else {			
			if(colY >= 576) {
				newCollision();
			} else {
				colY += colSpeed + 2;
			}	
		}
		
		if((pointObtained >= 1) && (pointObtained % 5 == 0)) {
			pointObtained++;
			colSpeed += 2;
		}
		
		
	}
	
	public void checkPoint() {
		System.out.println("posX: " + posX);
		System.out.println("pointX: " + pointX);
		
		if(pointX >= posX && pointX <= (posX+tileSize)) {
			pointObtained++;
			newPoint();
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Graphics2D better than Graphics
		Graphics2D g2 = (Graphics2D)g;
		
		player.draw(g2);
////		
////		// Temporary "Character" for game. Which is just a white square :p
////		g2.setColor(Color.white);		
////		g2.fillRect(posX, posY, tileSize, tileSize);
		g2.setColor(Color.white);	
		g2.fillRect(0, 560, screenWidth, screenHeight);
		
		g.setColor(Color.green);
		g.fillOval(pointX, pointY, normalTileSize, normalTileSize);
		//System.out.println("Points: " + pointObtained);
		
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Score: " + pointObtained, ((screenWidth - metrics.stringWidth("Score: " + pointObtained)) / 2), g.getFont().getSize());
		
		if(gameOver) {
			repaint();
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 75));
			FontMetrics metrics2 = getFontMetrics(g.getFont());
			g.drawString("Game Over", ((screenWidth - metrics2.stringWidth("Game Over")) / 2), (screenHeight / 4));
			stopGameThread();
		}
		
		g2.dispose();
	}
}