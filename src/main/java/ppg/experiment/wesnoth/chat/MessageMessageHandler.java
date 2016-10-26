package ppg.experiment.wesnoth.chat;

import io.netty.channel.Channel;
import ppg.experiment.wesnoth.chat.wml.WMLMessage;

public abstract class MessageMessageHandler implements MessageHandler {

    @Override
    public boolean handles(WMLMessage message) {
        return "[message]".equals(message.getNode());
    }

    @Override
    public void handle(WMLMessage msg, Channel c) {
        showMessage(msg.getAttribute("sender"), msg.getAttribute("message"));
    }

    public abstract void showMessage(String sender, String message);

}
