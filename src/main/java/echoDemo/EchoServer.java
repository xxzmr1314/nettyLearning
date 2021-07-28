package echoDemo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.ArrayList;

public class EchoServer {
    private int port;

    public EchoServer(int port){
        this.port = port;
    }

    /**
     * 启动流程
     */
    public void run() throws InterruptedException {
        //配置服务端线程组 一个eventloopGroup维护多个eventloop,一个eventLoop维护一个selector，一个selector维护多个channel
        EventLoopGroup bossGroup = new NioEventLoopGroup();//默认线程池数量= cpu核数*2  可看成ktv前台接待，可以一个人接待多个，也可以多人接待多人
        EventLoopGroup workerGroup = new NioEventLoopGroup();//可看成ktv服务人员
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();//服务端启动类，用来帮我们开启netty
            serverBootstrap.group(bossGroup,workerGroup)//设置线程组，如果只设置一个的话，就是一个人又干接待的活又干服务的活
                    .channel(NioServerSocketChannel.class)//设置channel类型
                    .option(ChannelOption.SO_BACKLOG,1024)//用于每个新进来的channel，设置TCP连接中的一些参数，这里是设置TCP三次握手的等待队列的最大长度，即客户端第一次访问服务端时被服务端保存的请求以及客户端第二次访问服务端被保存的请求的总和
                    .option(ChannelOption.TCP_NODELAY,true)//用于每个新进来的channel，设置TCP连接中的一些参数，这是为了解决Nagel算法的问题，就是每次尽量发送大块的数据，这样会导致每次都要等网络包堆积一下再一次性发送，实时性弱。设置成true就是关闭Nagel算法
                    .childHandler(new ChannelInitializer<SocketChannel>() {//加入过滤处理链
                        protected void initChannel(SocketChannel ch) throws Exception {//设置channelHandler需要注册到pipeline中
                            ch.pipeline().addLast(new EchoServerHandler());//执行顺序： Inbound顺序执行 Outbound逆序执行
                        }
                    });

            System.out.println("Echo 服务器启动inglallala");
            //绑定端口，同步等待
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            //等待服务端监听端口关闭
            channelFuture.channel().closeFuture().sync();
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }


    }

    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        if (args.length >0){
            port = Integer.parseInt(args[0]);
        }
        ArrayList<Integer> array = new ArrayList<Integer>();

        new EchoServer(port).run();
    }
}
