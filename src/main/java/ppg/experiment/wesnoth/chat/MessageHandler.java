package ppg.experiment.wesnoth.chat;

import io.netty.channel.Channel;

public interface MessageHandler {
    boolean handles(String root);

    void handle(String msg, Channel c);
}
