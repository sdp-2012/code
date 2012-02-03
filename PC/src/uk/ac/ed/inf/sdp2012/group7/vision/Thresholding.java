package uk.ac.ed.inf.sdp2012.group7.vision;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Point;
import java.util.ArrayList;

/**
 * 
 * @author s0951580
 * TODO: get WHICH ROBOT ARE WE? and find the centroid of the plate that is ours
 * TODO: clustering algorithm to get the T right 
 * TODO: BLUE ROBOT IS CURRENTLY HARD CODED, needs to be done for the other pitch as well 
 */


public class Thresholding {

	private ArrayList<Point> yellowRobot = new ArrayList<Point>();
	private ArrayList<Point> blueRobot = new ArrayList<Point>();
	private ArrayList<Point> greenPlates = new ArrayList<Point>();
	
    private Color c;
    
	private int GB;// green - blue
	private int RG; // red - green
	private int RB; // red - blue
	private int RGthresh;
	
    private int[][] redBallThresh= new int[2][3];
    private int[][] yellowRobotThresh= new int[2][3];
    private int[][] blueRobotThresh= new int[2][3];
    private int[][] greenPlatesThresh= new int[2][1];
    
    private int pitch;
    private int height;
    private int width;
    
    private Point ballCentroid = new Point();
    private Point blueCentroid = new Point();
    private Point yellowCentroid = new Point();
    private Point blueGreenPlateCentroid = new Point();
    
    private int ballCount;
    private int yellowCount;
    private int blueCount;
    private int robot; // 0 for Yellow, 1 for Blue(our robot) 
    

    
    
