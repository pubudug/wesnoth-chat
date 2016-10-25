package ppg.experiment.wesnoth.chat;

import java.nio.charset.StandardCharsets;

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
        String log = "Got message " + msg;
        System.out.println(log);
        String string = "[version]\n\tversion=\"1.12.6\"\n[/version]\n\n";
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        ByteBuf buf = c.alloc().buffer(bytes.length);
        buf.writeBytes(bytes);
        c.writeAndFlush(buf);
    }
}
