import java.util.*;
import java.awt.*;
//import javax.swing.*;
import java.awt.event.*;

public class BoardArea extends Canvas implements FiniteStateMachine {

   public static Label		 coordinates;
   public static FractalWindow parent;
   private ArrayList<Point>		   points;
   private int				 pointCount, threshhold;
   //private int				 offset	 = 0;
   private int				 W, H;
   private Random			  rGen;
   //private double			  multiplier = 0.5;
   private int				 state;
   private java.awt.Point	  mouse; //, point1, point2;

   public BoardArea(FractalWindow ctrl, int heightPixels, int widthPixels) {

	  parent = ctrl;
	  pointCount = 0;
	  points = new ArrayList<Point>();
	  setState(SETTING_POINTS);
	  threshhold = 10;
	  W = widthPixels;
	  H = heightPixels;
	  setSize(new java.awt.Dimension(H, W));
	  setLocation(new java.awt.Point(30, 60));
	  rGen = new Random();
	  setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

	  addMouseListener(new MouseAdapter() {

		 public void mousePressed(MouseEvent e) {

			mouse = e.getPoint();
			if (getState() == SETTING_POINTS) {

			   pointCount++;
			   System.out.println(pointCount);
			   points.add(mouse);

			}

			mouseLocation(mouse);
			repaint();
		 }

		 public void mouseEntered(MouseEvent e) {

		 }

		 public void mouseExited(MouseEvent e) {
			//parent.coordinates.setText(" ");
		 }

	  });

	  addMouseMotionListener(new MouseMotionAdapter() {
		 public void mouseMoved(MouseEvent e) {
			mouse = e.getPoint();
			mouseLocation(mouse);
		 }

	  });
   }

   
   public ArrayList<Point> getPixelsOnLine(Point p1, Point p2) {
	  ArrayList<Point> pixelsOnLine = new ArrayList<Point>();
	    
	    // Calculate the slope of the line between the two points
	    double slope = (double) (p2.y - p1.y) / (p2.x - p1.x);
	    
	    // Calculate the y-intercept of the line
	    double yIntercept = p1.y - slope * p1.x;
	    
	    // Determine the minimum and maximum x and y values between the two points
	    int minX = Math.min(p1.x, p2.x);
	    int maxX = Math.max(p1.x, p2.x);
	    int minY = Math.min(p1.y, p2.y);
	    int maxY = Math.max(p1.y, p2.y);
	    
	    // Loop through each pixel between the minimum and maximum x and y values
	    for (int x = minX; x <= maxX; x++) {
	        for (int y = minY; y <= maxY; y++) {
	            // Calculate the distance between the current pixel and the line
	            double distance = Math.abs(slope * x - y + yIntercept) / Math.sqrt(1 + slope * slope);
	            
	            // If the distance is less than or equal to 0.5, add the pixel to the list
	            if (distance <= 0.5) {
	                pixelsOnLine.add(new Point(x, y));
	            }
	        }
	    }
	    
	    return pixelsOnLine;
	}

   public void mouseLocation(java.awt.Point pt) {
	  //parent.coordinates.setText((int) pt.getY() + " " + (int) pt.getX());
   }

   // paint on the canvas object
   public void paint(Graphics g) {
	  super.paint(g);

	  // draw border
	  g.setColor(Color.black);
	  g.drawLine(0, 0, 0, W - 1);
	  g.drawLine(0, 0, H - 1, 0);
	  g.drawLine(H - 1, 0, H - 1, W - 1);
	  g.drawLine(0, W - 1, H - 1, W - 1);

	  /**
	   * paint the endpoints RED
	   */
	  g.setColor(Color.red);
	  Iterator<Point> itr = points.iterator();
	  while (itr.hasNext()) {
		 Point p = (Point) itr.next();
		 g.drawRect((int) p.getX(), (int) p.getY(), 2, 2);
	  }

	  if (getState() == DRAWING && points.size() > 1 && threshhold > 1) {

		 Point last = points.get(points.size() - 1);
		 g.setColor(Color.black);
		 itr = points.iterator();
		 while (itr.hasNext()) {
			Point next = (Point) itr.next();
			drawFractal(g, last, next);
			last = next;
		 }
	  }
	  if(points.size()>=2) {
		 g.setColor(Color.black);
		 for(int i=0; i<points.size()-1; i++)
			for(int j=i+1; j<points.size(); j++) {
            	  ArrayList<Point> pixelsOnLine = getPixelsOnLine(points.get(i), points.get(j));
            	  for (Point pixel : pixelsOnLine)
            	       g.drawLine((int) points.get(i).getX(), (int) points.get(i).getY(), (int) points.get(j).getX(), (int) points.get(j).getY());	   
			}
	  }
   }

   public void drawFractal(Graphics g, Point p1, Point p2) {

	  boolean up = false;
	  double magnitude = distance(p1, p2);

	  up = rGen.nextBoolean();
	  

	  g.setColor(Color.black);
	  if (magnitude < threshhold) // base case if the points are too close to
								  // divide further

		 g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(),
			   (int) p2.getY());

	  else {

		 // vector
		 Vector unitV = new Vector(
			   ( p2.getX() -  p1.getX()) / magnitude, 
			   ( p2.getY() -  p1.getY()) / magnitude
			 );

		 Vector orthogV = new Vector(  -unitV.getY(), unitV.getX()  );

		 double ratio = rGen.nextDouble();
		 double offset = ratio * magnitude / 2;

		 if (!up)
			orthogV = new Vector(-orthogV.getX(), -orthogV.getY());

		 Vector resultant = new Vector(
			   unitV.getX() * magnitude / 2 + orthogV.getX() * offset, 
			   unitV.getY() * magnitude / 2 + orthogV.getY() * offset);

		 Point middlePoint = new Point((int) (p1.getX() + resultant.getX()),
			   (int) (p1.getY() + resultant.getY()));

		 drawFractal(g, p1, middlePoint);
		 drawFractal(g, middlePoint, p2);
	  }

   }

   /**
    * 
    * @param p1
    *           -- first point
    * @param p2
    *           -- second point
    * @return -- the Cartesian distance between the points
    */
   private double distance(Point p1, Point p2) {

	  return Math.sqrt(Math.pow((int) p1.getX() - (int) p2.getX(), 2)
			+ Math.pow((int) p1.getY() - (int) p2.getY(), 2));

   }

   public void draw() {
	  setState(DRAWING);
	  repaint();
   }

   public void setThreshhold(int n) {
	  threshhold = n;
   }

   public void reset() {
	  pointCount = 0;
	  points.clear();
	  setState(SETTING_POINTS);
	  repaint();
   }

   public void setState(int s) {
	  state = s;
   }

   public int getState() {
	  return state;
   }

}
