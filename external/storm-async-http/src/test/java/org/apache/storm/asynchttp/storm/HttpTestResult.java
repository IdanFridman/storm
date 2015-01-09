package org.apache.storm.asynchttp.storm;

import java.io.Serializable;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by andrew on 9/1/15.
 */
public class HttpTestResult implements Serializable {
    private Semaphore done = new Semaphore(-1);
    private int status = 0;
    private String body = null;
    private boolean isDone = false;

    synchronized public void setResult(int status, String body) {
        if (isDone)
            throw new IllegalStateException("Result should only be set once.");
        isDone = true;
        this.status = status;
        this.body = body;
        done.release();
    }

    public void waitResult(long timeout, TimeUnit timeUnit) throws InterruptedException {
        if(!done.tryAcquire(timeout, timeUnit))
            throw new RuntimeException("Timeout!");
    }

    synchronized public int getStatus() {
        if (!isDone)
            throw new IllegalStateException("Result should be read only after 'waitResult'");
        return status;
    }

    synchronized public String getBody() {
        if (!isDone)
            throw new IllegalStateException("Result should be read only after 'waitResult'");
        return body;
    }
}
