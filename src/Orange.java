/**
 * Represents an Orange that moves through a sequence of processing states.
 *
 * Each orange begins in the {@code Fetched} state and progresses through
 * several stages until it reaches the final {@code Processed} state.
 *
 * The processing time for each state is simulated using Thread.sleep().
 */
public class Orange {

    /**
     * Represents the different stages of orange processing.
     *
     * Each state contains a processing time (in milliseconds) that
     * simulates the amount of work required to complete that stage.
     */
    public enum State {

        /** Orange has been fetched from source. */
        Fetched(15),

        /** Orange has been peeled. */
        Peeled(38),

        /** Orange has been squeezed for juice. */
        Squeezed(29),

        /** Juice has been bottled. */
        Bottled(17),

        /** Final processed state. */
        Processed(1);

        /** Index of the final state in the enum sequence. */
        private static final int finalIndex = State.values().length - 1;

        /** Time (in milliseconds) required to complete this state. */
        final int timeToComplete;

        /**
         * Constructs a processing state.
         *
         * @param timeToComplete time in milliseconds required for this state
         */
        State(int timeToComplete) {
            this.timeToComplete = timeToComplete;
        }

        /**
         * Returns the next state in the processing sequence.
         *
         * @return the next {@code State}
         * @throws IllegalStateException if already at the final state
         */
        State getNext() {
            int currIndex = this.ordinal();

            // Prevent advancing beyond the final processing state
            if (currIndex >= finalIndex) {
                throw new IllegalStateException("Already at final state");
            }

            return State.values()[currIndex + 1];
        }
    }

    /** Current processing state of the orange. */
    private State state;

    /**
     * Creates a new Orange starting in the {@code Fetched} state.
     * Immediately performs work associated with the initial state.
     */
    public Orange() {
        state = State.Fetched;
        doWork();
    }

    /**
     * Returns the current processing state of the orange.
     *
     * @return current {@code State}
     */
    public State getState() {
        return state;
    }

    /**
     * Advances the orange to the next processing stage.
     *
     * Simulates work for the new state after transitioning.
     *
     * @throws IllegalStateException if the orange has already
     *                               reached the final state
     */
    public void runProcess() {

        // Don't attempt to process an already completed orange
        if (state == State.Processed) {
            throw new IllegalStateException("This orange has already been processed");
        }

        // Move to the next state in the process
        state = state.getNext();

        // Simulate work for the new state
        doWork();
    }

    /**
     * Simulates work being performed for the current state.
     *
     * The thread sleeps for the duration defined by the state's
     * processing time. If interrupted, a warning message is printed.
     */
    private void doWork() {
        try {
            // Sleep for the amount of time necessary to do the work
            Thread.sleep(state.timeToComplete);
        } catch (InterruptedException e) {
            // Processing was interrupted; result may be incomplete
            System.err.println("Incomplete orange processing, juice may be bad");
        }
    }
}
