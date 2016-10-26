package ppg.experiment.wesnoth.chat;

import io.netty.channel.Channel;
import ppg.experiment.wesnoth.chat.wml.WMLMessage;

public abstract class WhisperMessageHandler implements MessageHandler {

    @Override
    public boolean handles(WMLMessage message) {
        return "[whisper]".equals(message.getNode());
    }

    @Override
    public void handle(WMLMessage msg, Channel c) {
        showWhisperedMessage(msg.getAttribute("sender"),
                msg.getAttribute("message"));
    }

    public abstract void showWhisperedMessage(String sender, String message);
}
