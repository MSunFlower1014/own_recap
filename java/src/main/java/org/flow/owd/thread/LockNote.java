package org.flow.owd.thread;

import java.util.concurrent.locks.ReentrantLock;

public class LockNote {
    private static ReentrantLock lock = new ReentrantLock(Boolean.TRUE);

    public static void main(String[] args) {
        try {
            lock.lock();
        } catch (Exception e) {
            lock.unlock();
        }


        synchronized (lock) {
            System.out.println("hello");
        }
    }
}
