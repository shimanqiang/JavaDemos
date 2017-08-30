package socket.nio.demo3;

/**
 * Created by smq on 2017/6/5.
 */
public class SocketConfig {
    private int poolSize = 1;//线程池大小

    private String hostName;//主机名称
    private String hostAddress;//主机IP
    private int port;//主机端口号

    private int sendBufferSize = 200 * 1024;// 发送缓冲区容量，单位：字节
    private int receiveBufferSize = 200 * 1024;// 接收缓冲区容量，单位：字节

    private int connectTimeout = 30 * 1000;//连接超时时间，单位秒
    private int heartBeatInterval = 30 * 1000;//心跳间隔时间，单位秒
    private int checkInterval = 1 * 60 * 1000;//运行检查时间，单位秒

    private int retryInterval = 10; //重试间隔，单位秒
    private int retryTimes = 3;//重试次数

    private String charset = "UTF-8";//字符集

    private String heartBeatMsg = "000000";//心跳包内容
    private int headLength = 6;//报文头长度位数
    private int messageCodeLength = 15;// 报文码长度
    private int macLength = 32;// MAC长度
    private int maxSingleLength = 200 * 1024;// 单个报文最大长度，单位：字节

    /**
     * 是否可以运行
     */
    private boolean canRun;


    public SocketConfig() {

    }

    public SocketConfig(String hostAddress, int port) {
        this.hostAddress = hostAddress;
        this.port = port;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public int getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(int retryInterval) {
        this.retryInterval = retryInterval;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getSendBufferSize() {
        return sendBufferSize;
    }

    public void setSendBufferSize(int sendBufferSize) {
        this.sendBufferSize = sendBufferSize;
    }

    public int getReceiveBufferSize() {
        return receiveBufferSize;
    }

    public void setReceiveBufferSize(int receiveBufferSize) {
        this.receiveBufferSize = receiveBufferSize;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public boolean isCanRun() {
        return canRun;
    }

    public void setCanRun(boolean canRun) {
        this.canRun = canRun;
    }

    public int getHeartBeatInterval() {
        return heartBeatInterval;
    }

    public void setHeartBeatInterval(int heartBeatInterval) {
        this.heartBeatInterval = heartBeatInterval;
    }

    public int getCheckInterval() {
        return checkInterval;
    }

    public void setCheckInterval(int checkInterval) {
        this.checkInterval = checkInterval;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getHeartBeatMsg() {
        return heartBeatMsg;
    }

    public void setHeartBeatMsg(String heartBeatMsg) {
        this.heartBeatMsg = heartBeatMsg;
    }

    public int getHeadLength() {
        return headLength;
    }

    public void setHeadLength(int headLength) {
        this.headLength = headLength;
    }

    public int getMessageCodeLength() {
        return messageCodeLength;
    }

    public void setMessageCodeLength(int messageCodeLength) {
        this.messageCodeLength = messageCodeLength;
    }

    public int getMacLength() {
        return macLength;
    }

    public void setMacLength(int macLength) {
        this.macLength = macLength;
    }

    public int getMaxSingleLength() {
        return maxSingleLength;
    }

    public void setMaxSingleLength(int maxSingleLength) {
        this.maxSingleLength = maxSingleLength;
    }
}
