package com.tensquare.thread;

import java.util.concurrent.BlockingQueue;

/**
 * @author:Haicz
 * @date:2019/03/17
 */
public class Consumer implements Runnable {
    private final BlockingQueue<Integer> queue;

    public Consumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {

        try {
            Thread.sleep(3000);
            consumer(queue.take());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void consumer( Integer n) {

        System.out.println("Thread:"+Thread.currentThread().getId()+"counsumer:"+n);
    }
}
