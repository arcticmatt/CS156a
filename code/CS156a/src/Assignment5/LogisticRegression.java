package Assignment5;

import Assignment2.MyPoint;
import Assignment2.PLAGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by mattlim on 11/1/14.
 */
public class LogisticRegression {
    LogisticGraph logisticGraph;
    double[] weightVector;
    Random random = new Random();
    private double learningRate;
    private double weightVectorDiff;
    private int epochs = 0;

    public static void main(String[] args) {
        int NUM_TIMES = 1000;
        int NUM_OUT_OF_SAMPLE_POINTS = 10000;
        double sumErrorOut = 0;
        double sumEpochs = 0;
        for (int i = 0; i < NUM_TIMES; i++) {
            LogisticRegression logisticRegression = new LogisticRegression(100, .01);
            logisticRegression.run();
            ArrayList<MyPoint> outOfSamplePoints =
                    logisticRegression.logisticGraph.getRandomPointList(NUM_OUT_OF_SAMPLE_POINTS);
            double sumCrossEntropyError = 0;
            for (int j = 0; j < NUM_OUT_OF_SAMPLE_POINTS; j++) {
                MyPoint point = outOfSamplePoints.get(j);
                double wX = logisticRegression.weightVector[0] + logisticRegression.weightVector[1] * point.x
                        + logisticRegression.weightVector[2] * point.y;
                double crossEntropyError = Math.log(1 + Math.exp(-point.score * wX));
                sumCrossEntropyError += crossEntropyError;
            }
            sumErrorOut += sumCrossEntropyError / NUM_OUT_OF_SAMPLE_POINTS;
            sumEpochs += logisticRegression.epochs;
        }
        double avgErrorOut = sumErrorOut / NUM_TIMES;
        double avgEpochs = sumEpochs / NUM_TIMES;
        System.out.println("E_out = " + avgErrorOut);
        System.out.println("Avg epochs = " + avgEpochs);
    }

    public LogisticRegression(int numPoints, double rate) {
        logisticGraph = new LogisticGraph(numPoints);
        weightVector = new double[] {0, 0, 0};
        learningRate = rate;
    }

    /* Compute gradient for single point */
    public double[] computeGradient(MyPoint point) {
        double[] grad = new double[3];
        grad[0] = 1 * point.score;
        grad[1] = point.x * point.score;
        grad[2] = point.y * point.score;
        double wX = weightVector[0] + weightVector[1] * point.x + weightVector[2] * point.y;
        double denominator = 1 + Math.exp(point.score * wX);
        grad[0] = -grad[0] / denominator;
        grad[1] = -grad[1] / denominator;
        grad[2] = -grad[2] / denominator;
        return grad;
    }

    public void updateWeightVector(MyPoint point) {
        double[] grad = computeGradient(point);
        for (int i = 0; i < weightVector.length; i++) {
            weightVector[i] = weightVector[i] - learningRate * grad[i];
        }
    }

    public double getDifferenceMagnitude(double[] firstVector, double[] secondVector) {
        if (firstVector.length != secondVector.length)
            return -1;
        double diffMagnitude = 0;
        for (int i = 0; i < firstVector.length; i++) {
            diffMagnitude += Math.pow(firstVector[i] - secondVector[i], 2);
        }
        diffMagnitude = Math.sqrt(diffMagnitude);
        return diffMagnitude;
    }

    public void run() {
        double[] oldWeightVector;
        double[] newWeightVector;
        double diffMagnitude;

        do {
            oldWeightVector = weightVector.clone();
            logisticGraph.shufflePoints();
            for (int i = 0; i < logisticGraph.numPoints; i++) {
                updateWeightVector(logisticGraph.points.get(i));
            }
            newWeightVector = weightVector.clone();
            diffMagnitude = getDifferenceMagnitude(oldWeightVector, newWeightVector);
            epochs++;
        } while (diffMagnitude >= .01);
    }
}
