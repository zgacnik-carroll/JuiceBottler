/**
 * Plant represents a producer in a producer-consumer simulation.
 *
 * Each Plant runs in its own thread and continuously produces Orange
 * objects for a fixed amount of time. Produced oranges are placed into
 * a BlockingMailbox shared with multiple Worker threads.
 *
 * Workers consume and process oranges into bottles.
 *
 * This class demonstrates:
 * - Multiple producer threads (plants)
 * - Multiple consumer threads (workers per plant)
 * - Thread coordination using a blocking mailbox
 */
public class Plant implements Runnable {

    /** Total time (in milliseconds) that plants produce oranges */
    public static final long PROCESSING_TIME = 5 * 1000;

    /** Number of plant producer threads */
    private static final int NUM_PLANTS = 2;

    /** Number of worker threads per plant */
    private static final int NUM_WORKERS = 2;

    /** Number of oranges required to produce one bottle */
    public final int ORANGES_PER_BOTTLE = 3;

    /** Thread that runs this plant */
    private final Thread thread;

    /** Total oranges produced by this plant */
    private int orangesProvided;

    /** Total oranges processed by this plant's workers */
    private int orangesProcessed;

    /** Controls whether the plant should continue producing oranges */
    private volatile boolean timeToWork;

    /** Shared mailbox used for producer-consumer coordination */
    private BlockingMailbox mailbox;

    /** Array of worker threads assigned to this plant */
    private Worker[] workers;

    /**
     * Main entry point.
     *
     * Creates multiple plants, starts them, allows them to run for a fixed
     * duration, then stops them and prints final statistics.
     */
    public static void main(String[] args) {

        Plant[] plants = new Plant[NUM_PLANTS];

        // Create and start each plant
        for (int i = 0; i < NUM_PLANTS; i++) {
            plants[i] = new Plant(i + 1);
            plants[i].startPlant();
        }

        // Let plants run for the configured processing time
        delay(PROCESSING_TIME, "Plant malfunction");

        // Stop all plants
        for (Plant p : plants)
            p.stopPlant();

        // Wait for all plants and workers to finish
        for (Plant p : plants)
            p.waitToStop();

        int totalProvided = 0;
        int totalProcessed = 0;
        int totalBottles = 0;
        int totalWasted = 0;

        // Aggregate totals from all plants
        for (Plant p : plants) {
            totalProvided += p.getProvidedOranges();
            totalProcessed += p.getProcessedOranges();
            totalBottles += p.getBottles();
            totalWasted += p.getWaste();
        }

        // Display final statistics
        System.out.println("Total provided/processed = "
                + totalProvided + "/" + totalProcessed);
        System.out.println("Created " + totalBottles +
                ", wasted " + totalWasted + " oranges");
    }

    /**
     * Utility method to pause execution safely.
     *
     * @param time   time in milliseconds to sleep
     * @param errMsg unused error message placeholder
     */
    private static void delay(long time, String errMsg) {
        try {
            Thread.sleep(Math.max(1, time));
        } catch (InterruptedException ignored) {}
    }

    /**
     * Constructs a Plant with a unique thread identifier.
     *
     * Initializes:
     * - Production counters
     * - Blocking mailbox
     * - Worker threads
     *
     * @param threadNum identifier for naming the plant thread
     */
    Plant(int threadNum) {

        orangesProvided = 0;
        orangesProcessed = 0;

        // Create plant thread
        thread = new Thread(this, "Plant[" + threadNum + "]");

        // Create shared mailbox
        mailbox = new BlockingMailbox();

        // Create worker threads assigned to this plant
        workers = new Worker[NUM_WORKERS];
        for (int i = 0; i < NUM_WORKERS; i++) {
            workers[i] = new Worker(this, mailbox, i + 1);
        }
    }

    /**
     * Returns the thread name of this plant.
     *
     * @return plant thread name
     */
    public String getThreadName() {
        return thread.getName();
    }

    /**
     * Starts the plant and its worker threads.
     */
    public void startPlant() {
        timeToWork = true;

        // Start all workers first
        for (Worker w : workers)
            w.startWorker();

        // Start plant producer thread
        thread.start();
    }

    /**
     * Signals the plant and its workers to stop.
     * Workers continue until mailbox is empty.
     */
    public void stopPlant() {
        timeToWork = false;

        for (Worker w : workers)
            w.stopWorker();
    }

    /**
     * Waits for the plant thread and all worker threads to finish.
     */
    public void waitToStop() {
        try {
            thread.join();

            for (Worker w : workers)
                w.waitToStop();

        } catch (InterruptedException ignored) {}
    }

    /**
     * Plant producer logic.
     *
     * Continuously creates oranges and places them into the mailbox
     * until the timeToWork flag becomes false.
     */
    public void run() {
        System.out.print(Thread.currentThread().getName() + " Processing oranges\n");

        while (timeToWork) {
            mailbox.put(new Orange());  // Produce orange
            orangesProvided++;
            System.out.print(".");      // Progress indicator
        }

        System.out.println();
        System.out.println(Thread.currentThread().getName() + " Done");
    }

    /**
     * Increments the processed counter.
     *
     * Synchronized to prevent race conditions between worker threads.
     */
    public synchronized void incrementProcessed() {
        orangesProcessed++;
    }

    /**
     * @return total oranges produced by this plant
     */
    public int getProvidedOranges() {
        return orangesProvided;
    }

    /**
     * @return total oranges processed by this plant's workers
     */
    public int getProcessedOranges() {
        return orangesProcessed;
    }

    /**
     * Calculates number of complete bottles produced.
     *
     * @return bottles created
     */
    public int getBottles() {
        return orangesProcessed / ORANGES_PER_BOTTLE;
    }

    /**
     * Calculates leftover oranges that do not complete a bottle.
     *
     * @return wasted oranges
     */
    public int getWaste() {
        return orangesProcessed % ORANGES_PER_BOTTLE;
    }
}
