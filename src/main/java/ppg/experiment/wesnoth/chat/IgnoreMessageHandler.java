package ppg.experiment.wesnoth.chat;

import io.netty.channel.Channel;
import ppg.experiment.wesnoth.chat.wml.WMLMessage;

public class IgnoreMessageHandler implements MessageHandler {

    @Override
    public boolean handles(WMLMessage message) {
        return true;
    }

    @Override
    public void handle(WMLMessage msg, Channel c) {
        System.out.println("Message " + msg + " ignored!");
    }

}
