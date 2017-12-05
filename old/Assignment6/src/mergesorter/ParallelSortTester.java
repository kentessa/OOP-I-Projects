package src.mergesorter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import static java.lang.Thread.yield;

public class ParallelSortTester implements Runnable {

    static final int AVAILABLEPROCESSORS = Runtime.getRuntime().availableProcessors();
    static  final  int ROUNDS = 15;
    int LENGTH = 1000;   // length of array to sort
    Integer[] randomArrays = null;

    private Thread thread;
    private String threadName;


    ParallelSortTester(String threadName) {
        this.threadName = threadName;
        System.out.printf("%s threads:  %n", threadName);
    }

    public static void main(String[] args) {
        for (int i = 1; i <= AVAILABLEPROCESSORS; i++ ) {

            int AVAILABLETHREADS = i;
            ParallelSortTester r1 = new ParallelSortTester( i + "");
            r1.start();
        }
    }


    /**
     * Returns true if the given array is in sorted ascending order.
     *
     * @param a the array to examine
     * @param comp the comparator to compare array elements
     * @return true if the given array is sorted, false otherwise
     */
    public static <E> boolean isSorted(E[] a, Comparator<? super E> comp) {
        for (int i = 0; i < a.length - 1; i++) {
            if (comp.compare(a[i], a[i + 1]) > 0) {
                System.out.println(a[i] + " > " + a[i + 1]);
                return false;
            }
        }
        return true;
    }

    // Randomly rearranges the elements of the given array.
    public static <E> void shuffle(E[] a) {
        for (int i = 0; i < a.length; i++) {
            // move element i to a random index in [i .. length-1]
            int randomIndex = (int) (Math.random() * a.length - i);
            swap(a, i, i + randomIndex);
        }
    }

    // Swaps the values at the two given indexes in the given array.
    public static final <E> void swap(E[] a, int i, int j) {
        if (i != j) {
            E temp = a[i];
            a[i] = a[j];
            a[j] = temp;
        }
    }

    // Creates an array of the given length, fills it with random
    // non-negative integers, and returns it.
    public static Integer[] createRandomArray(int length) {
        Integer[] a = new Integer[length];
        Random rand = new Random(System.currentTimeMillis());
        for (int i = 0; i < a.length; i++) {
            a[i] = rand.nextInt(1000000);
        }
        return a;
    }


    @Override
    public void run() {

        Comparator<Integer> comp = new Comparator<Integer>() {
            public int compare(Integer d1, Integer d2) {
                return d1.compareTo(d2);
            }
        };

        for (int i = 0; i < ROUNDS; i++) {

            randomArrays = createRandomArray(LENGTH);

            // run the algorithm and time how long it takes to sort the elements
            long startTime = System.currentTimeMillis();
            ParallelMergeSorter.sort(randomArrays, comp);
            long endTime = System.currentTimeMillis();

            System.out.printf("%10d elements  =>  %6d ms \n", LENGTH, endTime - startTime);
            LENGTH = LENGTH * 2;
        }


        if (!isSorted(randomArrays, comp)) {
            throw new RuntimeException("not sorted afterward: " + Arrays.toString(randomArrays));
        }

        LENGTH = 1000;
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this, threadName);
            thread.start();
        }
    }
}