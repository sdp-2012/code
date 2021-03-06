package uk.ac.ed.inf.sdp2012.group7.strategy.oldastar;

import java.awt.Point;
import java.util.ArrayList;

public class OldAStarRunTest {

		public static void main(String[] args) {
			
			// set start and end points of the path
			Point start = new Point(32,14);
			Point end = new Point(27,14);
			
			// set obstacles
			Point oppositionTopLeft = new Point(28,12);
			Point oppositionBottomRight = new Point(31,15);
			ArrayList<Point> obstacles = new ArrayList<Point>();
			for (int i=oppositionTopLeft.y; i<=oppositionBottomRight.y; i++) {
				for (int j=oppositionTopLeft.x; j<=oppositionBottomRight.x; j++) {
					obstacles.add(new Point(j,i));
				}
			}
			
			// run the algorithm
			OldAStarRun run = new OldAStarRun(25,50,end, start, obstacles);
			OldPath path = run.getPath();
			path.printWaypoints();
		}
}
