/**
 * A thread-safe blocking mailbox used to coordinate communication
 * between a producer (Plant) and multiple consumers (Worker threads).
 *
 * This class implements a classic Producer–Consumer pattern using
 * intrinsic locks (synchronized), wait(), and notifyAll().
 *
 * Only one Orange may exist in the mailbox at a time.
 * If the mailbox is full, producers must wait.
 * If the mailbox is empty, consumers must wait.
 */
public class BlockingMailbox {

    /**
     * The single Orange currently stored in the mailbox.
     * If null, the mailbox is empty.
     */
    private Orange orange;

    /**
     * Constructs an empty BlockingMailbox.
     */
    public BlockingMailbox() {
        orange = null;
    }

    /**
     * Places an Orange into the mailbox.
     *
     * If the mailbox is already full, the calling thread
     * waits until the mailbox becomes empty.
     *
     * This method is synchronized to ensure mutual exclusion
     * when accessing the shared Orange reference.
     *
     * @param o the Orange to place into the mailbox
     */
    public synchronized void put(Orange o) {

        // Wait while mailbox is full (producer must block)
        while (orange != null) {
            try {
                wait();   // Releases lock and waits to be notified
            } catch (InterruptedException ignored) {
                // Ignored per assignment requirements
            }
        }

        // Place the orange into the mailbox
        orange = o;

        // Notify all waiting threads that mailbox state changed
        notifyAll();
    }

    /**
     * Retrieves and removes the Orange from the mailbox.
     *
     * If the mailbox is empty, the calling thread waits
     * until an Orange becomes available.
     *
     * This method is synchronized to prevent race conditions
     * between multiple Worker threads.
     *
     * @return the Orange retrieved from the mailbox
     */
    public synchronized Orange get() {

        // Wait while mailbox is empty (consumer must block)
        while (orange == null) {
            try {
                wait();   // Releases lock and waits to be notified
            } catch (InterruptedException ignored) {
                // Ignored per assignment requirements
            }
        }

        // Retrieve the orange
        Orange ret = orange;

        // Remove orange from mailbox (now empty)
        orange = null;

        // Notify all waiting threads that mailbox state changed
        notifyAll();

        return ret;
    }

    /**
     * Checks whether the mailbox is empty.
     *
     * This method is synchronized to ensure visibility
     * of the shared orange reference across threads.
     *
     * @return true if the mailbox contains no Orange,
     *         false otherwise
     */
    public synchronized boolean isEmpty() {
        return orange == null;
    }
}
