package ds.trees;

import java.util.Arrays;
import java.util.Random;

public class Main {

    private static final int ITERATIONS = 5;

    public static void main(String[] args) {
        if (BST.VALIDATE || RBT.VALIDATE) {
            System.err.println("WARNING: VALIDATE is true! Change it to false in BST and RBT before recording final numbers.");
        }

        System.out.println("--- Starting JVM Warmup ---");
        warmup();
        System.out.println("--- Warmup Complete ---\n");

        int[] randomArray = DataGenerator.generateRandom();
        int[] onePercent = DataGenerator.generate1PercentMisplaced();
        int[] fivePercent = DataGenerator.generate5PercentMisplaced();
        int[] tenPercent = DataGenerator.generate10PercentMisplaced();

        runBenchmark("Fully Random", randomArray);
        runBenchmark("1% Misplaced", onePercent);
        runBenchmark("5% Misplaced", fivePercent);
        runBenchmark("10% Misplaced", tenPercent);
    }

    private static void runBenchmark(String arrayType, int[] data) {
        System.out.println("=========================================");
        System.out.println(" BENCHMARK: " + arrayType);
        System.out.println("=========================================");

        double[] bstInsertTimes = new double[ITERATIONS];
        double[] bstSearchTimes = new double[ITERATIONS];
        double[] bstDeleteTimes = new double[ITERATIONS];
        double[] bstSortTimes = new double[ITERATIONS];

        double[] rbtInsertTimes = new double[ITERATIONS];
        double[] rbtSearchTimes = new double[ITERATIONS];
        double[] rbtDeleteTimes = new double[ITERATIONS];
        double[] rbtSortTimes = new double[ITERATIONS];

        double[] quickSortTimes = new double[ITERATIONS];

        int[] searchQueries = new int[100000];
        for (int i = 0; i < 50000; i++) {
            searchQueries[i] = data[i];
            searchQueries[i + 50000] = data[i] + 2000000;
        }

        int[] deletePool = Arrays.copyOf(data, data.length);
        Random rand = new Random(42);
        for (int i = 0; i < 20000; i++) {
            int swapIdx = i + rand.nextInt(deletePool.length - i);
            int temp = deletePool[i];
            deletePool[i] = deletePool[swapIdx];
            deletePool[swapIdx] = temp;
        }
        int[] deleteQueries = Arrays.copyOfRange(deletePool, 0, 20000);

        for (int i = 0; i < ITERATIONS; i++) {
            BST bst = new BST();

            long start = System.nanoTime();
            for (int val : data) bst.insert(val);
            bstInsertTimes[i] = toMillis(System.nanoTime() - start);

            start = System.nanoTime();
            bst.inOrder();
            bstSortTimes[i] = bstInsertTimes[i] + toMillis(System.nanoTime() - start);

            start = System.nanoTime();
            for (int val : searchQueries) bst.contains(val);
            bstSearchTimes[i] = toMillis(System.nanoTime() - start);

            start = System.nanoTime();
            for (int val : deleteQueries) bst.delete(val);
            bstDeleteTimes[i] = toMillis(System.nanoTime() - start);

            RBT rbt = new RBT();

            start = System.nanoTime();
            for (int val : data) rbt.insert(val);
            rbtInsertTimes[i] = toMillis(System.nanoTime() - start);

            start = System.nanoTime();
            rbt.inOrder();
            rbtSortTimes[i] = rbtInsertTimes[i] + toMillis(System.nanoTime() - start);

            start = System.nanoTime();
            for (int val : searchQueries) rbt.contains(val);
            rbtSearchTimes[i] = toMillis(System.nanoTime() - start);

            start = System.nanoTime();
            for (int val : deleteQueries) rbt.delete(val);
            rbtDeleteTimes[i] = toMillis(System.nanoTime() - start);
            if(i == 0) {
                System.out.println("  [ Tree Heights After Insertion ]");
                System.out.println("  BST Height: " + bst.height());
                System.out.println("  RBT Height: " + rbt.height() + "\n");
            }
            int[] array = Arrays.copyOf(data, data.length);
            start = System.nanoTime();
            QuickSort.sort(array);
            quickSortTimes[i] = toMillis(System.nanoTime() - start);
        }

        System.out.println("  [ BST Operations ]");
        Stats.print("Insertion", bstInsertTimes);
        Stats.print("Search   ", bstSearchTimes);
        Stats.print("Deletion ", bstDeleteTimes);
        Stats.print("Tree Sort", bstSortTimes);

        System.out.println("\n  [ RBT Operations ]");
        Stats.print("Insertion", rbtInsertTimes);
        Stats.print("Search   ", rbtSearchTimes);
        Stats.print("Deletion ", rbtDeleteTimes);
        Stats.print("Tree Sort", rbtSortTimes);

        System.out.println("\n  [ Standard Sorting ]");
        Stats.print("MergeSort", quickSortTimes);

        System.out.println("\n  [ RBT Speed-Up (BST Mean / RBT Mean) ]");
        System.out.printf("  Insertion Speed-Up: %6.2fx\n", Stats.getMean(bstInsertTimes) / Stats.getMean(rbtInsertTimes));
        System.out.printf("  Search Speed-Up:    %6.2fx\n", Stats.getMean(bstSearchTimes) / Stats.getMean(rbtSearchTimes));
        System.out.printf("  Deletion Speed-Up:  %6.2fx\n", Stats.getMean(bstDeleteTimes) / Stats.getMean(rbtDeleteTimes));
        System.out.println();
    }

    private static double toMillis(long nanos) {
        return nanos / 1_000_000.0;
    }

    private static void warmup() {
        int[] dummyData = DataGenerator.generateNearlySorted(100);
        BST bst = new BST();
        RBT rbt = new RBT();
        for (int i = 0; i < 5000; i++) {
            bst.insert(dummyData[i]);
            rbt.insert(dummyData[i]);
            bst.contains(dummyData[i]);
            rbt.contains(dummyData[i]);
            bst.delete(dummyData[i]);
            rbt.delete(dummyData[i]);
        }
    }

    private static class Stats {
        static double getMean(double[] times) {
            double sum = 0;
            for (double t : times) sum += t;
            return sum / times.length;
        }

        static void print(String label, double[] times) {
            Arrays.sort(times);
            double median = times[times.length / 2];
            double mean = getMean(times);

            double varianceSum = 0;
            for (double t : times) varianceSum += Math.pow(t - mean, 2);
            double stdDev = Math.sqrt(varianceSum / times.length);

            System.out.printf("  %-10s -> Mean: %7.2f ms | Median: %7.2f ms | StdDev: %5.2f ms\n",
                    label, mean, median, stdDev);
        }
    }
}