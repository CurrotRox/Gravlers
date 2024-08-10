import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class Main {
    public static void main(String[] args) {
        long startTime = System.nanoTime();
        Random rand = new Random();
        int rollAmount = 1000000000; // Amount of rolls to be calculated

        //Setting up a thread pool with a set amount of threads.
        ExecutorService executor = Executors.newFixedThreadPool(28);
        RollInstance maxOnesInstance = new RollInstance(rand);

        for (int i = 0; i < rollAmount; i++) {
            executor.execute(maxOnesInstance); //Getting all the instances started
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            try {
                Thread.sleep(25); //Waiting while the executor shuts down.
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        long endTime = System.nanoTime();
        long totalDuration = (endTime - startTime) / 1000000;

        System.out.println("Max ones: " + RollInstance.getMaxOnes());
        System.out.println("Completed Instances: " + RollInstance.getRunInstances());
        System.out.println("Total duration (ms): " + totalDuration);
    }
}

class RollInstance implements Runnable {
    //Using AtomicIntegers to prevent race conditions of using multiple threads. The runInstances is just to make sure everything is synchronized properly.
    private static AtomicInteger globalMaxOnes = new AtomicInteger(0);
    private static AtomicInteger runInstances = new AtomicInteger(0);

    private  Random rand;

    public RollInstance(Random randomGen) {
        rand = randomGen; // Passing the same instance of the rand variable, so it's not being reinitialized literally a billion times.
    }
    public void run() {
        int rolls = 0;
        for (int i = 0; i < 231; i++) {
            if (rand.nextInt(4) == 0) rolls++; //If the "roll" is 0, that's a 1, and we add it to the rolls.
        }
        updateMaxOnes(rolls); //Once finished, check and if need be, update maxOnes.
    }

    private void updateMaxOnes(int contender) {
        // Atomically update globalMaxOnes
        globalMaxOnes.updateAndGet(currentMax -> Math.max(currentMax, contender));
        runInstances.incrementAndGet(); // Atomically increment the runInstances
    }

    public static int getMaxOnes(){
        return globalMaxOnes.get();
    }

    public static int getRunInstances() {
        return runInstances.get();
    }
}
