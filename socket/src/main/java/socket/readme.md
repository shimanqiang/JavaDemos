## 1、demo
http://blog.csdn.net/anxpp/article/details/51512200

问题：需要在client端，bug处理，开启非阻塞模式前 做连接操作
```
socketChannel.connect(new InetSocketAddress(host, port));
socketChannel.configureBlocking(false);//开启非阻塞模式

```


## 2、粘包、断包问题
http://blog.csdn.net/nsdhy/article/details/24249741

https://my.oschina.net/u/2397619/blog/494159

http://blog.csdn.net/scythe666/article/details/51996268


