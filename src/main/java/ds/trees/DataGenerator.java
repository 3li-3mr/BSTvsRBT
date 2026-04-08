package ds.trees;

import java.util.Random;

public class DataGenerator {

    private static final long SEED = 42;
    private static final int SIZE = 100000;

    public static int[] generateRandom() {
        Random rand = new Random(SEED);
        int[] arr = new int[SIZE];
        for (int i = 0; i < SIZE; i++) {
            arr[i] = rand.nextInt(1000000);
        }
        return arr;
    }

    public static int[] generateNearlySorted(int swaps) {
        Random rand = new Random(SEED);
        int[] arr = new int[SIZE];

        for (int i = 0; i < SIZE; i++) {
            arr[i] = i;
        }

        for (int i = 0; i < swaps; i++) {
            int index1 = rand.nextInt(SIZE);
            int index2 = rand.nextInt(SIZE);

            int temp = arr[index1];
            arr[index1] = arr[index2];
            arr[index2] = temp;
        }
        return arr;
    }

    public static int[] generate1PercentMisplaced() {
        return generateNearlySorted(1000);
    }

    public static int[] generate5PercentMisplaced() {
        return generateNearlySorted(5000);
    }

    public static int[] generate10PercentMisplaced() {
        return generateNearlySorted(10000);
    }
}