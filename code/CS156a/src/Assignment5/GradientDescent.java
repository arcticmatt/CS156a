package Assignment5;

import org.math.plot.utils.Array;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by mattlim on 11/1/14.
 */
public class GradientDescent {
    public double learningRate;
    public ArrayList<Double> weights;
    public int iterations = 0;

    public static void main(String[] args) {
        ArrayList<Double> initialWeights = new ArrayList<Double>();
        initialWeights.add(1.);
        initialWeights.add(1.);
        GradientDescent gradientDescent = new GradientDescent(.1, initialWeights);
        gradientDescent.run();
        System.out.println("Gradient descent iterations = " + gradientDescent.iterations);
        System.out.println("Final weight vector = (" + gradientDescent.weights.get(0) + ", " +
            gradientDescent.weights.get(1) + ")");
    }

    public GradientDescent(double rate, ArrayList<Double> initialWeights) {
        learningRate = rate;
        weights = initialWeights;
    }

    public void run() {
        while (computeError() > Math.pow(10, -14)) {
            updateWeights();
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

    public void updateWeights() {
        ArrayList<Double> grad = computeGradient();
        for (int i = 0; i < weights.size(); i++) {
            weights.set(i, weights.get(i) - learningRate * grad.get(i));
        }
    }
}
