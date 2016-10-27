package ppg.experiment.wesnoth.chat.handler;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import ppg.experiment.wesnoth.chat.wml.WMLMessage;

public abstract class VersionRequestHandler implements MessageHandler {

    @Override
    public boolean handles(WMLMessage message) {
        return "[version]".equals(message.getNode());
    }

    @Override
    public void handle(WMLMessage msg, Channel c) {
        String string = "[version]\n\tversion=\"" + getVersion()
                + "\"\n[/version]\n\n";
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        ByteBuf buf = c.alloc().buffer(bytes.length);
        buf.writeBytes(bytes);
        c.writeAndFlush(buf);
    }

    public abstract String getVersion();
}
