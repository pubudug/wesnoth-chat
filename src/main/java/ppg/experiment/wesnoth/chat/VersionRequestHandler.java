package ppg.experiment.wesnoth.chat;

import io.netty.channel.Channel;

public class VersionRequestHandler implements MessageHandler {

    @Override
    public boolean handles(String root) {
        return "version".equals(root);
    }

    @Override
    public void handle(String msg, Channel c) {

    }
}
