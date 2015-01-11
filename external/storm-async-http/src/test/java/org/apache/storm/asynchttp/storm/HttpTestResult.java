package org.apache.storm.asynchttp.storm;

import java.io.Serializable;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by andrew on 9/1/15.
 */
public class HttpTestResult implements Serializable {
    private static Semaphore done = new Semaphore(0);
    private static int status = 0;
    private static String body = null;
    private static boolean isDone = false;

    synchronized static public void setResult(int status, String body) {
        if (isDone)
            throw new IllegalStateException("Result should only be set once.");
        isDone = true;
        HttpTestResult.status = status;
        HttpTestResult.body = body;
        done.release();
    }

    public static void waitResult(long timeout, TimeUnit timeUnit) throws InterruptedException {
        if(!done.tryAcquire(timeout, timeUnit))
            throw new RuntimeException("Timeout!");
    }

    synchronized public static int getStatus() {
        if (!isDone)
            throw new IllegalStateException("Result should be read only after 'waitResult'");
        return status;
    }

    synchronized public static String getBody() {
        if (!isDone)
            throw new IllegalStateException("Result should be read only after 'waitResult'");
        return body;
    }
}
