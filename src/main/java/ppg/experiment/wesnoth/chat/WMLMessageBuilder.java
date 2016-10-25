package ppg.experiment.wesnoth.chat;

import ppg.experiment.wesnoth.chat.parser.Token;
import ppg.experiment.wesnoth.chat.parser.TokenCallback;
import ppg.experiment.wesnoth.chat.parser.Tokenizer;
import ppg.experiment.wesnoth.chat.wml.WMLMessage;
import ppg.experiment.wesnoth.chat.wml.WMLNode;

public class WMLMessageBuilder implements TokenCallback {

    private WMLMessage wmlMessage;
    private WMLNode currentNode;

    @Override
    public void foundToken(Token token) {
        if (this.wmlMessage == null
                && token.getToken() == Tokenizer.START_TAG) {
            this.wmlMessage = new WMLMessage(token);
            this.currentNode = this.wmlMessage;
        } else if (token.getToken() == Tokenizer.END_TAG) {
            if (currentNode.getParent() != null) {
                currentNode = currentNode.getParent();
            }
        } else {
            throw new RuntimeException(
                    "Unexpected element at beginning : " + token);
        }

    }

    public WMLMessage getWMLMessage() {
        return wmlMessage;
    }
}
