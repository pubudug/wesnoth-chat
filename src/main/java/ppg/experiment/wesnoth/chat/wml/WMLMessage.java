package ppg.experiment.wesnoth.chat.wml;

import ppg.experiment.wesnoth.chat.parser.Token;

public class WMLMessage extends WMLNode {

    public WMLMessage(Token token) {
        super(token.getSequence(), null);
    }

    @Override
    public String toString() {
        return "WMLMessage [getNode()=" + getNode() + "]";
    }

}
