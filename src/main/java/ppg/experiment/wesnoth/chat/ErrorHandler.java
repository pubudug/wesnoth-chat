package ppg.experiment.wesnoth.chat;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import ppg.experiment.wesnoth.chat.wml.WMLMessage;

public abstract class ErrorHandler implements MessageHandler {

    @Override
    public boolean handles(WMLMessage message) {
        return "[error]".equals(message.getNode());
    }

    @Override
    public void handle(WMLMessage msg, Channel c) {
        if ("101".equals(msg.getAttribute("error_code"))) {
            String string = "[login]\n\tusername=\""
                    + requestDifferentNick(msg.getAttribute("message"))
                    + "\"\n[/logn]\n\n";
            byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
            ByteBuf buf = c.alloc().buffer(bytes.length);
            buf.writeBytes(bytes);
            c.writeAndFlush(buf);
        }
    }

    public abstract String requestDifferentNick(String message);
}
