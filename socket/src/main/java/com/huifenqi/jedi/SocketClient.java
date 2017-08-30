package com.huifenqi.jedi;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Hello world!
 */
public class SocketClient {
    private static SocketClient instance = null;

    private SocketClient() {
        start();
    }

//    private static class Holder {
//        private static final SocketClient instance = new SocketClient();
//    }

    public static SocketClient getInstance() {
        if (null == instance) {
            synchronized (SocketClient.class) {
                if (null == instance) {
                    instance = new SocketClient();
                }
            }
        }
        return instance;
//        return Holder.instance;
    }


    private LinkedBlockingQueue<byte[]> sendQueue = new LinkedBlockingQueue<byte[]>();
    private LinkedBlockingQueue<byte[]> receiveQueue = new LinkedBlockingQueue<byte[]>();

    private void start() {
        try {
            //Socket socket = new Socket("127.0.0.1", 8091);
            SocketChannel socketChannel = SocketChannel.open();
            //socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress("127.0.0.1", 8091));
            new SendThread(socketChannel, sendQueue).start();
            new ReceiveThread(socketChannel, receiveQueue).start();
            new HeartbeatThread(sendQueue).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private LinkedBlockingQueue<String> taskQueue = new LinkedBlockingQueue<>();


    private final ReentrantLock takeLock = new ReentrantLock();
    private final ReentrantLock putLock = new ReentrantLock();
    private final AtomicInteger count = new AtomicInteger();

    private final Condition notEmpty = takeLock.newCondition();
    private final Condition notFull = putLock.newCondition();

    public void mytest(String str) {
        sendQueue.offer(str.getBytes());
    }

    public String syncTest(String input) {
        taskQueue.offer(input);
        final AtomicInteger count = this.count;
        final ReentrantLock takeLock = this.takeLock;
//        takeLock.lockInterruptibly();
//
//        try {
//            while (count.get() == 0) {
//                notEmpty.await();
//            }
//            x = dequeue();
//            c = count.getAndDecrement();
//            if (c > 1)
//                notEmpty.signal();
//        } finally {
//            takeLock.unlock();
//        }
//        if (c == capacity)
//            signalNotFull();

        String ret = input + "#";
        try {
            takeLock.lockInterruptibly();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean offer = sendQueue.offer(input.getBytes());
        if (offer) {
        }

        try {

            byte[] take = receiveQueue.take();

            takeLock.unlock();
            ret += new String(take);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static void main(String[] args) {
        System.out.println(SocketClient.getInstance().toString());
        SocketClient.getInstance();
    }

    public static void main2(String[] args) {
        System.out.println("Hello World!");

        LinkedBlockingQueue<byte[]> sendQueue = new LinkedBlockingQueue<byte[]>();
        LinkedBlockingQueue<byte[]> receiveQueue = new LinkedBlockingQueue<byte[]>();

        try {
            Socket socket = new Socket("127.0.0.1", 8091);
//            new SendThread(socket, sendQueue).start();
//            new ReceiveThread(socket, receiveQueue).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(sendQueue.toString());

        int idx = 0;

        while (true) {
            String send = "test:" + System.currentTimeMillis() + "#test";
            idx++;
            if (idx % 3 == 0) {
                //System.out.println(idx);
                send = "00000000";
            }

            boolean offer = sendQueue.offer(send.getBytes());

            //sendQueue.put(send.getBytes());
            if (offer) {
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


    }


}

class HeartbeatThread extends Thread {
    private LinkedBlockingQueue<byte[]> queue;

    public HeartbeatThread(LinkedBlockingQueue<byte[]> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            String send = send = "00000000";

            boolean offer = queue.offer(send.getBytes());

            if (offer) {
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class SendThread extends Thread {
    private SocketChannel socket;
    private LinkedBlockingQueue<byte[]> queue;

    public SendThread(SocketChannel socket, LinkedBlockingQueue<byte[]> queue) {
        this.socket = socket;
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                byte[] poll = queue.take();
                //System.out.println(new String(poll));

//                PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
//                pw.write(new String(poll));
//                pw.flush();
                socket.write(ByteBuffer.wrap(poll));
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
}

class ReceiveThread extends Thread {
    private SocketChannel socket;

    private LinkedBlockingQueue<byte[]> queue;

    public ReceiveThread(SocketChannel socket, LinkedBlockingQueue<byte[]> queue) {
        this.socket = socket;
        this.queue = queue;
    }


    @Override
    public void run() {
        while (true) {
            try {
//                Reader reader = new InputStreamReader(socket.getInputStream());
//                CharBuffer charbuffer = CharBuffer.allocate(8192);
//                int index = -1;
//                while ((index = reader.read(charbuffer)) != -1) {
//                    charbuffer.flip();//设置从0到刚刚读取到的位置
//                    System.out.println("client:" + charbuffer.toString());
//
//                    queue.offer(charbuffer.toString().getBytes());
//                }
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                socket.read(buffer);

                buffer.flip();
                if (buffer.hasRemaining()) {
                    Charset charset = Charset.forName("UTF-8");
                    CharsetDecoder decoder = charset.newDecoder();
                    CharBuffer charBuffer = decoder.decode(buffer);
                    String data = charBuffer.toString();
                    buffer.clear();
                    System.out.println("client:" + data);
                    queue.offer(data.getBytes());
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
