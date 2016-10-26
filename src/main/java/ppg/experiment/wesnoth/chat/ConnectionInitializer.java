package ppg.experiment.wesnoth.chat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.LengthFieldPrepender;
import ppg.experiment.wesnoth.chat.codec.GzipEncoder;

public class ConnectionInitializer extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LogManager
            .getLogger(ConnectionInitializer.class);

    public ConnectionInitializer() {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("Channel active!");
        ByteBuf buf = ctx.alloc().buffer(4);
        buf.writeBytes(new byte[] { 0, 0, 0, 0 });
        ctx.channel().writeAndFlush(buf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        ByteBuf b = (ByteBuf) msg;
        int connectionNumber = b.readInt();
        LOGGER.info("Connection number: {}", connectionNumber);
        ctx.pipeline().remove(this);

        ctx.pipeline().addLast(new LengthFieldPrepender(4));

        ctx.pipeline().addLast(new GzipEncoder());
        ctx.fireChannelRead(msg);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        LOGGER.error(cause.getMessage(), cause);
        ctx.close();
    }
}
