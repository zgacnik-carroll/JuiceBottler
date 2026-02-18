public class Plant implements Runnable {

    public static final long PROCESSING_TIME = 5 * 1000;
    private static final int NUM_PLANTS = 2;
    private static final int NUM_WORKERS = 2;

    public final int ORANGES_PER_BOTTLE = 3;

    private final Thread thread;

    private int orangesProvided;
    private int orangesProcessed;

    private volatile boolean timeToWork;

    private BlockingMailbox mailbox;
    private Worker[] workers;

    public static void main(String[] args) {

        Plant[] plants = new Plant[NUM_PLANTS];

        for (int i = 0; i < NUM_PLANTS; i++) {
            plants[i] = new Plant(i + 1);
            plants[i].startPlant();
        }

        delay(PROCESSING_TIME, "Plant malfunction");

        for (Plant p : plants)
            p.stopPlant();

        for (Plant p : plants)
            p.waitToStop();

        int totalProvided = 0;
        int totalProcessed = 0;
        int totalBottles = 0;
        int totalWasted = 0;

        for (Plant p : plants) {
            totalProvided += p.getProvidedOranges();
            totalProcessed += p.getProcessedOranges();
            totalBottles += p.getBottles();
            totalWasted += p.getWaste();
        }

        System.out.println("Total provided/processed = "
                + totalProvided + "/" + totalProcessed);
        System.out.println("Created " + totalBottles +
                ", wasted " + totalWasted + " oranges");
    }

    private static void delay(long time, String errMsg) {
        try {
            Thread.sleep(Math.max(1, time));
        } catch (InterruptedException ignored) {}
    }

    Plant(int threadNum) {

        orangesProvided = 0;
        orangesProcessed = 0;

        thread = new Thread(this, "Plant[" + threadNum + "]");

        mailbox = new BlockingMailbox();

        workers = new Worker[NUM_WORKERS];
        for (int i = 0; i < NUM_WORKERS; i++) {
            workers[i] = new Worker(this, mailbox, i + 1);
        }
    }

    public String getThreadName() {
        return thread.getName();
    }

    public void startPlant() {
        timeToWork = true;

        for (Worker w : workers)
            w.startWorker();

        thread.start();
    }

    public void stopPlant() {
        timeToWork = false;

        for (Worker w : workers)
            w.stopWorker();
    }

    public void waitToStop() {
        try {
            thread.join();

            for (Worker w : workers)
                w.waitToStop();

        } catch (InterruptedException ignored) {}
    }

    public void run() {
        System.out.print(Thread.currentThread().getName() + " Processing oranges\n");

        while (timeToWork) {
            mailbox.put(new Orange());
            orangesProvided++;
            System.out.print(".");
        }

        System.out.println();
        System.out.println(Thread.currentThread().getName() + " Done");
    }

    public synchronized void incrementProcessed() {
        orangesProcessed++;
    }

    public int getProvidedOranges() {
        return orangesProvided;
    }

    public int getProcessedOranges() {
        return orangesProcessed;
    }

    public int getBottles() {
        return orangesProcessed / ORANGES_PER_BOTTLE;
    }

    public int getWaste() {
        return orangesProcessed % ORANGES_PER_BOTTLE;
    }
}
