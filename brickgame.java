//final homework
//a brick game
//B88901014
//B88901041
//B88901044
import java.applet.*;
import java.awt.*;
import java.awt.event.*;


public class brickgame extends Applet {
  
  private plate p;
  private wall w;
  private double ranX;          
  private double ranY;
  public ball b;
  public time t;
  public static int point;     // the score of a brick       
  public static int score = 0; // the total score of a player gets
  public static String s;      // show the current data
  public static int controll;  // controll = 0 means game stops 
  public static int brCounter; // the number of current bricks
  public brick [] br; 
 
  public void init()
  {
  	requestFocus();

    w = new wall(10, 60, 480, 330);
	p = new plate( 200, w, this);
	t = new time(this, 0);
	ranX = Math.random();
	ranY = Math.random();
	s = " Current number of bricks : " + brCounter + "     Score : " + point + "    Time: " + t.second + " seconds";  
	point = 50;
	controll = 1;
	brCounter = 0;
	br = new brick[63];
	// generate bricks randomly
	for(int i = 0; i < 9; i++)
	{
	 for(int j = 0; j < 7; j++)
	 { 
	   boolean visible = Math.random() > 0.2;
	   switch (j%3) 
	   {
	     case 0:
            br[i*7+j] = new brick(50+i*45, 90+j*20, 35, 10, this, visible);
	        break;
	     case 1:
            br[i*7+j] = new brick(65+i*45, 90+j*20, 35, 10, this, visible);
	        break;
	     case 2:
            br[i*7+j] = new brick(35+i*45, 90+j*20, 35, 10, this, visible);
	        break;
	   }
	   if (visible)
	   {
	   	brCounter++;
	   }
	 }
	}
	
    b = new ball(250, 375, 5, w, p, this, ranX, ranY);
  }

  public void paint(Graphics g)
  {
    g.setColor(Color.black);
    g.fill3DRect(w.l, w.t, w.w, w.h, true);
	g.fill3DRect(0, 0, 500, 50, true);

    g.setColor(Color.green);
    for(int i=0; i<500; i+=10) {
      g.fill3DRect(i, 50, 10, 10, false);
      g.fill3DRect(i, 390, 10, 10, false); 
    }
    for(int i=50; i<400; i+=10) {
      g.fill3DRect(0, i, 10, 10, false);
      g.fill3DRect(490, i, 10, 10, false);
    }
    b.draw();
	p.draw();
	for(int i = 0; i < br.length; i++)
	{
		br[i].draw();
	} 
  }

   
  
  public boolean keyDown(Event e, int key)
  {
    showStatus(Integer.toString(key));
    
	switch(key) {
       case 97:
          b.start();
		  p.start();
		  t.start();
		  break;
	   case 1006:
	      p.setdx(-10);	
		  break;
       case 1007:
	      p.setdx(10);
		  break;
	   case 98:
	  	  if (controll == 0)       // because when controll = 1,press "b" is forbidden
		  {
		    p.erase();
		    for(int i = 0; i < br.length; i++)
		    {
			  br[i].erase();
		    }
			
		    init();                // generate new game 
			
		    for(int i = 0; i < br.length; i++)
	        {
		       br[i].draw();
	        }
		    b.draw();
		    p.draw();
		  }
		    break;
        }	
    return true;
  }

  public void show(String s)
  {
    showStatus(s);
  }
}


class ball extends Thread
{
  private int x;              // x coordniate of the ball's center 
  private int y;              // y coordinate of the ball's center
  private double randomX;     // random number to decide the value of dx
  private double randomY;
  public int dx;              // the amount of a ball moves in x component  
  public int dy;
  private int rad;            // ball's radius 
  public static int sleepTime; 
  private Graphics g;
  private wall w;
  private plate p;
  private brickgame bg;
  private Color color = new Color(1.0F, 0.6F, 0.5F);

  public ball(int x1, int y1, int r1, wall w1, plate p1, brickgame b1, double ranX, double ranY)
  {
    setxy(x1, y1);
    rad = r1;
    w = w1;
	sleepTime = 30;
	p = p1;
    bg = b1;
    g = bg.getGraphics().create();  // get the Graphics object
	
	randomX = ranX;
	randomY = ranY;
	
	if (randomX > 0.7) 
	{
		dx = 4;
	}else if(randomX > 0.35)
	{
		dx = -3;
	}else
	{
		dx = 2;
	}
	
	if (randomY > 0.7)
	{
		dy = -4;
	}else if(randomY > 0.35)
	{
		dy = -3;
	}else
	{
		dy = -2;
	}
	 
  }

