package ppg.experiment.wesnoth.chat.parser;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * This software and all files contained in it are distrubted under the MIT license.
 * 
 * Copyright (c) 2013 Cogito Learning Ltd
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
