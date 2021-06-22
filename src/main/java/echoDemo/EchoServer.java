package echoDemo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {
    private int port;

    public EchoServer(int port){
        this.port = port;
    }

    /**
     * 启动流程
     */
    public void run() throws InterruptedException {
        //配置服务端线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();//用来帮我们开启netty
        serverBootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {//建立连接之后的处理，

                    }
                });

        System.out.println("Echo 服务器启动inglallala");
        //绑定端口，同步等待
        ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

    }

    public static void main(String[] args) {
        int port = 8080;
        if (args.length >0){
            port = Integer.parseInt(args[0]);
        }

        new EchoServer(port).run();
    }
}
