package ppg.experiment.wesnoth.chat.handler;

import io.netty.channel.Channel;
import ppg.experiment.wesnoth.chat.wml.WMLMessage;

public interface MessageHandler {
    boolean handles(WMLMessage message);

    void handle(WMLMessage msg, Channel c);
}
