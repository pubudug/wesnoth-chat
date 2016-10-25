package ppg.experiment.wesnoth.chat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import ppg.experiment.wesnoth.chat.wml.WMLMessage;

public class VersionRequestHandler implements MessageHandler {

    private static final Logger logger = LogManager
            .getLogger(VersionRequestHandler.class);

    @Override
    public boolean handles(WMLMessage message) {
        return "[version]".equals(message.getNode());
    }

    @Override
    public void handle(WMLMessage msg, Channel c) {
        System.out.println("Got message " + msg);
        String string = "[version]\nversion=1.12.6\n[/version]";
        byte[] bytes = string.getBytes();
        ByteBuf buf = c.alloc().buffer(bytes.length);
        buf.writeBytes(bytes);
        c.writeAndFlush(buf);
    }
}
