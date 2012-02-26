/**
 * 
 */
package uk.ac.ed.inf.sdp2012.group7.strategy.planning;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;

import uk.ac.ed.inf.sdp2012.group7.strategy.Strategy;
import uk.ac.ed.inf.sdp2012.group7.strategy.astar.AStar;
import uk.ac.ed.inf.sdp2012.group7.strategy.astar.AreaMap;
import uk.ac.ed.inf.sdp2012.group7.strategy.astar.Node;
import uk.ac.ed.inf.sdp2012.group7.strategy.astar.Path;

/**
 * 
 */

/**
 * @author twig
 *
 */
public class PlanningBuffer extends Observable implements Observer {

	private Plan held_plan;
	
	public static final Logger logger = Logger.getLogger(Plan.class);
	private long time_stamp = System.currentTimeMillis();

	//I would like to be able to read the plans created
	//offline; but I don't need EVERY plan, so I will
	//use a counter...
	private int counter = 0;
	
	public PlanningBuffer(Observer myWatcher){
		this.addObserver(myWatcher);
	}


	@Override
	public void update(Observable o, Object arg) {
		synchronized(this){
			logger.debug("Planning Buffer Updated");
			this.held_plan = (Plan)arg;
			setChanged();
			notifyObservers(held_plan);
			if(counter > 1){
				savePlan();
				counter = 0;
			}
			Strategy.logger.info("Current plan count: " + Integer.toString(counter));
			counter++;
		}
	}
	
	public Plan getPlan(){
		return held_plan;
	}
	
	public void savePlan(){
		AreaMap map = held_plan.getAStar().getAreaMap();
		if(map.getNodes().length <= 0) return;
		
		String[][] ascii = new String[map.getMapHeight()][map.getMapWidth()];
		for(int y = 0; y < ascii.length; y++){
			for(int x = 0; x < ascii[0].length; x++){
				ascii[y][x] = ".";
			}
		}
				
		for(int y = 0; y < ascii.length; y++){
			for(int x = 0; x < ascii[y].length; x++){
				Node n = map.getNode(x,y);
				if(n.isGoal()) ascii[y][x] = "T";
				if(n.isObstical()) ascii[y][x] = "X";
				if(n.isObstical()) ascii[y][x] = "X";
				if(n.isStart()) ascii[y][x] = "S";
				if(n.getCost() == 1) ascii[y][x] = "O";
				if(n.isVisited()) ascii[y][x] = " ";
			}
		}
		for(int y = 0; y < ascii.length; y++){
			String line = "";
			for(int x = 0; x < ascii[y].length; x++){
				line += ascii[y][x] + " ";
			}
			System.out.println(line);
		}
		//Path path = held_plan.getNodePath();
		//ArrayList<Node> waypoints = path.getWayPoints();
		//for(Node n : waypoints){
			//Draw node onto map
		//}
	}

}
