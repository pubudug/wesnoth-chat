package ppg.experiment.wesnoth.chat.handler;

import java.util.List;

import io.netty.channel.Channel;
import ppg.experiment.wesnoth.chat.wml.WMLMessage;
import ppg.experiment.wesnoth.chat.wml.WMLNode;

public abstract class GameListDiffMessageHandler implements MessageHandler {

    @Override
    public boolean handles(WMLMessage message) {
        return "[gamelist_diff]".equals(message.getNode());
    }

    @Override
    public void handle(WMLMessage msg, Channel c) {
        List<WMLNode> insertTags = msg.getChildren("[insert_child]");
        for (WMLNode insertTag : insertTags) {
            List<WMLNode> users = insertTag.getChildren("[user]");
            for (WMLNode user : users) {
                addUser(Integer.parseInt(insertTag.getAttribute("index")),
                        user.getAttribute("name"));
            }
        }

        List<WMLNode> deleteTags = msg.getChildren("[delete_child]");
        for (WMLNode deleteTag : deleteTags) {
            List<WMLNode> users = deleteTag.getChildren("[user]");
            for (@SuppressWarnings("unused") WMLNode user : users) {
                removeUser(Integer.parseInt(deleteTag.getAttribute("index")));
            }
        }
    }

    public abstract void removeUser(int index);

    public abstract void addUser(int index, String name);
}
