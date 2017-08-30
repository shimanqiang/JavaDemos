package socket.nio.demo2;

import socket.IdGenerator;

import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

public class Client {
    private static String DEFAULT_HOST = "127.0.0.1";
    private static int DEFAULT_PORT = 12345;
    private static ClientHandle clientHandle;

    public static void start() {
        start(DEFAULT_HOST, DEFAULT_PORT);
    }

    public static synchronized void start(String ip, int port) {
        if (clientHandle != null)
            clientHandle.stop();
        clientHandle = new ClientHandle(ip, port);
        new Thread(clientHandle, "Server").start();
    }

    //向服务器发送消息  
    public static synchronized String sendMsg(String msg) throws Exception {

        clientHandle.sendMsg(msg);

        return new String(clientHandle.getReceiveQueue().take());
    }

    public static void main(String[] args) {
        start();

        new Thread(() -> {
            try {
                while (true) {
                    byte[] take = Global.getSendQueue().take();
                    String s = Client.sendMsg(new String(take));
                    System.out.println(new String(take) + "\t" + s);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }, "Test Worker").start();


        new Thread(() -> {
            try {
                int index = 0;
                while (true) {
                    index++;
                    String time = UUID.randomUUID().toString();
                    String msg = String.format("%04d", time.getBytes().length) + time;
                    boolean offer = Global.getSendQueue().offer(msg.getBytes());
                    if (index % 5 == 0) {
                        Thread.sleep(5 * 1000);
                    }

                    System.out.println("- - - - - - - - -- - - - - -- - - - - -");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }, "Test Worker Data").start();


        new Thread(() -> {
            try {
                while (true) {
                    String send = "0000";
                    clientHandle.sendMsg(send);

                    Thread.sleep(5 * 1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "heartBeat").start();
    }
}  