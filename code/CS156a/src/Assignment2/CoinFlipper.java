package Assignment2;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mattlim on 10/8/14.
 */
public class CoinFlipper {
    public ArrayList<Coin> coinList = new ArrayList<Coin>();
    public double v1 = 0;
    public double vRand = 0;
    public double vMin = 0;
    public Random random = new Random();

    public static void main(String[] args) {
        double v1Sum = 0;
        double vRandSum = 0;
        double vMinSum = 0;
        for (int i = 0; i < 100000; i++) {
            CoinFlipper coinFlipper = new CoinFlipper(1000);
            coinFlipper.runFlipSimulation();
            v1Sum += coinFlipper.v1;
            vRandSum += coinFlipper.vRand;
            vMinSum += coinFlipper.vMin;
        }
        double v1Avg = v1Sum / 100000;
        double vRandAvg = vRandSum / 100000;
        double vMinAvg = vMinSum / 100000;
        System.out.println("v1Avg = " + v1Avg + " vRandAvg = " + vRandAvg + " vMinAvg = " + vMinAvg);
    }

    public CoinFlipper(int numCoins) {
        for (int i = 0; i < numCoins; i++) {
            Coin coin = new Coin();
            coinList.add(coin);
        }
    }

    public void runFlipSimulation() {
        int numHeadsFirstCoin = 0;
        int numHeadsRandCoin = 0;
        int minNumHeads = 11;
        int numHeadsCounter = 0;
        int randomCoinNumber = random.nextInt(1000);
        //System.out.println("Random coin number = " + randomCoinNumber);
        for (int i = 0; i < coinList.size(); i++) {
            Coin currentCoin = coinList.get(i);
            numHeadsCounter = 0;
            for (int j = 0; j < 10; j++) {
                currentCoin.flipCoin();

                if (currentCoin.heads && i == 0)
                    numHeadsFirstCoin++;
                else if (currentCoin.heads && i == randomCoinNumber)
                    numHeadsRandCoin++;

                if (currentCoin.heads)
                    numHeadsCounter++;
            }
            if (numHeadsCounter < minNumHeads)
                minNumHeads = numHeadsCounter;
        }
        v1 = (double) numHeadsFirstCoin / 10;
        vRand = (double) numHeadsRandCoin / 10;
        vMin = (double) minNumHeads / 10;
    }
}
