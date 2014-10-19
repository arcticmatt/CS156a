package Assignment2;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mattlim on 10/8/14.
 */
public class PLAGraph {
    public Point.Double firstPoint;
    public Point.Double secondPoint;
    public Double slope;
    public double[] weightVector;
    public ArrayList<MyPoint> points = new ArrayList<MyPoint>();
    public Random random = new Random();
    public int numPoints;

    public PLAGraph(int numPoints) {
        this.numPoints = numPoints;
        firstPoint = new Point.Double(2 * random.nextDouble() - 1, 2 * random.nextDouble() - 1);
        secondPoint = new Point.Double(2 * random.nextDouble() - 1, 2 * random.nextDouble() - 1);
        slope = (secondPoint.y - firstPoint.y) / (secondPoint.x - firstPoint.x);
        weightVector = new double[] {0, 0, 0};
        points = getRandomPointList(numPoints);
    }

    public void setScoreForPoint(MyPoint point) {
        double lineX = (point.y - firstPoint.y) / slope + firstPoint.x;
        if (lineX > point.x) {
            point.score = -1;
        } else {
            point.score = 1;
        }
    }

    public ArrayList<MyPoint> getRandomPointList(int numPoints) {
        ArrayList<MyPoint> pointList = new ArrayList<MyPoint>();
        for (int i = 0; i < numPoints; i++) {
            MyPoint randomPoint = new MyPoint(2 * random.nextDouble() - 1, 2 * random.nextDouble() - 1);
            setScoreForPoint(randomPoint);
            pointList.add(randomPoint);
        }
        return pointList;
    }


    public int countMisclassified(ArrayList<MyPoint> pointList, double[] weightVector) {
        int count = 0;
        for (MyPoint point : pointList) {
            if (isMisclassified(point, weightVector))
                count++;
        }
        return count;
    }

    public int signOfInnerProduct(MyPoint point, double[] weightVector) {
        double innerProduct = weightVector[0] + weightVector[1] * point.x + weightVector[2] * point.y;
        //double innerProduct = weightVector[0] * point.x + weightVector[1] * point.y + weightVector[2];
        if (innerProduct > 0)
            return 1;
        else
            return -1;
    }

    public boolean isMisclassified(MyPoint point, double[] weightVector) {
        if (signOfInnerProduct(point, weightVector) != point.score) {
            return true;
        }
        return false;
    }

    public boolean isMisclassified(ArrayList<MyPoint> pointList, double[] weightVector) {
        for (MyPoint point : pointList) {
            if (signOfInnerProduct(point, weightVector) != point.score) {
                return true;
            }
        }
        return false;
    }
}
