package com.mattdahepic.mdecore.helpers;

import java.util.Random;

public class RandomHelper {
    /** The Random object */
    public static final Random RAND = new Random();
    /**
     * Returns true or false based upon a random chance.
     *
     * @param chance between 0 and 100
     * @return true or false depending on random
     */
    public static boolean randomChance (int chance) {
        return RAND.nextInt(100) < chance;
    }

    /**
     * Returns a random float in the range specified
     *
     * @param min value of returned float
     * @param max value of returned float
     * @return Random float between min and max
     */
    public static float randomFloatInRange (float min, float max) {
        return RAND.nextFloat()*(max-min)+min;
    }

    /**
     * Returns a random int in the range specified
     *
     * @param min value of returned int
     * @param max value of returned int
     * @return Random int between min and max
     */
    public static int randomIntInRange (int min, int max) {
        return RAND.nextInt((max-min)+1)+min;
    }
}
