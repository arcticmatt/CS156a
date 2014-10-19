package Assignment2;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mattlim on 10/12/14.
 */
public class TargetFunction {
    public ArrayList<MyPoint> points = new ArrayList<MyPoint>();
    public Random random = new Random();
    public int numPoints;

    public TargetFunction(int numPoints) {
        this.numPoints = numPoints;
        this.points = getRandomPointList(numPoints);
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

    /* HARDCORE FUNCTION HERE */
    public void setScoreForPoint(MyPoint point) {
        if (Math.pow(point.x, 2) + Math.pow(point.y, 2) - .6 > 0) {
            if (Math.random() > .1)
                point.score = 1;
            else
                point.score = -1;
        } else {
            if (Math.random() > .1)
                point.score = -1;
            else
                point.score = 1;
        }
    }

    public int countMisclassified(ArrayList<MyPoint> points, double[] weightVector) {
        int count = 0;
        for (MyPoint point : points) {
            if (isMisclassified(point, weightVector))
                count++;
        }
        return count;
    }

    public int countMisclassified(ArrayList<double[]> points, ArrayList<MyPoint> pointsForScore, ArrayList<Double> weightVector) {
        int count = 0;
        for (int i = 0; i < points.size(); i++) {
            if (isMisclassified(points.get(i), pointsForScore.get(i), weightVector))
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

    public int signOfInnerProduct(double[] point, ArrayList<Double> weightVector) {
        double innerProduct = 0;
        for (int i = 0; i < point.length; i++) {
            innerProduct += point[i] * weightVector.get(i);
        }
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

    public boolean isMisclassified(double[] point, MyPoint pointForScore, ArrayList<Double> weightVector) {
        if (signOfInnerProduct(point, weightVector) != pointForScore.score) {
            return true;
        }
        return false;
    }


    public boolean isMisclassified(ArrayList<MyPoint> points, double[] weightVector) {
        for (MyPoint point : points) {
            if (signOfInnerProduct(point, weightVector) != point.score) {
                return true;
            }
        }
        return false;
    }
}
