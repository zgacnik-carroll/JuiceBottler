public class Worker implements Runnable {

    private final Thread thread;
    private final Plant plant;
    private final BlockingMailbox mailbox;

    private volatile boolean timeToWork;

    public Worker(Plant plant, BlockingMailbox mailbox, int num) {
        this.plant = plant;
        this.mailbox = mailbox;
        this.thread = new Thread(this,
                plant.getThreadName() + "-Worker[" + num + "]");
    }

    public void startWorker() {
        timeToWork = true;
        thread.start();
    }

    public void stopWorker() {
        timeToWork = false;
    }

    public void waitToStop() {
        try {
            thread.join();
        } catch (InterruptedException ignored) {}
    }

    public void run() {

        System.out.println(thread.getName() + " started.");

        while (timeToWork || !mailbox.isEmpty()) {

            Orange o = mailbox.get();

            while (o.getState() != Orange.State.Bottled) {
                o.runProcess();
            }

            plant.incrementProcessed();
        }

        System.out.println(thread.getName() + " stopped.");
    }

}
