package ppg.experiment.wesnoth.chat.handler;

import io.netty.channel.Channel;
import ppg.experiment.wesnoth.chat.wml.WMLMessage;

public abstract class UserMessageHandler implements MessageHandler {

    @Override
    public boolean handles(WMLMessage message) {
        return "[user]".equals(message.getNode());
    }

    @Override
    public void handle(WMLMessage msg, Channel c) {
        addUser(msg.getAttribute("name"));
    }

    public abstract void addUser(String name);

}
