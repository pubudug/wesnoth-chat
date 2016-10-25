package ppg.experiment.wesnoth.chat.parser;

public class Token {
    private final int token;
    private final String sequence;

    public Token(int token, String sequence) {
        super();
        this.token = token;
        this.sequence = sequence;
    }

    public int getToken() {
        return token;
    }

    public String getSequence() {
        return sequence;
    }

}