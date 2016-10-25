package ppg.experiment.wesnoth.chat.parser;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

import java.util.List;

import org.mockito.ArgumentCaptor;
import org.testng.annotations.Test;

public class TokenizerTest {

    @Test
    public void testVersionRequst() {
        String text = "[version]\n [/version]";
        Tokenizer t = new Tokenizer();
        TokenCallback mock = mock(TokenCallback.class);
        t.tokenize(text, mock);

        ArgumentCaptor<Token> argumentCaptor = ArgumentCaptor
                .forClass(Token.class);
        verify(mock, times(2)).foundToken(argumentCaptor.capture());

        List<Token> values = argumentCaptor.getAllValues();
        assertEquals(values.get(0).getSequence(), "[version]");
        assertEquals(values.get(1).getSequence(), "[/version]");

    }

    @Test
    public void testLoginResponse() {
        String text = "[gamelist][/gamelist]"
                + "[user]available=\"yes\"game_id=\"0\"location=\"\"name=\"ported\"registered=\"no\"status=\"lobby\"[/user]"
                + "[user]available=\"yes\"game_id=\"0\"location=\"\"name=\"ugudu\"registered=\"no\"status=\"lobby\"[/user]\r\n";
        Tokenizer t = new Tokenizer();
        TokenCallback mock = mock(TokenCallback.class);
        t.tokenize(text, mock);

        ArgumentCaptor<Token> argumentCaptor = ArgumentCaptor
                .forClass(Token.class);
        verify(mock, times(42)).foundToken(argumentCaptor.capture());

        List<Token> values = argumentCaptor.getAllValues();
        assertEquals(values.get(0).getSequence(), "[gamelist]");
        assertEquals(values.get(1).getSequence(), "[/gamelist]");
        assertEquals(values.get(2).getSequence(), "[user]");
        assertEquals(values.get(3).getSequence(), "available");
        assertEquals(values.get(4).getSequence(), "=");
        assertEquals(values.get(5).getSequence(), "\"yes\"");

    }

}
