package ppg.experiment.wesnoth.chat;

import java.util.LinkedList;
import java.util.List;

import ppg.experiment.wesnoth.chat.parser.Token;
import ppg.experiment.wesnoth.chat.parser.TokenCallback;
import ppg.experiment.wesnoth.chat.parser.Tokenizer;
import ppg.experiment.wesnoth.chat.wml.WMLMessage;
import ppg.experiment.wesnoth.chat.wml.WMLNode;

public class WMLMessageBuilder implements TokenCallback {

    private List<WMLMessage> wmlMessage;
    private WMLNode currentNode;

    public WMLMessageBuilder() {
        wmlMessage = new LinkedList<>();
    }

    @Override
    public void foundToken(Token token) {
        if (token.getToken() == Tokenizer.START_TAG) {
            if (this.currentNode == null) {
                WMLMessage message = new WMLMessage(token);
                this.currentNode = message;
                wmlMessage.add(message);
            } else {
                WMLNode node = new WMLNode(token.getSequence(), currentNode);
                currentNode.addChild(node);
                currentNode = node;
            }
        } else if (token.getToken() == Tokenizer.END_TAG) {
            if (currentNode.getParent() != null) {
                currentNode = currentNode.getParent();
            } else {
                this.currentNode = null;
            }
        } else if (token.getToken() == Tokenizer.ATTRIBUTE_NAME_AND_VALUE) {
            if (currentNode != null) {
                String[] parts = token.getSequence().trim().split("=");
                currentNode.addAttribute(parts[0],
                        parts[1].substring(1, parts[1].length() - 1));
            } else {
                // ignore ping message
            }
        }
    }

    public List<WMLMessage> getWMLMessages() {
        return wmlMessage;
    }
}
