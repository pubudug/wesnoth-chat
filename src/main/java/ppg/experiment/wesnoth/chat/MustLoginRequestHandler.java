package ppg.experiment.wesnoth.chat;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import ppg.experiment.wesnoth.chat.wml.WMLMessage;

public class MustLoginRequestHandler implements MessageHandler {

    @Override
    public boolean handles(WMLMessage message) {
        return "[mustlogin]".contentEquals(message.getNode());
    }

    @Override
    public void handle(WMLMessage msg, Channel c) {
        String string = "[login]\n\tusername=\"ugudu\"\n[/logn]\n\n";
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        ByteBuf buf = c.alloc().buffer(bytes.length);
        buf.writeBytes(bytes);
        c.writeAndFlush(buf);

    }

}
