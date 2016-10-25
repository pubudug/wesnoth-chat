package ppg.experiment.wesnoth.chat;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ConnectionInitializer extends ChannelInboundHandlerAdapter {

    public ConnectionInitializer() {
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
        ByteBuf b = (ByteBuf) msg;
        int connectionNumber = b.readInt();
        System.out.println("Connection number: " + connectionNumber);
        ctx.pipeline().remove(this);
        ctx.fireChannelRead(msg);

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