  public void setxy(int x1, int y1)
  {
    x = x1;
    y = y1;
  }

  public int getx()
  {
    return x;
  }

  public int gety()
  {
    return y;
  }

  public int getr()
  {
    return rad;
  }

  public void erase()
  {
    g.setColor(Color.black);
    g.fillOval(x-rad, y-rad, rad*2, rad*2);
  }

  public void draw()
  {
    g.setColor(color);
    g.fillOval(x-rad, y-rad, rad*2, rad*2);
  }

  public void run()
  {
    while(bg.controll != 0) 
	{ 
	  g.setColor(Color.black);
	  g.fill3DRect(0, 0, 500, 50, true);
	  bg.s = " Current number of bricks : " + bg.brCounter + "     Score : " + bg.score + "     Time : " + bg.t.second + " seconds";
	  g.setColor(color);
	  g.drawString(bg.s, 30, 30);
	  
      erase();

      x += dx;
      y += dy;
      
	  // check if collision with bricks
      int k = 5;
	  int i;
	  for(i = 0; i < bg.br.length; i++)
	  { 
	  	k = bg.br[i].collision( rad, x, y);
		if(k != 5)                           // k = 5 means no collision with bricks
			break;
	  }
	  if (k == 1)                            // k = 1 means ball hits top
	  {
	  	bg.br[i].erase();
		y = 2*(bg.br[i].gety1() - rad) - y;
		dy = -dy;
	  }else if(k == 2)                       // k = 2 means ball hits bottom
	  {
	  	bg.br[i].erase();
		y = 2*(bg.br[i].gety1()+ 10 + rad) - y;
		dy = -dy;
	  }else if(k == 3)                       // k = 3 means ball hits left side
	  {
	  	bg.br[i].erase();
		
		x = 2*(bg.br[i].getx1() - rad) - x;
		dx = -dx;
	  }else if(k == 4)                       // k = 4 means ball hits right side
	  {
	  	bg.br[i].erase();
	
		x = 2*(bg.br[i].getx1()+ 35 + rad) - x;
		dx = -dx;
	   }
	  i = 0;
	  k = 5;                               
	
	  // check if collision happens at wall
      if(x >= w.r-rad) 
	  {
        x = 2*(w.r-rad)- x - 1;
	    dx = -dx;
      } else if(x <= w.l+rad) 
	  {
        x = 2*(w.l+rad)-x; 
	    dx = -dx;
      }

      if(y > w.b-rad)         // if ball hits the bottom, stop the game
	  { 
	  	y = 2*(w.b-rad)- y - 1;
		bg.controll = 0;
      } else if(y <= w.t+rad) 
	  {
        y = 2*(w.t+rad)-y;
	    dy = -dy;
      }
	  
	  // check if ball falls on the plate
	  if ( (y >= 380 - rad) && (x >= p.getx()) && (x <= p.getx() + 100))
	  {
	  	y = 2*(380-rad) - y;
		dy = -dy;
	  }                          

      draw();      // draw the ball in new coordinate 
             
      try 
	  {
        Thread.sleep(sleepTime);
      }
      catch(InterruptedException e)
	   {
        bg.show(e.toString());
      }
    }
	
	erase();
	g.setColor(Color.black);
	g.fill3DRect(0, 0, 500, 50, true);
	bg.s = " GAME OVER !!!    You get score " + bg.score + "     Time : " + bg.t.second + " seconds!!";
	g.setColor(Color.white);
	g.drawString(bg.s, 30, 30);
	
	if (bg.brCounter != 0)    // if not complete game, set score 0 when restart the game
	{
		bg.score = 0;
	}	
  }
}
				 
class wall
{
  public int l, r, t, b, w, h;

  public wall(int left, int top, int width, int height)
  {
    l = left;         
    t = top;
    w = width;
    h = height;
    r = left+width;
    b = top+height;
  }
}


class plate extends Thread
{ 
	private int x;
	private int deltax;
	private int length; 
    private Graphics g;  
	private wall w;
	private brickgame bg;
	
	public plate( int x1, wall w1, brickgame b2)
	{
		setx(x1);
		length = 100;
		deltax = 0;
		w = w1;
		bg = b2;
        g = bg.getGraphics().create();
	}
	
