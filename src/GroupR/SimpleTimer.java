package GroupR;

import java.text.DecimalFormat;

public class SimpleTimer {

    private final DecimalFormat time = new DecimalFormat("#.###");
    private long start;

    public SimpleTimer() {
        start = -1;
    }

    public void start() {
        if(start != -1) {
            throw new IllegalArgumentException("Stop timer before starting.");
        }
        start = System.nanoTime();
    }


    public String stop() {
        if(this.start == -1) {
            throw new IllegalArgumentException("Timer must be started before stopping.");
        }
        long end = System.nanoTime();
        long start = this.start;
        this.start = -1;
        return "Execution Time: " + time.format(((end - start) / 1000000000.0)) + "s";
    }
}
