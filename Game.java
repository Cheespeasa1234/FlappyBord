import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

//Honors Computer Science - Mr. Uhl
//Program description: A template class for creating graphical applications

public class Game extends JPanel implements MouseListener, KeyListener
{
   //Variables for the class
	
   private static final long serialVersionUID = 1L;
   public static final int PREF_W = 1111;
   public static final int PREF_H = 696;
   
   public static boolean showingHitboxes = false;
   
   
   public int birdScale = 6;
   public static int pipeScale = 2;
   
   //physics vars, for just this class
   private int x = 300; 		//bird xpos
   private int y = 100; 		//bird ypos
   private int dy = 0;  		//velocity
   private int jumpHeight = 14; //velocity change in jump
   private int maxSpeed = 15;   //max speed in any direction
   private int scrollSpeed = 4; //how fast stuff moves
   private int deltaTime = 1;   //delta time
   
   private int score = 0;

   //static b/c i need to reference these in Pipe class for maths...
   public static Image bird = new ImageIcon(Game.class.getResource("bird.png")).getImage();
   public static Image pipe = new ImageIcon(Game.class.getResource("pipe.png")).getImage();   
   public static Image tube = new ImageIcon(Game.class.getResource("tube.png")).getImage(); 
   public static Image sky = new ImageIcon(Game.class.getResource("sky.jpg")).getImage();
   
   private Rectangle birdHitbox = new Rectangle(x + birdScale * 10, y - bird.getHeight(null), bird.getWidth(null) / 8 - (birdScale * 5), bird.getHeight(null) / 8);

   public ArrayList<Pipe> pipes = new ArrayList<Pipe>();
   
   public Timer pipeSpawnTimer = new Timer(1500, new ActionListener() {
	   	public void actionPerformed(ActionEvent e) {
	   		pipes.add(new Pipe(PREF_W, rand(200, PREF_H - 200)));
	   	}
   });
   
   public Timer timer = new Timer(1000/ 60, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			y += dy * deltaTime;
			
			if(dy <= maxSpeed) 
				dy += 1 * deltaTime;
				
			if(y <= 0) {
				y = 1;
				dy = 0;
			}
			if(y >= PREF_H) y = PREF_H - 1;
			
			for(int i = 0; i < pipes.size(); i++) {
				pipes.get(i).setX(pipes.get(i).x - scrollSpeed);
				if(pipes.get(i).x <= -200) {
					pipes.remove(i);
				}
				if(i <= 2) {
					if(pipes.get(i).x <= x) {
							score++;
					} else if(pipes.get(i).x <= x + 200) {
							pipes.get(i).testHitboxes = true;
					}
				}
			}
			
			birdHitbox = new Rectangle(x + birdScale * 2, y + 2, bird.getWidth(null) / (birdScale + 1) - birdScale * 2, bird.getHeight(null) / (birdScale + 1));
			
			repaint();
		}
   });
   
   public int rand(int min, int max) {
	   return (int) Math.floor(Math.random()*(max-min+1)+min);
   }
   
   //Class constructor BOB tHE BUILDER CAN hE FIX IT?????T?T??/
   public Game()
   {
      this.setFocusable(true);
      this.setBackground(Color.WHITE);
      addMouseListener(this);
      timer.start();
      pipeSpawnTimer.start();
      this.addMouseListener(this);
      this.addKeyListener(this);
   }
   
   //The method used to add graphical images to the panel
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;
      
      g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
      
      
      //draw the sky... a bit transparent
      float alpha = 0.5f;
      AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha * deltaTime);
      g2.setComposite(ac);

      g2.drawImage(sky, 0, 0, this);
      
      alpha = 1f;
      if(deltaTime == 0) alpha = 0.5f;
      ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha);
	  g2.setComposite(ac);
      
      g2.drawImage(bird, x, y, (bird.getWidth(this) / birdScale), bird.getHeight(this) / birdScale, this);
      
      for(int i = 0; i < pipes.size(); i++) {
    	  pipes.get(i).draw(g2);
    	  
		  for(int hitboxLoop = 0; hitboxLoop < 2; hitboxLoop++) { //loop through the hitboxes of each pipe
			  
			  if(showingHitboxes) {
				  g2.fill(pipes.get(i).hitboxes[hitboxLoop]);
			  }
			  
			  if(i < 3) {
				  if(pipes.get(i).hitboxes[hitboxLoop].intersects(birdHitbox)) {
					  pipeSpawnTimer.stop();
					  scrollSpeed = 0;
					  dy = 0;
					  deltaTime = 0;
					  
					  g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
	
					  g2.setFont(new Font("Monato", Font.BOLD, 40));
					  g2.drawString("You lose!", PREF_W / 2 - g2.getFontMetrics().stringWidth("You lose!") / 2, 100);
				 
					  g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
					  
				  }	 
			  }
    		  
    	  }
		  
		  //birdhitboxdisplay
		  //hello
		  if(showingHitboxes) g2.fill(birdHitbox);
		  
      }
   }
   
   
/** *******  METHODS FOR INITIALLY CREATING THE JFRAME AND JPANEL  *********/

   public Dimension getPreferredSize()
   {
      return new Dimension(PREF_W, PREF_H);
   }
   
   public static void createAndShowGUI()
   {
      JFrame frame = new JFrame("My First Panel!");
      JPanel gamePanel = new Game();
      
      frame.getContentPane().add(gamePanel);
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
      
   }
   
   public static void main(String[] args)
   {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            createAndShowGUI();
         }
      });
   }

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("You released a key.");
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			dy = -jumpHeight;
		} else if(e.getKeyCode() == KeyEvent.VK_H) {
			showingHitboxes = !showingHitboxes;
		}
	}
}