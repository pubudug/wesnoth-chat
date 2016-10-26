package ppg.experiment.wesnoth.chat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.channel.Channel;
import ppg.experiment.wesnoth.chat.wml.WMLMessage;

public class IgnoreMessageHandler implements MessageHandler {
    private static final Logger LOGGER = LogManager
            .getLogger(IgnoreMessageHandler.class);

    @Override
    public boolean handles(WMLMessage message) {
        return true;
    }

    @Override
    public void handle(WMLMessage msg, Channel c) {
        LOGGER.warn("Message {} ignored!", msg);
    }

}
