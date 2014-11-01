package Assignment4;

import Assignment2.MyPoint;
import Jama.Matrix;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mattlim on 10/25/14.
 */
public class AverageHypothesis {
    public static final int NUM_TIMES = 1000000;
    public static final double AVERAGE_HYPOTHESIS = 1.432;
    public static Random random = new Random();

    public static void main(String[] args) {
        AverageHypothesis averageHypothesis = new AverageHypothesis();
        double slopeSum = 0;
        double varianceSum = 0;
        for (int i = 0; i < NUM_TIMES; i++) {
            double slope = averageHypothesis.getSlope();
            double variance = Math.pow(slope - AVERAGE_HYPOTHESIS, 2);
            slopeSum += slope;
            varianceSum += variance;
        }
        double averageSlope = slopeSum / NUM_TIMES;
        double averageVariance = varianceSum / NUM_TIMES;
        System.out.println("Average slope = " + averageSlope);
        System.out.println("E_d variance = " + averageVariance);
        averageVariance = averageHypothesis.getVariance(averageVariance);
        System.out.println("E_x variance = " + averageVariance);
        double averageBias = averageHypothesis.getBias();
        System.out.println("Average bias = " + averageBias);

        double sumWeightVector1 = 0;
        double sumWeightVector2 = 0;
        for (int i = 0; i < NUM_TIMES; i++) {
            double x1 = 2 * random.nextDouble() - 1;
            double x2 = 2 * random.nextDouble() - 1;
            double y1 = Math.sin(Math.PI * x1);
            double y2 = Math.sin(Math.PI * x2);
            Point.Double firstPoint = new Point.Double(x1, y1);
            Point.Double secondPoint = new Point.Double(x2, y2);
            double[] weightVector = averageHypothesis.getWeightVector(firstPoint, secondPoint);
            sumWeightVector1 += weightVector[0];
            sumWeightVector2 += weightVector[1];
        }
        double averageWeightVector1 = sumWeightVector1 / NUM_TIMES;
        double averageWeightVector2 = sumWeightVector2 / NUM_TIMES;
        System.out.println("Average weight vector = " + averageWeightVector1 + "x^2" + " + " + averageWeightVector2);

        double averageError = averageHypothesis.getAverageError();
        System.out.println("Average error = " + averageError);
    }

    public double getAverageError() {
        double sumError = 0;
        for (int i = 0; i < NUM_TIMES; i++) {
            double x1 = 2 * random.nextDouble() - 1;
            double x2 = 2 * random.nextDouble() - 1;
            double y1 = Math.sin(Math.PI * x1);
            double y2 = Math.sin(Math.PI * x2);
            double currY1 = .77 * x1 + .0043;
            double currY2 = .77 * x2 + .0043;
            double meanSquaredError1 = Math.pow(currY1 - y1, 2);
            double meanSquaredError2 = Math.pow(currY2 - y2, 2);
            double error = (meanSquaredError1 + meanSquaredError2) / 2;
            sumError += error;
        }
        return sumError / NUM_TIMES;
    }

    public double getSlope() {
        Random random = new Random();
        double x1 = 2 * random.nextDouble() - 1;
        double x2 = 2 * random.nextDouble() - 1;
        double y1 = Math.sin(Math.PI * x1);
        double y2 = Math.sin(Math.PI * x2);

        double minError = Integer.MAX_VALUE;
        double bestSlope = 0;
        double currSlope = -5.0;

            while (currSlope < 5.0) {
            double currY1 = currSlope * x1;
            double currY2 = currSlope * x2;
            double meanSquaredError1 = Math.pow(currY1 - y1, 2);
            double meanSquaredError2 = Math.pow(currY2 - y2, 2);
            double error = (meanSquaredError1 + meanSquaredError2) / 2;
            if (error < minError) {
                bestSlope = currSlope;
                minError = error;
            }
            currSlope += .01;
        }
        return bestSlope;
    }


    public double getBias() {
        double count = 0;
        double sumBias = 0;
        for (double i = -1; i < 1; i += .0005) {
            double hypothesisVal = 1.432 * i;
            double actualVal = Math.sin(Math.PI * i);
            double bias = Math.pow(hypothesisVal - actualVal, 2);
            sumBias += bias;
            count++;
        }
        double averageBias = sumBias / count;
        return averageBias;
    }

    public double getVariance(double constant) {
        double count = 0;
        double sumVariance = 0;
        for (double i = -1; i < 1; i += .0005) {
            double variance = constant * Math.pow(i, 2);
            sumVariance += variance;
            count++;
        }
        double averageVariance = sumVariance / count;
        return averageVariance;
    }

    // For question 4
    public double getWeightVectorValue(Point.Double point1, Point.Double point2) {
        Matrix xMatrix = getXMatrix(point1.x, point2.x);
        Matrix yMatrix = getYMatrix(point1.y, point2.y);
        Matrix xMatrixInverse = xMatrix.inverse();
        double[][] weightVector2D = (xMatrixInverse.times(yMatrix)).getArray();
        double weightVector = weightVector2D[0][0];
        return weightVector;
    }

    // For question 7
    public double[] getWeightVector(Point.Double point1, Point.Double point2) {
        Matrix xMatrix = getXMatrix(point1.x, point2.x);
        Matrix yMatrix = getYMatrix(point1.y, point2.y);
        Matrix xMatrixInverse = xMatrix.inverse();
        double[][] weightVector2D = (xMatrixInverse.times(yMatrix)).getArray();
        double[] weightVector = { weightVector2D[0][0], weightVector2D[1][0] };
        return weightVector;
    }

    // For question 4
//    public Matrix getXMatrix(double x1, double x2) {
//        Matrix matrix = new Matrix(2, 1);
//        matrix.set(0, 0, x1);
//        matrix.set(1, 0, x2);
//        return matrix;
//    }

    // For question 7
    public Matrix getXMatrix(double x1, double x2) {
        Matrix matrix = new Matrix(2, 2);
        matrix.set(0, 0, Math.pow(x1, 2));
        matrix.set(0, 1, 1);
        matrix.set(1, 0, Math.pow(x2, 2));
        matrix.set(1, 1, 1);
        return matrix;
    }

    // For question 4 and 7
    public Matrix getYMatrix(double y1, double y2) {
        Matrix matrix = new Matrix(2, 1);
        matrix.set(0, 0, y1);
        matrix.set(1, 0, y2);
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
