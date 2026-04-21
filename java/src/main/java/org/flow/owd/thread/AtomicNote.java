package org.flow.owd.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicNote {
    AtomicInteger integer = new AtomicInteger();
    CountDownLatch latch = new CountDownLatch(10);
}
