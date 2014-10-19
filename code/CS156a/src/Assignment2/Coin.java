package Assignment2;

import java.util.Random;

/**
 * Created by mattlim on 10/8/14.
 */
public class Coin {
    public boolean heads;
    Random random;
    public Coin() {
        random = new Random();
        heads = true;
    }

    public void flipCoin() {
        heads = random.nextBoolean();
    }
}
