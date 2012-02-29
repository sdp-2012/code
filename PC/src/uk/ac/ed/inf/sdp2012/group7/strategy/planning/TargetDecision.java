package uk.ac.ed.inf.sdp2012.group7.strategy.planning;


import java.awt.Point;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import uk.ac.ed.inf.sdp2012.group7.strategy.PlanTypes;
import uk.ac.ed.inf.sdp2012.group7.strategy.Strategy;
import uk.ac.ed.inf.sdp2012.group7.vision.worldstate.WorldState;



/**
 * @author s0955088
 *
 */
public class TargetDecision {

	public static final Logger logger = Logger.getLogger(PlanningThread.class);
	
	/**
	 * 
	 */
	private AllMovingObjects all_moving_objects;
	private AllStaticObjects all_static_objects;
	private ArrayList<Point> obstacles;
	private int plan_type;
	private int action;
	private boolean clear_shot = false;
	private boolean we_have_ball = false;
	private boolean they_have_ball = false;
	private boolean ball_is_too_close_to_wall = false;
	private WorldState worldState = WorldState.getInstance();

	
	//Constructor
	public TargetDecision(AllMovingObjects aMO, AllStaticObjects aSO, ArrayList<Point> obstacles) {
		this.all_moving_objects = aMO;
		this.all_static_objects = aSO;
		this.obstacles = obstacles;
		this.plan_type = this.all_static_objects.getPlanType();
		this.clearShot();
		this.weHaveBall();
		this.theyHaveBall();
		this.ballTooCloseToWall();
		

	}
	
	
	public Point getTargetAsNode() {
		
		/*
		 * This whole section is experimental
		 */
		
		
		Point target = new Point();
		//put it into node for assessment
		//hack :o)
		target = all_static_objects.convertToNode(this.all_moving_objects.getBallPosition());
		//boolean for knowing if the ball is on the pitch
		boolean ballOnPitch = ((target.x >= 0) && (target.x <= all_static_objects.getWidth()) && 
							   (target.y >= 0) && (target.y <= all_static_objects.getHeight()));
		
		if(this.plan_type == PlanTypes.PlanType.FREE_PLAY.ordinal()){
		
			//Lets get this shit in, and then go read about proper decision making structures later.
			if(!ballOnPitch){
				//fuck off and sit next to our goal
				this.action = PlanTypes.ActionType.DRIVE.ordinal();
				logger.debug("Ball is not found on pitch");
				return this.all_static_objects.getInfront_of_our_goal();
				
			} else {
				if(this.ball_is_too_close_to_wall){
					//sit just near to the ball
					this.action = PlanTypes.ActionType.DRIVE.ordinal();
					return this.handlingBallTooCloseWall(target);
				}
				else {
					if(this.clear_shot){
						this.action = PlanTypes.ActionType.KICK.ordinal();
						return target;
					} else {
						this.action = PlanTypes.ActionType.DRIVE.ordinal();
						return target;
					}
				}
			
			}
		} 
		else if(this.plan_type == PlanTypes.PlanType.HALT.ordinal()){
			this.action = PlanTypes.ActionType.STOP.ordinal();
			return target;
		}
		//Penalty modes continue from here...
		else {
			this.action = PlanTypes.ActionType.STOP.ordinal();
			return target;		
		}				
	}


	private void weHaveBall(){

		Point our_position = all_moving_objects.getOurPosition();
		Point ball_position = all_moving_objects.getBallPosition();
		double our_angle = all_moving_objects.getOurAngle();

		if(40 < (int)our_position.distance(ball_position)){


			double angle_between_us_ball = Math.asin((ball_position.x - our_position.x)/our_position.distance(ball_position));

			if (angle_between_us_ball < 0){ 
				angle_between_us_ball = angle_between_us_ball + 360;
			}

			if(Math.abs(angle_between_us_ball - our_angle) < (30)){
				we_have_ball = true;
			}
		}


	}
	
	private void theyHaveBall(){

		Point their_position = all_moving_objects.getTheirPosition();
		Point ball_position = all_moving_objects.getBallPosition();
		double their_angle = all_moving_objects.getTheirAngle();

		if(40 < (int)their_position.distance(ball_position)){


			double angle_between_them_ball = Math.asin((ball_position.x - their_position.x)/their_position.distance(ball_position));

			if (angle_between_them_ball < 0){ 
				angle_between_them_ball = angle_between_them_ball + 360;
			}

			if(Math.abs(angle_between_them_ball - their_angle) < (30)){
				they_have_ball = true;
			}
		}


	}

	private void clearShot(){

		if(we_have_ball){

			//Positions
			Point our_position = all_moving_objects.getOurPosition();


			//Angles
			double our_angle = all_moving_objects.getOurAngle();
			double angle_with_top_post = Math.asin((all_static_objects.getTheir_top_goal_post().x 
					- our_position.x)/(our_position.distance(all_static_objects.getTheir_top_goal_post())));
			double angle_with_bottom_post = Math.asin((all_static_objects.getTheir_bottom_goal_post().x 
					- our_position.x)/(our_position.distance(all_static_objects.getTheir_bottom_goal_post())));

			//fix for normal angles into bearings.... :D
			if(angle_with_top_post < 0){
				angle_with_bottom_post = angle_with_bottom_post + 360;
				angle_with_top_post = angle_with_top_post + 360;
			}

			//Set clear shot boolean
			if(worldState.getShootingDirection() == 1){
				if(our_angle > angle_with_top_post && our_angle < angle_with_bottom_post){
					this.clear_shot = true;
				}
			}
			else{
				if(our_angle < angle_with_top_post && our_angle > angle_with_bottom_post){
					this.clear_shot = true;
				}
			}				
		}
	}
	
	private void ballTooCloseToWall() {
		
		this.ball_is_too_close_to_wall = this.obstacles.contains(all_static_objects.convertToNode(this.all_moving_objects.getBallPosition()));
	}
	
	public boolean getClearShot(){
		return clear_shot;
	}
	public Point handlingBallTooCloseWall(Point p) {
		//boundary handling...
		Point position = p;
		// 3 is the boundary variable	
		if (position.x < 3) {
			position.x = 3;
		}
		if (position.x > 47) {
			position.x = 47;
		}
		if (position.y < 3) {
			position.y = 3;
		}
		if (position.y > 22) {
			position.y = 22;
		}
	
		return position;
	}
	
	public int getAction(){
		return this.action;
	}
	
}