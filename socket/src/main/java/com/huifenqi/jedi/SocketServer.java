package com.huifenqi.jedi;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by smq on 2017/6/3.
 */
public class SocketServer {

    private final static ExecutorService pool = Executors.newCachedThreadPool();


    public static void main(String[] args) {

        ServerSocket server;
        try {
            System.out.println("server start");
            //server = new ServerSocket(8091);
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(8091));
            while (true) {

                SocketChannel socketChannel = serverSocketChannel.accept();
                //Socket socket = server.accept(); //
                pool.execute(new Handler(socketChannel));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Handler implements Runnable {
    private SocketChannel socket;

    public Handler(SocketChannel socket) {
        this.socket = socket;
    }

    private boolean canRun = true;
    private long updateTime = System.currentTimeMillis();

    public void run() {
        if (null == socket) {
            return;
        }

        try {
            System.out.println("Thread:" + Thread.currentThread() + "新连接:" + socket.getRemoteAddress());

            new Thread(() -> {
                while (true) {
                    long l = System.currentTimeMillis() - updateTime;
                    //System.out.println(updateTime);
                    if (l > 30 * 1000) {
                        canRun = false;
                    }
                    if (!canRun) {
                        break;
                    }
                }
            }).start();

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (true) {
                if (!canRun) {
                    break;
                }

                int read = socket.read(buffer);
                buffer.flip();
                if (buffer.hasRemaining()) {
                    Charset charset = Charset.forName("UTF-8");
                    CharsetDecoder decoder = charset.newDecoder();
                    CharBuffer charBuffer = decoder.decode(buffer);
                    String data = charBuffer.toString();
                    //buffer.clear();
                    //String data = new String(buffer.array());
                    System.out.println("data:" + data);
                    if ("00000000".equals(data)) {
                        updateTime = System.currentTimeMillis();
                        System.out.println("Thread:" + Thread.currentThread() + socket.getRemoteAddress() + "收到心跳");
                        continue;
                    }
                    Thread.sleep(1000);
                    updateTime = System.currentTimeMillis();

                    ByteBuffer bb = ByteBuffer.wrap(data.getBytes());
                    socket.write(bb);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("Thread:" + Thread.currentThread() + "关闭连接:" + socket.getRemoteAddress());
                if (socket != null)
                    socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}


class Handler2 implements Runnable {
    private Socket socket;

    public Handler2(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        if (null == socket) {
            return;
        }

        long updateTime = System.currentTimeMillis();

        InputStream is = null;
        OutputStream os = null;

        BufferedInputStream bis = null;

        try {
            socket.setKeepAlive(true);
            is = socket.getInputStream();
            bis = new BufferedInputStream(socket.getInputStream());
            os = socket.getOutputStream();

            System.out.println("Thread:" + Thread.currentThread() + "新连接:" + socket.getInetAddress() + ":" + socket.getPort());

            ByteBuffer buffer = ByteBuffer.allocate(64);
            //CharBuffer buffer = CharBuffer.allocate(1024);
            byte[] buff = new byte[5];
            while (true) {
                byte[] headLength = new byte[8];
                is.read(headLength);
                System.out.println("8位报文总长度：" + new String(headLength));

                byte[] merchantLength = new byte[15];
                is.read(merchantLength);
                System.out.println("15位合作方编号：" + new String(merchantLength));

//                byte[] tradeCodeLength = new byte[8];
//                is.read(tradeCodeLength);
//                System.out.println("8位交易码："+ new String(tradeCodeLength));
//
//                byte[] signLength = new byte[4];
//                is.read(signLength);
//                System.out.println("4位加签域长度:"+ new String(signLength));
//
//                int len1 = 5;
//                byte[] s1Length = new byte[len1];
//                is.read(s1Length);
//                System.out.println("签名域值:"+ new String(s1Length));
//
//                int len2 = 5;
//                byte[] s2Length = new byte[len2];
//                is.read(s2Length);
//                System.out.println("密文串:"+ new String(s2Length));


//                String s = new String(buff);
//                System.out.println("data:" + s);
//                is.read(buffer.array());
//                buffer.flip();
//                if (buffer.hasRemaining()) {
//                    String s = buffer.asCharBuffer().toString();
//                    System.out.println("data:" + s);
//                }

                //Thread.sleep(5000);
            }


        } catch (Exception e) {
            e.printStackTrace();
            try {
                System.out.println("Thread:" + Thread.currentThread() + "关闭连接:" + socket.getInetAddress() + ":" + socket.getPort());

                if (is != null)
                    is.close();
                if (os != null)
                    os.close();
                if (socket != null)
                    socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
