
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
	
	// screen dimensions
	final int normalTileSize = 16;	// 16x16 tiles, basically since it's 16 bit game
	final int scale = 3;	// Enlarge the 16 pixels
	final int tileSize = normalTileSize * scale;	// Scaled tile
	
	// Ratio 4:3 screen
	final int screenCol = 16;
	final int screenrow = 12;
	final int screenWidth = tileSize * screenCol;	// 768 pixel
	final int screenHeight = tileSize * screenrow;	// 576 pixel
	
	// Controls the time of the game; starting and stopping
	Thread gameThread;
	
	// Gets keys pressed by user
	KeyHandler key = new KeyHandler();
	
	// Player's default position
	int posX = 350;
	int posY = 500;
	int speed = 5;
	
	// Collision's positions
	int colX;
	int colY;
	Random random;
	
	// Point's positions
	int pointX;
	int pointY;
	int pointObtained = 0;
	
	// Frames per second
	int fps = 60;
	
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
		colY = 10;
	}
	
	public void newPoint() {
		pointX = random.nextInt( (int) (screenWidth / normalTileSize)) * normalTileSize;
		pointY = 515;
	}
	
	public void dropCollision() {
		if(colY >= 576) {
			newCollision();
		} else {
			colY += speed + 2;
		}
	}
	
	public void checkPoint() {
		if(posX == pointX) {
			pointObtained++;
			newPoint();
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Graphics2D better than Graphics
		Graphics g2 = (Graphics2D)g;
		
		// Temporary "Character" for game. Which is just a white square :p
		g2.setColor(Color.white);		
		g2.fillRect(posX, posY, tileSize, tileSize);
		
		g2.fillRect(0, 560, screenWidth, screenHeight);
		
		g.setColor(Color.red);
		g.fillRect(colX, colY, normalTileSize, normalTileSize);
		
		g.setColor(Color.green);
		g.fillOval(pointX, pointY, normalTileSize, normalTileSize);
		//System.out.println("Points: " + pointObtained);
		
		g2.dispose();
	}
}