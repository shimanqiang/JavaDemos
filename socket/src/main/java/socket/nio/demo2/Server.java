package socket.nio.demo2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by smq on 2017/6/5.
 */
public class Server {

    private static ServerHandler serverHandler;

    public static void main(String[] args) {
        serverHandler = new ServerHandler();

        if (null == serverHandler) {
            serverHandler.stop();
        }

        new Thread(serverHandler, "Server Nio").start();

    }
}



