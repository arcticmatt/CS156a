package Assignment5;

import org.math.plot.utils.Array;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by mattlim on 11/1/14.
 */
public class CoordinateDescent {
    public double learningRate;
    public ArrayList<Double> weights;
    public int iterations = 0;

    public static void main(String[] args) {
        ArrayList<Double> initialWeights = new ArrayList<Double>();
        initialWeights.add(1.);
        initialWeights.add(1.);
        CoordinateDescent coordinateDescent = new CoordinateDescent(.1, initialWeights);
        coordinateDescent.run();
        System.out.println("Final error = " + coordinateDescent.computeError());
        System.out.println("Final weight vector = (" + coordinateDescent.weights.get(0) + ", " +
                coordinateDescent.weights.get(1) + ")");
    }

    public CoordinateDescent(double rate, ArrayList<Double> initialWeights) {
        learningRate = rate;
        weights = initialWeights;
    }

    public void run() {
        for (int i = 0; i < 15; i++) {
            updateFirstWeight();
            updateSecondWeight();
            iterations++;
        }
    }

    /* Here, the gradient is
    < 2(ue^v - 2ve^(-u))(e^v + 2ve^(-u)), 2(ue^v - 2ve^(-u))(ue^v - 2e^(-u)) >
     */
    public ArrayList<Double> computeGradient() {
        ArrayList<Double> grad = new ArrayList<Double>();
        double u = weights.get(0);
        double v = weights.get(1);
        double partialU = 2 * (u * Math.exp(v) - 2 * v * Math.exp(-u)) * (Math.exp(v) + 2 * v * Math.exp(-u));
        double partialV = 2 * (u * Math.exp(v) - 2 * v * Math.exp(-u)) * (u * Math.exp(v) - 2 * Math.exp(-u));
        grad.add(partialU);
        grad.add(partialV);
        return grad;
    }

    public double computeError() {
        double u = weights.get(0);
        double v = weights.get(1);
        double error = Math.pow((u * Math.exp(v) - 2 * v * Math.exp(-u)), 2);
        return error;
    }

    public void updateFirstWeight() {
        ArrayList<Double> grad = computeGradient();
        weights.set(0, weights.get(0) - learningRate * grad.get(0));
    }

    public void updateSecondWeight() {
        ArrayList<Double> grad = computeGradient();
        weights.set(1, weights.get(1) - learningRate * grad.get(1));
    }
}
