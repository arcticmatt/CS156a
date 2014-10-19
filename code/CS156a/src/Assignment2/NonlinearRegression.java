package Assignment2;

import Jama.Matrix;

import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by mattlim on 10/12/14.
 */
public class NonlinearRegression {
    public static final int NUM_POINTS = 1000;

    public static void main(String[] args) {
        /* Question 9 */
        int numTrials = 1000;
        double sumFractionMisclassifiedOut = 0;
        ArrayList<ArrayList<Double>> weightVectors = new ArrayList<ArrayList<Double>>();
        for (int i = 0; i < numTrials; i++) {
            TargetFunction function = new TargetFunction(NUM_POINTS);
            NonlinearRegression nonlinearRegression = new NonlinearRegression();
            ArrayList<double[]> pointsList = new ArrayList<double[]>();
            ArrayList<MyPoint> outOfSamplePoints = function.getRandomPointList(1000);
            ArrayList<double[]> outOfSamplePointsTransformed = new ArrayList<double[]>();
            for (MyPoint point : function.points) {
                double[] newPoint = {1, point.x, point.y, point.x * point.y, Math.pow(point.x, 2), Math.pow(point.y, 2)};
                pointsList.add(newPoint);
            }
            for (MyPoint point : outOfSamplePoints) {
                double[] newPoint = {1, point.x, point.y, point.x * point.y, Math.pow(point.x, 2), Math.pow(point.y, 2)};
                outOfSamplePointsTransformed.add(newPoint);
            }

            ArrayList<Double> weightVector = nonlinearRegression.getWeightVector(pointsList, function.points);
            weightVectors.add(weightVector);
            double numMisclassifiedOut = function.countMisclassified(outOfSamplePointsTransformed, outOfSamplePoints, weightVector);
            sumFractionMisclassifiedOut += numMisclassifiedOut / outOfSamplePoints.size();
        }

        ArrayList<Double> avgWeightVector = new ArrayList<Double>();
        for (int i = 0; i < weightVectors.get(0).size(); i++) {
            double sum = 0;
            double average = 0;
            for (int j = 0; j < weightVectors.size(); j++) {
               sum += weightVectors.get(j).get(i);
            }
            average = sum / weightVectors.size();
            System.out.println("Sum = " + sum + " Average = " + average);
            avgWeightVector.add(average);
        }

        System.out.println("Average");
        DecimalFormat df = new DecimalFormat("#.##");
        for (int i = 0; i < avgWeightVector.size(); i++) {
            System.out.print(df.format(avgWeightVector.get(i)) + " + ");
        }
        System.out.println("E out = " + sumFractionMisclassifiedOut / numTrials);



        double firstSum = 0;
        double secondSum = 0;
        double thirdSum = 0;
        double fourthSum = 0;
        double fifthSum = 0;
        for (int k = 0; k < 100; k++) {
            TargetFunction function = new TargetFunction(NUM_POINTS);
            ArrayList<MyPoint> testPoints = function.getRandomPointList(1000);
            ArrayList<double[]> testPointsTransformed = new ArrayList<double[]>();
            for (MyPoint point : testPoints) {
                double[] newPoint = {1, point.x, point.y, point.x * point.y, Math.pow(point.x, 2), Math.pow(point.y, 2)};
                testPointsTransformed.add(newPoint);
            }

            Double[] hypothesisWeightVector = {-1.0, .000864, -.000769, -.00515, 1.55, 1.56};
            ArrayList<Double> hypothesisWeightVectorList = new ArrayList<Double>(Arrays.asList(hypothesisWeightVector));

            Double[] firstWeightVector = {-1.0, -0.05, 0.08, 0.13, 1.5, 1.5};
            ArrayList<Double> firstWeightVectorList = new ArrayList<Double>(Arrays.asList(firstWeightVector));

            Double[] secondWeightVector = {-1.0, -0.05, 0.08, 0.13, 1.5, 15.0};
            ArrayList<Double> secondWeightVectorList = new ArrayList<Double>(Arrays.asList(secondWeightVector));

            Double[] thirdWeightVector = {-1.0, -0.05, 0.08, 0.13, 15., 1.5};
            ArrayList<Double> thirdWeightVectorList = new ArrayList<Double>(Arrays.asList(thirdWeightVector));

            Double[] fourthWeightVector = {-1.0, -1.5, 0.08, 0.13, 0.05, 0.05};
            ArrayList<Double> fourthWeightVectorList = new ArrayList<Double>(Arrays.asList(fourthWeightVector));

            Double[] fifthWeightVector = {-1.0, -0.05, 0.08, 1.5, .15, .15};
            ArrayList<Double> fifthWeightVectorList = new ArrayList<Double>(Arrays.asList(fifthWeightVector));

            double firstCounter = 0;
            double secondCounter = 0;
            double thirdCounter = 0;
            double fourthCounter = 0;
            double fifthCounter = 0;
            for (int i = 0; i < testPoints.size(); i++) {
                Boolean hypothesisMisclassified = function.isMisclassified(testPointsTransformed.get(i), testPoints.get(i),
                        hypothesisWeightVectorList);
                Boolean firstMisclassified = function.isMisclassified(testPointsTransformed.get(i), testPoints.get(i),
                        firstWeightVectorList);
                Boolean secondMisclassified = function.isMisclassified(testPointsTransformed.get(i), testPoints.get(i),
                        secondWeightVectorList);
                Boolean thirdMisclassified = function.isMisclassified(testPointsTransformed.get(i), testPoints.get(i),
                        thirdWeightVectorList);
                Boolean fourthMisclassified = function.isMisclassified(testPointsTransformed.get(i), testPoints.get(i),
                        fourthWeightVectorList);
                Boolean fifthMisclassified = function.isMisclassified(testPointsTransformed.get(i), testPoints.get(i),
                        fifthWeightVectorList);
                if (hypothesisMisclassified.compareTo(firstMisclassified) != 0)
                    firstCounter++;
                if (hypothesisMisclassified.compareTo(secondMisclassified) != 0)
                    secondCounter++;
                if (hypothesisMisclassified.compareTo(thirdMisclassified) != 0)
                    thirdCounter++;
                if (hypothesisMisclassified.compareTo(fourthMisclassified) != 0)
                    fourthCounter++;
                if (hypothesisMisclassified.compareTo(fifthMisclassified) != 0)
                    fifthCounter++;
            }
            firstSum += firstCounter;
            secondSum += secondCounter;
            thirdSum += thirdCounter;
            fourthSum += fourthCounter;
            fifthSum += fifthCounter;
        }
        double firstAvg = firstSum / 100;
        double secondAvg = secondSum / 100;
        double thirdAvg = thirdSum / 100;
        double fourthAvg = fourthSum / 100;
        double fifthAvg = fifthSum / 100;
        System.out.println("First = " + firstAvg + " Second = " + secondAvg + " Third = " + thirdAvg + " Fourth = " +
        fourthAvg + " Fifth = " + fifthAvg);
    }

    public NonlinearRegression() {

    }

    public ArrayList<Double> getWeightVector(ArrayList<double[]> points, ArrayList<MyPoint> myPoints) {
        Matrix xMatrix = getXMatrix(points);
        Matrix yMatrix = getYMatrix(myPoints);
        Matrix xMatrixInverse = xMatrix.inverse();
        double[][] weightVector2D = (xMatrixInverse.times(yMatrix)).getArray();
        ArrayList<Double> weightVector = new ArrayList<Double>();
        DecimalFormat df = new DecimalFormat("#.##");
        for (int i = 0; i < weightVector2D.length; i++) {
            //System.out.print(df.format(weightVector2D[i][0]) + " + ");
            weightVector.add(weightVector2D[i][0]);
        }
        return weightVector;
    }

    public Matrix getXMatrix(ArrayList<double[]> points) {
        if (points.size() < 1)
            return null;
        int pointDimn = points.get(0).length;
        Matrix matrix = new Matrix(NUM_POINTS, pointDimn);
        for (int i = 0; i < points.size(); i++) {
            double[] point = points.get(i);
            for (int j = 0; j < pointDimn; j++) {
                matrix.set(i, j, point[j]);
            }
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
}
