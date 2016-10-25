package ppg.experiment.wesnoth.chat;

import java.io.FileOutputStream;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.LengthFieldPrepender;

public class ConnectionInitializer extends ChannelInboundHandlerAdapter {

    public ConnectionInitializer() {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Channel active!");
        ByteBuf buf = ctx.alloc().buffer(4);
        buf.writeBytes(new byte[] { 0, 0, 0, 0 });
        ctx.channel().writeAndFlush(buf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        ByteBuf b = (ByteBuf) msg;
        int connectionNumber = b.readInt();
        System.out.println("Connection number: " + connectionNumber);
        ctx.pipeline().remove(this);

        ctx.pipeline().addLast(new LengthFieldPrepender(4));

        ctx.pipeline().addLast(new ChannelOutboundHandlerAdapter() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg,
                    ChannelPromise promise) throws Exception {
                ByteBuf buf = (ByteBuf) msg;
                ByteBuf c = buf.copy();
                byte[] bytes = new byte[c.readableBytes()];
                c.readBytes(bytes);
                FileOutputStream stream = new FileOutputStream(
                        "/home/pubudu/tmp/blah");
                try {
                    stream.write(bytes);
                } finally {
                    stream.close();
                }
                super.write(ctx, msg, promise);
            }
        });
        ctx.pipeline().addLast(new GzipEncoder());
        ctx.fireChannelRead(msg);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
