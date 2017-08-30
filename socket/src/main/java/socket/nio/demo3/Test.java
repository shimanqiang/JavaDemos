package socket.nio.demo3;

import socket.IdGenerator;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by smq on 2017/6/6.
 */
public class Test {

    private static final LinkedBlockingQueue<byte[]> sendQueue = new LinkedBlockingQueue<byte[]>();
    private static final LinkedBlockingQueue<byte[]> receiveQueue = new LinkedBlockingQueue<byte[]>();

    public static void main(String[] args) {


        AsyncLongConnectClient client = new AsyncLongConnectClient(sendQueue, receiveQueue);
        client.start();


        while (true) {
            long start = System.currentTimeMillis();
            sendMsgWorker(sendQueue);
            System.out.println(System.currentTimeMillis() - start + " ms");
            try {
                Thread.sleep(30 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public static synchronized String exportSendMsg(String msg) {
        try {
            boolean offer = sendQueue.offer(msg.getBytes());
            if (offer) {
                byte[] take = receiveQueue.take();
                return new String(take);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void sendMsgWorker(LinkedBlockingQueue<byte[]> sendQueue) {
        int count = 100000;
        for (int i = 0; i < count; i++) {
            try {
                byte[] bytes = pack();
                if (bytes != null) {
                    //sendQueue.put(bytes);
                    String input = new String(bytes);
                    String s = exportSendMsg(input);

                    System.out.println(input + "\t" + s);
                }
                //Thread.sleep(3 * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private static void sendMsgWorker2(LinkedBlockingQueue<byte[]> sendQueue) {
        ExecutorService es = Executors.newFixedThreadPool(4);

        int count = 1000;
        CountDownLatch latch = new CountDownLatch(count);

        for (int i = 0; i < count; i++) {
            es.execute(() -> {
//                while (true) {
//
//                }
                try {
                    byte[] bytes = pack();
                    if (bytes != null) {
                        //sendQueue.put(bytes);
                        String input = new String(bytes);
                        String s = exportSendMsg(input);

                        System.out.println(input + "\t" + s);
                    }
                    //Thread.sleep(3 * 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            //latch.countDown();
        }

        //        Thread.sleep(3000);
        //latch.await();
        es.shutdown();
    }

    private static byte[] pack() {
        String str = UUID.randomUUID().toString();
        String msg = String.format("%04d", str.getBytes().length) + str;

        return msg.getBytes();
    }
}
