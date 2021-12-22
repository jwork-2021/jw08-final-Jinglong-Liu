package app.util;

import java.util.Random;

public class ProbabilityUtil {
    /**
     *
     * @param probability:0-100
     * @return true or false
     */
    public static boolean get(int probability){
        Random random = new Random();
        int r = random.nextInt(100);
        return r > probability;
    }

    /**
     *
     * @return random number range [0,100)
     */
    public static int probability(){
        return new Random().nextInt(100);
    }
}
