package socket.nio.demo3;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by smq on 2017/6/5.
 */
public class AsyncLongConnectClient {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 收发消息队列
     */
    private LinkedBlockingQueue<byte[]> sendQueue;
    private LinkedBlockingQueue<byte[]> receiveQueue;

    /**
     * socket客户端帮助对象
     */
    private final SocketHelper socketHelper = new SocketHelper();
    /**
     * socket运行配置
     */
    private final SocketConfig config = new SocketConfig();

    /**
     * 可执行标记
     */
    private boolean canRun = false;


    /**
     * @param sendQueue    发送队列
     * @param receiveQueue 接受队列
     */
    public AsyncLongConnectClient(LinkedBlockingQueue<byte[]> sendQueue, LinkedBlockingQueue<byte[]> receiveQueue) {
        if (null == sendQueue || null == receiveQueue) {
            throw new RuntimeException("收发队列不能为空");
        }
        this.sendQueue = sendQueue;
        this.receiveQueue = receiveQueue;
    }


    /**
     * 停止
     */
    public void stop() {
        this.canRun = false;
    }

    /**
     * 启动
     */
    public void start() {

        config.setCanRun(true);
        config.setHostName("localhost");
        config.setHostAddress("127.0.0.1");
        config.setPort(12345);
        this.canRun = config.isCanRun();

        /**
         * test ----------------------------
         */
        config.setHeartBeatMsg("0000");
        config.setHeadLength(4);
        config.setMessageCodeLength(0);
        config.setMacLength(0);
        /**
         * test ----------------------------
         */

        /**
         * 运行线程：心跳检测
         */
        new Thread(() -> {
            while (true) {
                try {
                    if (canRun) {
                        //检查心跳
                        this.heartbeatCheck();

                        //心跳间隔时间，单位秒
                        Thread.sleep(config.getHeartBeatInterval());
                    } else {
                        //检测运行间隔
                        Thread.sleep(config.getCheckInterval());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "thread-heartbeat-check").start();


        /**
         * 运行线程：发送报文消息
         */
        new Thread(() -> {
            while (true) {
                try {
                    if (canRun) {
                        sendMessage();
                    } else {
                        //检测运行间隔
                        Thread.sleep(config.getCheckInterval());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "thread-send").start();


        /**
         * 运行线程：接受报文消息
         */
        new Thread(() -> {
            while (true) {
                try {
                    if (canRun) {
                        receiveMessage();
                    } else {
                        //检测运行间隔
                        Thread.sleep(config.getCheckInterval());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "thread-receive").start();

    }

    /**
     * 接受报文消息
     */
    private void receiveMessage() {
        while (socketHelper.getSocket() == null) {
            if (!canRun) {
                return;
            }
            this.connect();
        }
        Socket socket = socketHelper.getSocket();


        String hostName = config.getHostName();
        String hostAddress = config.getHostAddress();
        int hostPort = config.getPort();
        String charset = config.getCharset();

        int headLength = config.getHeadLength();
        int maxSingleLength = config.getMaxSingleLength();
        int messageCodeLength = config.getMessageCodeLength();
        int macLength = config.getMacLength();

        String socketKey = socketHelper.getSocketKey();
        try {
            byte[] bytes = socketHelper.getReceivedBytes();
            if (bytes == null) {
                bytes = new byte[0];
            }
            InputStream input = socket.getInputStream();
            /**
             * 1、读取报文头
             */
            if (bytes.length < headLength) {
                byte[] headBytes = new byte[headLength - bytes.length];
                int couter = input.read(headBytes);
                if (couter < 0) {
                    logger.error("连接[{} --> {}-{}:{}]已关闭", new Object[]{socketKey, hostName, hostAddress, hostPort});
                    this.close();
                    return;
                }
                bytes = ArrayUtils.addAll(bytes, ArrayUtils.subarray(headBytes, 0, couter));
                if (couter < headBytes.length) {// 未满足长度位数，可能是粘包造成，保存读取到的
                    socketHelper.setReceivedBytes(bytes);
                    return;
                }
            }
            String headMsg = new String(ArrayUtils.subarray(bytes, 0, headLength), charset);
            int xmlLength = NumberUtils.toInt(headMsg);
            if (xmlLength <= 0 || xmlLength > maxSingleLength * 1024) {
                logger.error("连接[{} --> {}-{}:{}]出现账数据，自动断链：{}", new Object[]{socketKey, hostName, hostAddress, hostPort, headMsg});
                this.close();
                return;
            }
            /**
             * 2、读取报文体
             */
            int bodyLength = messageCodeLength + xmlLength + macLength;
            if (bytes.length < headLength + bodyLength) {
                byte[] bodyBytes = new byte[headLength + bodyLength - bytes.length];
                int couter = input.read(bodyBytes);
                if (couter < 0) {
                    logger.error("连接[{} --> {}-{}:{}]已关闭", new Object[]{socketKey, hostName, hostAddress, hostPort});
                    this.close();
                    return;
                }
                bytes = ArrayUtils.addAll(bytes, ArrayUtils.subarray(bodyBytes, 0, couter));
                if (couter < bodyBytes.length) {// 未满足长度位数，可能是粘包造成，保存读取到的
                    socketHelper.setReceivedBytes(bytes);
                    return;
                }
            }
            byte[] bodyBytes = ArrayUtils.subarray(bytes, headLength, headLength + bodyLength);
            String bodyMsg = new String(bodyBytes, charset);
            logger.info("本地 <<=== 对端[{}-{}:{}] ## {}", new Object[]{hostName, hostAddress, hostPort, bodyMsg});
            receiveQueue.put(bodyBytes);

            bytes = ArrayUtils.subarray(bytes, headLength + bodyLength, bytes.length);
            socketHelper.setReceivedBytes(bytes);
        } catch (Exception e) {
            logger.error("从对端[{}-{}:{}]接收报文出现异常", new Object[]{hostName, hostAddress, hostPort});
            logger.error(e.getLocalizedMessage(), e);
            this.close();
        }

    }

    /**
     * 发送报文消息
     */
    private void sendMessage() {
        while (socketHelper.getSocket() == null) {
            if (!canRun) {
                return;
            }
            this.connect();
        }

        String hostName = config.getHostName();
        String hostAddress = config.getHostAddress();
        int hostPort = config.getPort();
        String charset = config.getCharset();

        byte[] bytes = null;
        try {
            bytes = sendQueue.take();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (bytes == null || bytes.length < 1) {
            return;
        }
        try {
            Socket socket = socketHelper.getSocket();
            if (socket == null) {
                sendQueue.put(bytes);
                //sendQueue.offer(bytes);
                return;
            }

            logger.info("本地 ===>> 对端[{}-{}:{}] ## {}", new Object[] { hostName, hostAddress, hostPort, new String(bytes, charset) });

            OutputStream os = socket.getOutputStream();
            os.write(bytes);
            os.flush();
            socketHelper.setLastActiveTime(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            this.close();

            try {
                sendQueue.put(bytes);
            } catch (Exception e1) {
                logger.error("向对端[{}-{}:{}]发送报文出现异常", new Object[] { hostName, hostAddress, hostPort });
                e1.printStackTrace();
            }
        }
    }

    /**
     * connect to socket server
     */
    private synchronized void connect() {

        //发送缓冲区容量，单位：字节
        int sendBufferSize = config.getSendBufferSize();
        //接收缓冲区容量，单位：字节
        int receiveBufferSize = config.getReceiveBufferSize();

        for (int i = 0; i < config.getRetryTimes(); i++) {

            try {
                Socket socket = socketHelper.getSocket();
                if (null != socket) {
                    if (socket.isConnected()) {
                        return;
                    } else {
                        this.close();
                    }
                }
                socket = new Socket();
                socket.setKeepAlive(true);
                socket.setTcpNoDelay(true);
                socket.setSendBufferSize(sendBufferSize);
                socket.setReceiveBufferSize(receiveBufferSize);

                socket.connect(new InetSocketAddress(config.getHostAddress(), config.getPort()), config.getConnectTimeout());

                /**
                 * 保存数据对象
                 */
                socketHelper.setSocket(socket);
                socketHelper.setSocketKey(socket.getRemoteSocketAddress() + ":" + socket.getPort());
                socketHelper.setLastActiveTime(new Date());
                socketHelper.setReceivedBytes(null);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 心跳检测
     */
    private void heartbeatCheck() {
        while (null == socketHelper.getSocket()) {
            if (!canRun) {
                return;
            }
            this.connect();
        }
        //发送心跳
        try {
            Socket socket = socketHelper.getSocket();

            if (socket.isConnected()) {
                System.out.println("最后心跳检测时间：" + DateFormatUtil.format(socketHelper.getLastActiveTime()));
                long intervalTime = new Date().getTime() - socketHelper.getLastActiveTime().getTime();
                if (intervalTime > config.getHeartBeatInterval()) {
                    String heartbeatMessage = config.getHeartBeatMsg();// 心跳报文:000000
                    String charset = config.getCharset();// 字符集:UTF-8

                    socketHelper.setLastActiveTime(new Date());
                    OutputStream output = socket.getOutputStream();
                    output.write(heartbeatMessage.getBytes(charset));
                    output.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.close();
        }
    }


    /**
     * 关闭
     */
    private void close() {
        // 清除已经保存的粘包块
        socketHelper.setReceivedBytes(null);
        Socket socket = socketHelper.getSocket();
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        socketHelper.setSocket(null);
        socketHelper.setLastActiveTime(null);
    }
}