	public void setx(int x1)
	{
		x = x1;
	}
	
	public void setdx(int dx)
	{
		deltax = dx;
	}
	 
	public int getx()
	{
		return x;
	}
	 
	public void erase()
	{
		g.setColor(Color.black);
		g.fill3DRect( x, 380, length, 10, true);
	}
	
	public void draw()
	{
		g.setColor(Color.red);
		g.fill3DRect( x, 380, length, 10, true);
	}
	
	public void run()
	{
		while (bg.controll != 0)
		{
			if(deltax != 0)
			   erase();
			   
            x += deltax;		
		    if ((x + length) > w.r)
			{
				x = w.r - length;
			}
			else if(x < w.l) 
			{
			    x = w.l;
			}
		    
			draw();
		    deltax = 0;
			
			try
			{
				Thread.sleep(10);
			}
			catch(InterruptedException e)
			{
				bg.showStatus(e.toString());
			}
		}
		draw();
	}
}
			
		
class brick
{   
    boolean visible;    // decide if brick exists
	private int x1, y1, x2, y2, width, height;
	private Graphics g;
	private brickgame bg;
	
	public brick( int a, int b, int w, int h, brickgame b3, boolean v)
	{
		x1 = a;
		y1 = b;
		width = w;
        height = h;
		x2 = x1 + width;
		y2 = y1 + height;
		visible = v;
		bg = b3;
		g = bg.getGraphics().create();
	}
	
	public int getx1()
	{
		return x1;
	}
	public int gety1()
	{
		return y1;
	}
	
	public void erase()
	{
		g.setColor(Color.black);
		g.fill3DRect(x1, y1, width, height, false);
		visible = false;
		if (bg.controll != 0)
		{
		   bg.brCounter--;
		   bg.score += bg.point;
		   bg.point += 10;
		   switch (bg.brCounter)
		   {
			  case 40:
				 bg.b.sleepTime = 25;
				 break;
			  case 30:
				 bg.b.sleepTime = 20;
				 break;
			  case 15:
				 bg.b.sleepTime = 15;
                 break;
			  case 2:
			     bg.b.sleepTime = 10;
				 break;
		    }
		} 			
			  
		if (bg.brCounter == 0)     // if no bricks visible, the game is completed
		{
			bg.controll = 0;
		}
	}
	
	public void draw()
	{
		if (visible)
		{
		  g.setColor(Color.blue);
		  g.fill3DRect(x1, y1, width, height, false);
		}
	}
	
	// decide the ball hits which side of a brick
	public int collision( int ballRad, int ballx, int bally)
	{
		if (visible)
		{ 
		  if (ballx >= (x1-ballRad) && ballx <= (x2+ballRad) && bally >= (y1-ballRad) && bally <= (y2+ballRad)) 
		  {
		  	int v1x = x1 - ballx;     // a vector's x component
			int v1y = y1 - bally;
			int v2x = x2 - ballx;
			int v2y = y2 - bally;
			
			if (bg.b.dx > 0 && bg.b.dy > 0 )
			{
				if ((bg.b.dx*v1y - bg.b.dy*v1x) >= 0) 
				{
					return 1;
				}
				return 3;
			}else if(bg.b.dx > 0 && bg.b.dy < 0)
			{
				if ((bg.b.dx*v2y - bg.b.dy*v1x) >= 0)
				{
					return 3;
				}
				return 2;
			}else if(bg.b.dx < 0 && bg.b.dy > 0)
			{
				if ((bg.b.dx*v1y - bg.b.dy*v2x) >= 0)
				{
					return 4;
				}
				return 1;
			}else if(bg.b.dx < 0 && bg.b.dy < 0)
			{
				if ((bg.b.dx*v2y - bg.b.dy*v2x) >= 0)
				{
					return 2;
				}
				return 4;
			}
		  }
		}
	   return 5;
	} 
}
		  	

class time extends Thread
{
	public static int second;
	private brickgame bg;
	
	time(brickgame b4, int t)
	{
		bg = b4;
		second = t;
	}
	
	public void run()
	{
	   while (bg.controll != 0)
	   {
		  second++;
          try
          { 
	         Thread.sleep(1000);
          }catch(InterruptedException e)
          {
		  	bg.show(e.toString());
          }
	    }
	}
}
	


	 
		
	 
	  		
		
	
	
			  

	
	
		
	 
