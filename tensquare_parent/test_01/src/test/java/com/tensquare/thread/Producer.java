package com.tensquare.thread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * @author:Haicz
 * @date:2019/03/17
 */
public class Producer implements Runnable {
    private final BlockingQueue<Integer> queue;

    public Producer(BlockingQueue<Integer>  queue) {
        this.queue = queue;
    }

    @Override
    public void run() {


            try {
                while (true) {

                    Thread.sleep(2000);
                    queue.put(producer());

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }


    public int producer() {
        int n = new Random().nextInt(10000);
        System.out.println("Thread:" + Thread.currentThread().getId() + " produce:" + n);
        return n;
    }
}
