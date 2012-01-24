interface VisualMethods{

//Priority 1 - Vital

	ObjectPosition getBallPosition(); //gets the position of the ball
	ObjectPosition getOurPosition(); //gets the position of our robot (return center?)
	ObjectPosition getOpponentPosition(); //gets the position of our oponents robot
	ObjectPosition getOurOrientation(); //gets our orientation
	ObjectPosition getOpponentOrientation(); //gets our opponents orientation
	Vector2 getDistanceToOpponent(); //gets the distance and direction to our opponent


//Priority 2 - Needed to have any sort of success

	Vector2 getDistanceToSide(); //finds distance to specified side
	Vector2 getBallVelocity(); //gets the velocity of the ball
	Vector2 getOurVelocity(); //gets our velocity
	Vector2 getOpponentVelocity(); //gets our opponents velocity



//Priority 3 - Could be useful


}
