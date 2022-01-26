import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

public class Pipe {
	
	public boolean testHitboxes = false;
	
	public Image pipe = Game.pipe;
	public Image tube = Game.tube;

	public int scale = Game.pipeScale;
	
	public int x;
	public int y;
	public int dist = 250;
	
	public Rectangle[] hitboxes = new Rectangle[4];

	/**
	 * 
	 * @param x The x position of the tube / split
	 * @param y The y position of the split
	 * 
	 */
	Pipe(int x, int splitY) {
		this.x = x;
		this.y = splitY;
	}
	
	Pipe(Point split) {
		this.x = (int) split.getX();
		this.y = (int) split.getY();
	}
	
	/**
	 * @param g2 The graphics object to draw to
	 * @return true
	 */
	boolean draw(Graphics2D g2) {
		
		//bottom
		g2.drawImage(tube,x+5,y + dist / 2 + pipe.getHeight(null) / scale + scale * 5, tube.getWidth(null) / scale, 1000, null);
		hitboxes[0] = new Rectangle(x + 5, y + dist / 2 + 10, tube.getWidth(null) / scale, 1000);
		
		//top
		g2.drawImage(tube,x+5,y - dist / 2, tube.getWidth(null) / scale, -1000, null);
		hitboxes[1] = new Rectangle(x + 5, 0, tube.getWidth(null) / scale, y - dist / 2 + pipe.getHeight(null) / scale);
		
		//bottom
		g2.drawImage(pipe, x, y + dist / 2 + 10, pipe.getWidth(null) / scale, pipe.getHeight(null) / scale, null);
		//hitboxes[2] = new Rectangle(x, y + dist / 2 + 10, pipe.getWidth(null) / scale, pipe.getHeight(null) / scale - 10);
		
		//top
		g2.drawImage(pipe, x, y - dist / 2, pipe.getWidth(null) / scale, pipe.getHeight(null) / scale, null);
		// hitboxes[3] = new Rectangle(x, y - dist / 2, pipe.getWidth(null) / scale, pipe.getHeight(null) / scale);
		
		return true;
		
	}
	
	boolean intersectsBird(Rectangle bird) {
		if(testHitboxes) {
			for(int i = 0; i < hitboxes.length; i++) {
				if(bird.intersects(hitboxes[i])) {return true;}
			}
		}
		return false;
	}
	
	void setX(int xv) {
		this.x = xv;
	} 
	void setY(int yv) {
		this.y = yv;
	}
	
}
