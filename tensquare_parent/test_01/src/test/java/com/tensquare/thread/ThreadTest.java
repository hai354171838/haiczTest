package com.tensquare.thread;

import java.util.HashMap;

/**
 * @author:Haicz
 * @date:2019/03/16
 */
public class ThreadTest {
    public static void main(String[] args) {
        //第一种线程的创建方法
        Thread thread = new Thread() {
            @Override
            public void run() {

                //让线程休眠3秒
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("当前线程的名字:"+Thread.currentThread().getName());
            }
        };
        thread.start();
        System.out.println("主线程的名字"+Thread.currentThread().getName());
        for (int i = 0; i < 15; i++) {
            System.out.println(i);
        }
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();

    }
}
