package org.flow.owd.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolNote {


    /**
     * public ThreadPoolExecutor(int corePoolSize,
     * int maximumPoolSize,
     * long keepAliveTime,
     * TimeUnit unit,
     * BlockingQueue<Runnable> workQueue,
     * ThreadFactory threadFactory,
     * RejectedExecutionHandler handler)
     *
     * @param args
     */
    public static void main(String[] args) {
        new ThreadPoolExecutor(10, 10, 200L, TimeUnit.MILLISECONDS
                , new LinkedBlockingQueue<Runnable>(10),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

}
