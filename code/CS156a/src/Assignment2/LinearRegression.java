package Assignment2;

import Jama.Matrix;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mattlim on 10/8/14.
 */
public class LinearRegression {
    public static final int NUM_POINTS = 100;

    public static void main(String[] args) {
        /* Questions 5-7 */
//        double sumFractionMisclassifiedIn = 0;
//        double sumFractionMisclassifiedOut = 0;
//        double sumNumTimesWeightVectorAdjusted = 0;
//        int numTrials = 1000;
//        double[][] weightVectors = new double[numTrials][3];
//        for (int i = 0; i < numTrials; i++) {
//            PLAGraph plaGraph = new PLAGraph(NUM_POINTS);
//            LinearRegression linearRegression = new LinearRegression();
//            PLARunner plaRunner = new PLARunner(plaGraph);
//            ArrayList<MyPoint> pointList = plaGraph.getRandomPointList(1000);
//
//            double[] weightVector = linearRegression.getWeightVector(plaGraph.points);
//            weightVectors[i] = weightVector;
//
//            int countMisclassifiedIn = plaGraph.countMisclassified(plaGraph.points, weightVector);
//            double fractionMisclassifiedIn = (double) countMisclassifiedIn / plaGraph.points.size();
//            System.out.println(fractionMisclassifiedIn);
//            sumFractionMisclassifiedIn += fractionMisclassifiedIn;
//
//            int countMisclassifiedOut = plaGraph.countMisclassified(pointList, weightVector);
//            double fractionMisclassifiedOut = (double) countMisclassifiedOut / pointList.size();
//            sumFractionMisclassifiedOut += fractionMisclassifiedOut;
//
//            plaRunner.weightVector = weightVector;
//            plaRunner.getWeightVector();
//            sumNumTimesWeightVectorAdjusted += plaRunner.numTimesWeightVectorAdjusted;
//
//            countMisclassifiedIn = plaGraph.countMisclassified(plaGraph.points, weightVector);
//            System.out.println("count misclassified = " + countMisclassifiedIn);
//        }
//        double avgFractionMisclassifiedIn = sumFractionMisclassifiedIn / numTrials;
//        double avgFractionMisclassifiedOut = sumFractionMisclassifiedOut / numTrials;
//        double avgNumTimesWeightVectorAdjusted = sumNumTimesWeightVectorAdjusted / numTrials;
//        System.out.println("Average fraction misclassified IN = " + avgFractionMisclassifiedIn);
//        System.out.println("Average fraction misclassified OUT = " + avgFractionMisclassifiedOut);
//        System.out.println("Average PLA iterations = " + avgNumTimesWeightVectorAdjusted);

        /* Question 8 */
        double sumFractionMisclassifiedIn = 0;
        int numTrials = 1000;
        for (int i = 0; i < numTrials; i++) {
            TargetFunction function = new TargetFunction(NUM_POINTS);
            LinearRegression linearRegression = new LinearRegression();

            double[] weightVector = linearRegression.getWeightVector(function.points);

            int countMisclassifiedIn = function.countMisclassified(function.points, weightVector);
            double fractionMisclassifiedIn = (double) countMisclassifiedIn / function.points.size();
            //System.out.println(fractionMisclassifiedIn);
            sumFractionMisclassifiedIn += fractionMisclassifiedIn;
        }
        double avgFractionMisclassifiedIn = sumFractionMisclassifiedIn / numTrials;
        System.out.println("Average fraction misclassified IN = " + avgFractionMisclassifiedIn);
    }

    public LinearRegression() {

    }

    public double[] getWeightVector(ArrayList<MyPoint> points) {
        Matrix xMatrix = getXMatrix(points);
        Matrix yMatrix = getYMatrix(points);
        Matrix xMatrixInverse = xMatrix.inverse();
        Matrix multiple = xMatrixInverse.times(yMatrix);
        double[][] weightVector2D = (xMatrixInverse.times(yMatrix)).getArray();
        double[] weightVector = { weightVector2D[0][0], weightVector2D[1][0], weightVector2D[2][0] };
        return weightVector;
    }

    public Matrix getXMatrix(ArrayList<MyPoint> points) {
        Matrix matrix = new Matrix(NUM_POINTS, 3);
        for (int i = 0; i < points.size(); i++) {
            MyPoint point = points.get(i);
            matrix.set(i, 0, 1);
            matrix.set(i, 1, point.x);
            matrix.set(i, 2, point.y);
        }
        return matrix;
    }

    public Matrix getYMatrix(ArrayList<MyPoint> points) {
        Matrix matrix = new Matrix(NUM_POINTS, 1);
        for (int i = 0; i < points.size(); i++) {
            MyPoint point = points.get(i);
            matrix.set(i, 0, point.score);
        }
        return matrix;
    }

    public void pseudoinverseTest() {
        Matrix matrix = new Matrix(3, 2);
        matrix.set(0, 0, 1);
        matrix.set(0, 1, 2);
        matrix.set(1, 0, 3);
        matrix.set(1, 1, 4);
        matrix.set(2, 0, 5);
        matrix.set(2, 1, 6);
        Matrix inverse = matrix.inverse();
        System.out.println("blah");
    }
}
