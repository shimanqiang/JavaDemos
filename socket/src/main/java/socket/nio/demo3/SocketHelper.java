package socket.nio.demo3;

import java.net.Socket;
import java.util.Date;

/**
 * Created by smq on 2017/6/5.
 */
public class SocketHelper {
    /**
     * socket关键字
     */
    private String socketKey;

    /**
     * socket对象
     */
    private Socket socket;


    /**
     * 最后一次的活动时间
     */
    private Date lastActiveTime = new Date();


    /**
     * 已经接受的粘包块
     */
    private byte[] receivedBytes;


    public String getSocketKey() {
        return socketKey;
    }

    public void setSocketKey(String socketKey) {
        this.socketKey = socketKey;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Date getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(Date lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    public byte[] getReceivedBytes() {
        return receivedBytes;
    }

    public void setReceivedBytes(byte[] receivedBytes) {
        this.receivedBytes = receivedBytes;
    }
}
