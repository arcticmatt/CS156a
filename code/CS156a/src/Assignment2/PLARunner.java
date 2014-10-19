package Assignment2;

import java.util.Collections;

/**
 * Created by mattlim on 10/9/14.
 */
public class PLARunner {
    public PLAGraph plaGraph;
    public double[] weightVector = {0, 0, 0};
    public int numTimesWeightVectorAdjusted = 0;

    public static void main(String[] args) {
        double sumNumTimesWeightVectorAdjusted = 0;
        for (int i = 0; i < 1000; i++) {
            PLARunner plaRunner = new PLARunner(10);
            plaRunner.getWeightVector();
            sumNumTimesWeightVectorAdjusted += plaRunner.numTimesWeightVectorAdjusted;
        }
        double avgNumTimesWeightVectorAdjusted = sumNumTimesWeightVectorAdjusted / 1000;
        System.out.println("Average iterations = " + avgNumTimesWeightVectorAdjusted);
    }

    public PLARunner(int numPoints) {
        plaGraph = new PLAGraph(numPoints);
    }

    public PLARunner(PLAGraph plaGraph) {
        this.plaGraph = plaGraph;
    }

    public double[] getWeightVector() {
        while (plaGraph.isMisclassified(plaGraph.points, weightVector)) {
            runPLAIteration();
        }
        return weightVector;
    }

    public void runPLAIteration() {
        Collections.shuffle(plaGraph.points);
        for (MyPoint point : plaGraph.points) {
            if (plaGraph.signOfInnerProduct(point, weightVector) != point.score) {
                adjustWeightVector(point);
                break;
            }
        }
    }

    public void adjustWeightVector(MyPoint point) {
        numTimesWeightVectorAdjusted++;
        weightVector[0] += point.score;
        weightVector[1] += point.score * point.x;
        weightVector[2] += point.score * point.y;
    }
}