    public Thresholding(int pitch) {  // Sets the constants for thresholding for each pitch 
    	redBallThresh[0][0] = 130;
    	redBallThresh[0][1] = 100;
    	redBallThresh[0][2] = 100;
    	redBallThresh[1][0] = 150;
    	redBallThresh[1][1] = 110;
    	redBallThresh[1][2] = 110;
    	yellowRobotThresh[0][0] = 140;
    	yellowRobotThresh[0][1] = 140;
    	yellowRobotThresh[0][2] = 170;
		yellowRobotThresh[1][0] = 150;
		yellowRobotThresh[1][1] = 190;
		yellowRobotThresh[1][2] = 140;
		blueRobotThresh[0][0] = 150;
		blueRobotThresh[0][1] = 150;
		blueRobotThresh[0][2] = 100;
		blueRobotThresh[1][0] = 150;
		blueRobotThresh[1][1] = 150;
		blueRobotThresh[1][2] = 100;
		greenPlatesThresh[0][0] = 155;
		greenPlatesThresh[1][0] = 155;

    	this.pitch=pitch;
    	//this.robot = robot;
    }
    public BufferedImage getThresh(BufferedImage img, int left, int right, int top, int bottom) { // Method to get thresholded image 
		   	
		   width = right-left;
		   height = top-bottom;
		 //  BufferedImage threshed = new BufferedImage(width,height, BufferedImage.TYPE_INT_ARGB);
	    	
           ballCount = 0;
           ballCentroid.setLocation(0,0);
            
           blueCount = 0;
           blueCentroid.setLocation(0,0);
            
           yellowCount = 0;
           yellowCentroid.setLocation(0,0);

           Point p = new Point();
	    	for (int i = left; i < right; i++) {
				for (int j = top; j < bottom; j++) {
					c = new Color(img.getRGB(i,j));
					GB = Math.abs((c.getBlue() - c.getGreen()));
					RG = Math.abs((c.getRed() - c.getGreen()));
					RB = Math.abs((c.getRed() - c.getBlue()));
					if( (c.getRed() > redBallThresh[pitch][0]) &&  (c.getBlue() <= redBallThresh[pitch][1]) &&  (c.getGreen() <= redBallThresh[pitch][2]) && GB < 40 ){ //  was inside  RB > 50 && RG > 50
						img.setRGB(i, j, Color.black.getRGB()); //Red Ball
						ballCount++;
						ballCentroid.setLocation(ballCentroid.getX() + i, ballCentroid.getY() + j);
						
					}
					/*if(  RB > 50 && RG > 50 && c.getRed() >= redBallThresh[pitch][0]){ //  was inside  RB > 50 && RG > 50
						img.setRGB(i, j, Color.black.getRGB()); //Red Ball
						ballCount++;
						ballCentroid.setLocation(ballCentroid.getX() + i, ballCentroid.getY() + j);
						
					}*/ // Did some experiments with this
					/*else if( RG < 35 &&  (c.getBlue() <= yellowRobotThresh[pitch][2]) && (c.getRed() > yellowRobotThresh[pitch][0])  && (c.getGreen() > yellowRobotThresh[pitch][1])   ){
						img.setRGB(i, j, yellow.getRGB()); // Yellow robot
						yellowCount++;
						yellowCentroid.setLocation(yellowCentroid.getX() + i, yellowCentroid.getY() + j);
					}*/ // LOL 
					else if(RB > 20 && RB < 50 && RG < 40 && (c.getRed() > yellowRobotThresh[pitch][0])  && (c.getGreen() > yellowRobotThresh[pitch][1])  &&  (c.getBlue() <= yellowRobotThresh[pitch][2])) {
						img.setRGB(i, j, Color.yellow.getRGB()); // Yellow robot
						p.setLocation(i, j);
						yellowRobot.add(p);
						yellowCount++;
						yellowCentroid.setLocation(yellowCentroid.getX() + i, yellowCentroid.getY() + j);
					}
					else if( (c.getRed() <= 110) && (c.getBlue()>110)   && (c.getGreen() <= 165)){
						img.setRGB(i, j, Color.blue.getRGB()); // Blue robot 
						p.setLocation(i, j);
						blueRobot.add(p);
						blueCount++;
						blueCentroid.setLocation(blueCentroid.getX() + i, blueCentroid.getY() + j);
						//make blue thresholds for the different pitches in that [pitch][x] style
					}
					else if ( GB > 50 && RG > 50 && c.getGreen() > 160) {
						img.setRGB(i,j, Color.green.getRGB()); // GreenPlates 
						p.setLocation(i,j);
						greenPlates.add(p);
					}
				}
			}
			
			ballCentroid.setLocation(ballCentroid.getX()/ballCount, ballCentroid.getY()/ballCount);
			yellowCentroid.setLocation(yellowCentroid.getX()/yellowCount, yellowCentroid.getY()/yellowCount);
			blueCentroid.setLocation(blueCentroid.getX()/blueCount, blueCentroid.getY()/blueCount);
			
			blueGreenPlateCentroid = findCentroid(getGreenPlateBlue(greenPlates));
			
	    	return img;
    }
    /**
     * 
     * @param allGreenThings
     * @return ourGreen
     *  Given all green points return the green points around the blue robot
     */
    public ArrayList<Point> getGreenPlateBlue(ArrayList<Point> allGreenThings){
    	ArrayList<Point> ourGreen = new ArrayList<Point>();
    	for (int i = 0; i < greenPlates.size(); i++) {
			if( (greenPlates.get(i).x > blueCentroid.x - 40) && (greenPlates.get(i).x < blueCentroid.x + 40) &&(greenPlates.get(i).y > blueCentroid.y - 40) && (greenPlates.get(i).y < blueCentroid.y + 40 ) ){
				ourGreen.add(greenPlates.get(i));
			}
		}
    	return ourGreen;
    }
    /**
     * 
     * @param listOfPoints
     * @return centroidPoint
     * Given an array of points return its centorid
     */
    public Point findCentroid(ArrayList<Point> listOfPoints){
    	int sumX = 0;
    	int sumY = 0;
    	for (int i = 0; i < listOfPoints.size(); i++) {
			sumX += listOfPoints.get(i).x;
			sumY += listOfPoints.get(i).y;
		}
    	
    	return new Point((int) (sumX/(double)listOfPoints.size() ), (int) (sumY/(double)listOfPoints.size()));
    }
    public Point getBallCentroid() {
        return ballCentroid;
    }
    
    public Point getBlueCentroid() {
        return blueCentroid;
    }
    
    public Point getYellowCentroid() {
        return yellowCentroid;
    }
    public Point getBlueGreenPlateCentori(){ 
    	return blueGreenPlateCentroid;
    }
	    
	    
}