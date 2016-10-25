package ppg.experiment.wesnoth.chat;

import io.netty.channel.Channel;
import ppg.experiment.wesnoth.chat.wml.WMLMessage;

public class GameListMessageHandler implements MessageHandler {

    @Override
    public boolean handles(WMLMessage message) {
        return "[gamelist]".equals(message.getNode());
    }

    @Override
    public void handle(WMLMessage msg, Channel c) {
        // Do nothing
    }

}
