
package paavohuh.sourcream.emulation;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A thread-safe tick-down timer, with configurable rate.
 */
public class Timer implements Runnable {
    private int value;
    private final float ratePerSecond;
    private final Object valueLock;
    private final ScheduledExecutorService scheduler;
    private ScheduledFuture<?> future;
    
    public Timer(int value, float ratePerSecond) {
        this.value = value;
        this.valueLock = true;
        this.scheduler = new ScheduledThreadPoolExecutor(1);
        this.ratePerSecond = ratePerSecond;
    }
    
    public void start() {
        if (future != null) {
            future.cancel(false);
        }
        
        future = scheduler.scheduleAtFixedRate(this, 0, (long) (1.0f / ratePerSecond * 1000), TimeUnit.MILLISECONDS);
    }

    public boolean isRunning() {
        if (future == null) {
            return false;
        }
        
        return !future.isDone();
    }
    
    public void stop() {
        if (future == null) {
            return;
        }
        
        future.cancel(true);
    }
    
    public int getValue() {
        synchronized(valueLock) {
            return value;
        }
    }

    public void setValue(int value) {
        synchronized(valueLock) {
            this.value = value;
        }
    }

    @Override
    public void run() {
        synchronized(valueLock) {
            if (value > 0) {
                value--;
            }
        }
    }
    
    public float getRatePerSecond() {
        return ratePerSecond;
    }
}
