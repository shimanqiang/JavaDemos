package socket.nio.demo2;

import socket.IdGenerator;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {

    public static void main(String[] args) throws InterruptedException {
        String time = IdGenerator.genNextId();
        time = UUID.randomUUID().toString();
        String msg = String.format("%04d", time.getBytes().length) + time;
        boolean offer = Global.getSendQueue().offer(msg.getBytes());
        System.out.println(offer);


        /**
        ExecutorService es = Executors.newCachedThreadPool();

        int count = 10000;
        CountDownLatch latch = new CountDownLatch(count);
        //Client.start();
        for (int i = 0; i < count; i++) {
            es.execute(new Worker());
//            if (count % 20 == 0) {
//                Thread.sleep(100);
//            }
            //latch.countDown();
        }

//        Thread.sleep(3000);
        //latch.await();
        es.shutdown();
         */
    }
}

class Worker implements Runnable {

    @Override
    public void run() {
//        long time = System.currentTimeMillis();
        String time = IdGenerator.genNextId();
        time = UUID.randomUUID().toString();
        //System.out.println(time);
        try {
//            String s = Client.sendMsg("" + time);
//            System.out.println(time + "#" + s);

//            String str = "wwwww";
//            Client.sendMsg("0000" +"0005" + str);
            //System.out.println(String.format("%04d",time.getBytes().length));

            String msg = String.format("%04d", time.getBytes().length) + time;
            Global.sendQueue.offer(msg.getBytes());

//            String s = Client.sendMsg(String.format("%04d",time.getBytes().length) + time);
//            System.out.println(time + "\t" + s);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}