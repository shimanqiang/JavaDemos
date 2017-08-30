package com.huifenqi.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadTest {

    private static final ExecutorService pool = Executors.newFixedThreadPool(10);


    public static void main(String[] args) {

        pool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName());
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(Thread.currentThread().getName());
                    }
                }, "t2").start();
            }
        });
        pool.shutdown();
    }
}
