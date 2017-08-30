package socket.nio.demo1;

/**
 * 测试方法
 *
 * @author yangtao__anxpp.com
 * @version 1.0
 */
public class Test2 {
    //测试主方法  
    @SuppressWarnings("resource")
    public static void main(String[] args) throws Exception {
        //运行服务器  
        //Server.start();
        //避免客户端先于服务器启动前执行代码  
        //Thread.sleep(1000);
        //运行客户端   
        Client.start();
       // while (Client.sendMsg("1233")) ;
    }
}  