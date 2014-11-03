package Assignment5;

import Assignment2.MyPoint;
import Assignment2.PLAGraph;

/**
 * Created by mattlim on 10/8/14.
 */
public class LogisticGraph extends PLAGraph {

    public LogisticGraph(int numPoints) {
        super(numPoints);
    }

    public MyPoint getRandomPoint() {
        int randInt = random.nextInt(numPoints);
        return points.get(randInt);
    }

}
