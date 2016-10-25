package ppg.experiment.wesnoth.chat;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MessageBus extends ChannelInboundHandlerAdapter {

    private List<MessageHandler> messageHandlers;

    public MessageBus(List<MessageHandler> messageHandler) {
        super();
        messageHandlers = messageHandler;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        ByteBuf b = (ByteBuf) msg;
        byte[] dst = new byte[b.readableBytes()];
        b.readBytes(dst);
        System.out.println(new String(dst));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
