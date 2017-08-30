package socket.nio.demo1;

import socket.IdGenerator;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {

    public static void main(String[] args) throws InterruptedException {


        ExecutorService es = Executors.newCachedThreadPool();

        int count = 100;
        CountDownLatch latch = new CountDownLatch(count);
        Client.start();
        for (int i = 0; i < count; i++) {
            es.execute(new Worker());
            //Thread.sleep(100);
            //latch.countDown();
        }

//        Thread.sleep(3000);
        //latch.await();
        es.shutdown();
    }
}

class Worker implements Runnable {

    @Override
    public void run() {
//        long time = System.currentTimeMillis();
        String time = IdGenerator.genNextId();
        //System.out.println(time);
        try {
            String s = Client.sendMsg("" + time);
            System.out.println(time + "#" + s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}