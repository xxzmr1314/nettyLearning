package echoDemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

/**
 * 这是一个处理器
 * 适配器模式ChannelInboundHandlerAdapter 比 ChannelInboundHandler比，多了方法的默认实现，我们只需要去重写我们需要的方法就可以了
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //直接从管道写出数据
//        Channel channel = ctx.channel();
//        channel.writeAndFlush(Unpooled.copiedBuffer("EchoClient channelReadCompleate",CharsetUtil.UTF_8));

        //通过pipeline写出数据
        ChannelPipeline channelPipeline = ctx.pipeline();
        channelPipeline.writeAndFlush(Unpooled.copiedBuffer("EchoClient channelReadCompleate",CharsetUtil.UTF_8));

        //通过context写出数据此方法只会在后续的Handler里面传播
//        ctx.writeAndFlush(data);

        ByteBuf data = (ByteBuf) msg;

        System.out.println("服务端收到数据："+data.toString(CharsetUtil.UTF_8));

        //通过context写出数据
        ctx.writeAndFlush(data);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("EchoServerHandler channelRead Compleate");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
