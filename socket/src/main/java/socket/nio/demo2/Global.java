package socket.nio.demo2;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by smq on 2017/6/5.
 */
public class Global {
    public static LinkedBlockingQueue<byte[]> sendQueue = new LinkedBlockingQueue<>();

    public static LinkedBlockingQueue<byte[]> getSendQueue() {
        return sendQueue;
    }
}
