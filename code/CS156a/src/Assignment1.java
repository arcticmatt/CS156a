import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by mattlim on 10/4/14.
 */
public class Assignment1 {
    static Point.Double firstPoint;
    static Point.Double secondPoint;
    static Double slope;
    static double[] weightVector;
    static ArrayList<MyPoint> myPoints = new ArrayList<MyPoint>();
    static ArrayList<MyPoint> largePointList = new ArrayList<MyPoint>();
    static int globalCounter = 0;
    static int globalCounterTotal = 0;
    static double globalProbabilityCounterTotal = 0;
    static Random random = new Random();
    static final int NUM_POINTS = 100;

    public static void main(String[] args) {
        System.out.println("Hello");
        Assignment1.initializeVariables();

        while (isMisclassified()) {
             runPLAIteration();
        }


        for (int i = 0; i < 1000; i++) {
            globalCounter = 0;
            System.out.println("Outside iteration #" + i);
            while (isMisclassified()) {
                runPLAIteration();
            }
            globalCounterTotal += globalCounter;
            double counter = 0;
            for (MyPoint point : largePointList) {
                if (signOfInnerProduct(point) != point.score) {
                    counter++;
                }
            }
            globalProbabilityCounterTotal += counter / (double) largePointList.size();
            Assignment1.initializeVariables();
        }
        System.out.println("Average iterations = " + globalCounterTotal / 1000);
        System.out.println("Average probability = " + globalProbabilityCounterTotal / (double) 1000);
    }

    public static void setScoreForPoint(MyPoint point) {
        double lineX = (point.y - firstPoint.y) / slope + firstPoint.x;
        if (lineX > point.x) {
            point.score = -1;
        } else {
            point.score = 1;
        }
    }

    public static void runPLAIteration() {
        Collections.shuffle(myPoints);
        for (MyPoint point : myPoints) {
            if (signOfInnerProduct(point) != point.score) {
                adjustWeightVector(point);
                break;
            }
        }
    }

    public static int signOfInnerProduct(MyPoint point) {
        double innerProduct = weightVector[0] * point.x + weightVector[1] * point.y + weightVector[2];
        if (innerProduct > 0)
            return 1;
        else
            return -1;
    }

    public static void adjustWeightVector(MyPoint point) {
        globalCounter++;
        weightVector[0] += point.score * point.x;
        weightVector[1] += point.score * point.y;
        weightVector[2] += point.score;
    }

    public static boolean isMisclassified() {
        for (MyPoint point : myPoints) {
            if (signOfInnerProduct(point) != point.score) {
                return true;
            }
        }
        return false;
    }

    public static void initializeVariables() {
        firstPoint = new Point.Double(2 * random.nextDouble() - 1, 2 * random.nextDouble() - 1);
        secondPoint = new Point.Double(2 * random.nextDouble() - 1, 2 * random.nextDouble() - 1);
        slope = (secondPoint.y - firstPoint.y) / (secondPoint.x - firstPoint.x);
        weightVector = new double[] {0, 0, 0};
        myPoints.clear();
        for (int i = 0; i < NUM_POINTS; i++) {
            MyPoint randomPoint = new MyPoint(2 * random.nextDouble() - 1, 2 * random.nextDouble() - 1);
            Assignment1.setScoreForPoint(randomPoint);
            myPoints.add(randomPoint);
        }
        largePointList.clear();
        for (int i = 0; i < 100000; i++) {
            MyPoint randomPoint = new MyPoint(2 * random.nextDouble() - 1, 2 * random.nextDouble() - 1);
            Assignment1.setScoreForPoint(randomPoint);
            largePointList.add(randomPoint);
        }
    }
}
