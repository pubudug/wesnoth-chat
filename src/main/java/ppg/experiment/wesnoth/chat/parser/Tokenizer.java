package ppg.experiment.wesnoth.chat.parser;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {

    public static int START_TAG = 0;
    public static int END_TAG = 1;
    public static int ATTRIBUTE_NAME_AND_VALUE = 2;
    public static int IGNORE = 3;

    private class TokenInfo {
        public final Pattern regex;
        public final int token;

        public TokenInfo(Pattern regex, int token) {
            super();
            this.regex = regex;
            this.token = token;
        }
    }

    private LinkedList<TokenInfo> tokenInfos;

    public Tokenizer() {
        tokenInfos = new LinkedList<TokenInfo>();
        add("\\[game\\].*?\\[/game\\]", IGNORE);//to avoid stackoverflow error
        add("\\[[a-z_]+?\\]", START_TAG);
        add("\\[/[a-z_]+?\\]", END_TAG);
        //\"(\"\")*
        //add("[a-z_]+=\".*?(\"\")*\"", ATTRIBUTE_NAME_AND_VALUE);
        add("[a-z_]+=\"(.*?(\"\")*.*?)*\"", ATTRIBUTE_NAME_AND_VALUE);
    }

    public void add(String regex, int token) {
        tokenInfos
                .add(new TokenInfo(Pattern.compile("^(" + regex + ")"), token));
    }

    public void tokenize(String str, TokenCallback callback) {
        String s = str.trim();
        while (!s.equals("")) {
            boolean match = false;
            for (TokenInfo info : tokenInfos) {
                Matcher m = info.regex.matcher(s);
                if (m.find()) {
                    match = true;
                    String tok = m.group().trim();
                    s = m.replaceFirst("").trim();
                    callback.foundToken(new Token(info.token, tok));
                    break;
                }
            }
            if (!match)
                throw new ParserException(
                        "Unexpected character in input: " + s);
        }
    }

}
