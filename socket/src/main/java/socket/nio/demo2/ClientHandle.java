package socket.nio.demo2;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * NIO客户端
 *
 * @author smq
 * @version 1.0
 */
public class ClientHandle implements Runnable {
    private LinkedBlockingQueue<byte[]> receiveQueue = new LinkedBlockingQueue<byte[]>();

    private String host;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean started;

    public ClientHandle(String ip, int port) {
        this.host = ip;
        this.port = port;
        try {
            ProxySelector.setDefault(new ProxySelector() {
                @Override
                public List<Proxy> select(URI uri) {

                    List<Proxy> list = new ArrayList<>();
                    Proxy proxy = new Proxy(Proxy.Type.SOCKS,
                            new InetSocketAddress("pay.huifenqi.com", 1080));


                    list.add(proxy);
                    return list;
                }

                @Override
                public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
                    System.out.println("无法连接到代理！");
                }
            });

            //创建选择器  
            selector = Selector.open();
            //打开监听通道  
            socketChannel = SocketChannel.open();

            boolean connect = socketChannel.connect(new InetSocketAddress(host, port));
            System.out.println("server is connect status " + connect);

            //如果为 true，则此通道将被置于阻塞模式；如果为 false，则此通道将被置于非阻塞模式  
            socketChannel.configureBlocking(false);//开启非阻塞模式
            started = true;

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop() {
        started = false;
    }

    public LinkedBlockingQueue<byte[]> getReceiveQueue() {
        return receiveQueue;
    }

    @Override
    public void run() {
        //循环遍历selector  
        while (started) {
            try {
                //无论是否有读写事件发生，selector每隔1s被唤醒一次  
                selector.select(1000);
                //阻塞,只有当至少一个注册的事件发生的时候才会继续.  
//              selector.select();  
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                SelectionKey key = null;
                while (it.hasNext()) {
                    key = it.next();
                    it.remove();
                    try {
                        handleInput(key);
                    } catch (Exception e) {
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        //selector关闭后会自动释放里面管理的资源  
        if (selector != null)
            try {
                selector.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void handleInput(SelectionKey key) throws IOException {
        int headLength = 4;

        if (key.isValid()) {
            SocketChannel sc = (SocketChannel) key.channel();
            if (key.isConnectable()) {
                if (sc.finishConnect()) ;
                else System.exit(1);
            }
            //读消息  
            if (key.isReadable()) {
                //创建ByteBuffer，并开辟一个1M的缓冲区  
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                //读取请求码流，返回读取到的字节数  
                int readBytes = sc.read(buffer);
                //读取到字节，对字节进行编解码  
                if (readBytes > 0 && buffer.hasRemaining()) {
                    //将缓冲区当前的limit设置为position=0，用于后续对缓冲区的读取操作  
                    buffer.flip();

                    while (buffer.limit() > buffer.position()) {
                        /**
                         * 处理多余的内容，可能是脏数据
                         */
                        int surplus = buffer.limit() - buffer.position();
                        if (surplus < 4) {
                            buffer.get(new byte[surplus]);
                            System.out.println("positionZXZ:" + buffer.position() + "\t limit:"
                                    + buffer.limit());
                            continue;
                        }

                        /**
                         * 读取headler
                         */
                        byte[] header = new byte[headLength];
                        buffer.get(header);
                        String headerData = new String(header, "UTF-8");

                        int bodyLength = 0;
                        try {
                            bodyLength = Integer.parseInt(headerData);
                        } catch (NumberFormatException e) {
                            //TODO：判断是否是心跳包
                            System.out.println("format number error");
                        }
                        if (0 == bodyLength) {
                            //可能是心跳包
                            System.out.println("可能是心跳包" + headerData);
                            System.out.println("position:" + buffer.position() + "\t limit:"
                                    + buffer.limit());
                            continue;
                        }

                        /**
                         * 读取body
                         */
                        byte[] body = new byte[bodyLength];
                        ByteBuffer bodyBuffer = buffer.get(body);
                        String bodyData = new String(body, "UTF-8");

                        //System.out.println("客户端收到消息：" + bodyData);
                        receiveQueue.offer(body);
                    }
                }
                //没有读取到字节 忽略  
//              else if(readBytes==0);  
                //链路已经关闭，释放资源  
                else if (readBytes < 0) {
                    key.cancel();
                    sc.close();
                }
            }
        }
    }

    //异步发送消息  
    private void doWrite(SocketChannel channel, String request) throws IOException {
//        if (!channel.isConnected()) {
//            System.out.println(1111);
//            doConnect();
//        }
//        channel.write(ByteBuffer.wrap(request.getBytes()));

        //将消息编码为字节数组
        byte[] bytes = request.getBytes();
        //根据数组容量创建ByteBuffer
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        //将字节数组复制到缓冲区
        writeBuffer.put(bytes);
        //flip操作
        writeBuffer.flip();
        //发送缓冲区的字节数组
        channel.write(writeBuffer);
        //****此处不含处理“写半包”的代码
    }

    private void doConnect() throws IOException {
//        if (socketChannel.connect(new InetSocketAddress(host, port))) ;
//        else socketChannel.register(selector, SelectionKey.OP_CONNECT);


        if (null != socketChannel) {
            boolean closed = socketChannel.finishConnect();
            if (closed) {
                boolean connect = socketChannel.connect(new InetSocketAddress(host, port));
                System.out.println("server is connect status " + connect);
                socketChannel.configureBlocking(false);//开启非阻塞模式
            }

        }
        if (socketChannel.isConnected()) {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    public void sendMsg(String msg) throws Exception {
        socketChannel.register(selector, SelectionKey.OP_READ);
        doWrite(socketChannel, msg);
    }
}  