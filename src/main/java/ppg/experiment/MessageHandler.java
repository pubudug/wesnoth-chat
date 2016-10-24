package ppg.experiment;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MessageHandler extends ChannelInboundHandlerAdapter {

    public MessageHandler() {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Channel active!");
        ByteBuf buf = ctx.alloc().buffer();
        buf.writeBytes(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 });
        ctx.channel().writeAndFlush(buf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        System.out.println(msg);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
            throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
