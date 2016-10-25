package ppg.experiment.wesnoth.chat;

import io.netty.channel.Channel;
import ppg.experiment.wesnoth.chat.wml.WMLMessage;

public class MissingMessageHandler implements MessageHandler {

    @Override
    public boolean handles(WMLMessage message) {
        return true;
    }

    @Override
    public void handle(WMLMessage msg, Channel c) {
        throw new RuntimeException("No handler found for message " + msg);
    }

}
