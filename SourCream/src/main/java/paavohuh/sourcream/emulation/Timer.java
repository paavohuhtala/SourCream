
package paavohuh.sourcream.emulation;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A thread-safe tick-down timer. Ticks down at the configured rate until 
 * reaching zero. When zero is reached, the timer continues ticking, but doesn't
 * decrement the value any further.
 */
public class Timer implements Runnable {
    private int value;
    private final float ratePerSecond;
    private final Object valueLock;
    private final ScheduledExecutorService scheduler;
    private ScheduledFuture<?> future;
    
    /**
     * Creates a new timer.
     * @param value The initial value of the timer.
     * @param ratePerSecond The number of ticks per second.
     */
    public Timer(int value, float ratePerSecond) {
        this.value = value;
        this.valueLock = true;
        this.scheduler = new ScheduledThreadPoolExecutor(1);
        this.ratePerSecond = ratePerSecond;
    }
    
    /**
     * Starts the timer.
     */
    public void start() {
        if (future != null) {
            future.cancel(false);
        }
        
        future = scheduler.scheduleAtFixedRate(this, 0, (long) (1.0f / ratePerSecond * 1000), TimeUnit.MILLISECONDS);
    }

    /**
     * Gets whether or not the timer is running.
     * @return True if the timer is running.
     */
    public boolean isRunning() {
        if (future == null) {
            return false;
        }
        
        return !future.isDone();
    }
    
    /**
     * Stops the timer.
     */
    public void stop() {
        if (future == null) {
            return;
        }
        
        future.cancel(true);
    }
    
    /**
     * Gets the value of the timer.
     * @return The value.
     */
    public int getValue() {
        synchronized (valueLock) {
            return value;
        }
    }

    /**
     * Sets the value of the timer.
     * @param value The value.
     */
    public void setValue(int value) {
        synchronized (valueLock) {
            this.value = value;
        }
    }

    @Override
    public void run() {
        synchronized (valueLock) {
            if (value > 0) {
                value--;
            }
        }
    }
    
    public float getRatePerSecond() {
        return ratePerSecond;
    }
}
