package com.huifenqi.jedi;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by smq on 2017/6/3.
 */
public class Test {

    public static void main(String[] args) throws InterruptedException {


        ExecutorService es = Executors.newCachedThreadPool();

        int count = 100;
//        CountDownLatch latch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            es.execute(new Worker());
            //Thread.sleep(100);
        }

//        Thread.sleep(3000);
        //latch.await();
        es.shutdown();
    }
}

class Worker implements Runnable {

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        //String s = SocketClient.getInstance().syncTest("" + time);
        //System.out.println(Thread.currentThread() + "#" + time + "#" + s);
        SocketClient.getInstance().mytest("" + time);
    }
}

class Worker2 implements Runnable {
    private CountDownLatch latch;

    public Worker2(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        String s = SocketClient.getInstance().syncTest("" + time);
        System.out.println(Thread.currentThread() + "--" + s);
        latch.countDown();
    }
}