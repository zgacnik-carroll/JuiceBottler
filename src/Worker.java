/**
 * Worker represents a consumer thread in the producer-consumer simulation.
 *
 * Each Worker belongs to a specific Plant and retrieves Orange objects
 * from a shared BlockingMailbox. The worker processes each orange until
 * it reaches the Bottled state, then updates the plant’s processed count.
 *
 * This class demonstrates:
 * - Consumer thread behavior
 * - Coordination with a blocking mailbox
 * - Safe shutdown using a volatile control flag
 */
public class Worker implements Runnable {

    /** Thread that executes this worker */
    private final Thread thread;

    /** Reference to the owning plant */
    private final Plant plant;

    /** Shared mailbox used to retrieve oranges */
    private final BlockingMailbox mailbox;

    /** Controls whether the worker should continue running */
    private volatile boolean timeToWork;

    /**
     * Constructs a Worker associated with a specific plant.
     *
     * @param plant   the plant this worker belongs to
     * @param mailbox shared mailbox used for retrieving oranges
     * @param num     worker number (used for naming the thread)
     */
    public Worker(Plant plant, BlockingMailbox mailbox, int num) {
        this.plant = plant;
        this.mailbox = mailbox;

        // Create the worker thread with a descriptive name
        this.thread = new Thread(this,
                plant.getThreadName() + "-Worker[" + num + "]");
    }

    /**
     * Starts the worker thread and enables processing.
     */
    public void startWorker() {
        timeToWork = true;
        thread.start();
    }

    /**
     * Signals the worker to stop accepting new work.
     * The worker will continue processing until the mailbox is empty.
     */
    public void stopWorker() {
        timeToWork = false;
    }

    /**
     * Waits for this worker thread to terminate.
     */
    public void waitToStop() {
        try {
            thread.join();
        } catch (InterruptedException ignored) {}
    }

    /**
     * Worker execution logic.
     *
     * Continuously retrieves oranges from the mailbox and processes
     * them until:
     *   1) The plant signals shutdown, and
     *   2) The mailbox is empty.
     *
     * This ensures all produced oranges are fully processed before exit.
     */
    public void run() {

        System.out.println(thread.getName() + " started.");

        // Continue working while plant is active OR mailbox still contains oranges
        while (timeToWork || !mailbox.isEmpty()) {

            // Retrieve next orange (blocks if mailbox is empty)
            Orange o = mailbox.get();

            // Process orange through its lifecycle until bottled
            while (o.getState() != Orange.State.Bottled) {
                o.runProcess();
            }

            // Notify plant that an orange has been fully processed
            plant.incrementProcessed();
        }

        System.out.println(thread.getName() + " stopped.");
    }
}
