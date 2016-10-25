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
        if (this.currentNode == null
                && token.getToken() == Tokenizer.START_TAG) {
            WMLMessage message = new WMLMessage(token);
            this.currentNode = message;
            wmlMessage.add(message);
        } else if (token.getToken() == Tokenizer.END_TAG) {
            if (currentNode.getParent() != null) {
                currentNode = currentNode.getParent();
            } else {
                this.currentNode = null;
            }
        }
    }

    public List<WMLMessage> getWMLMessages() {
        return wmlMessage;
    }
}
