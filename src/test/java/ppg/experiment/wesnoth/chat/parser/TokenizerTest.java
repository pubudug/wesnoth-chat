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
        assertEquals(values.get(Tokenizer.START_TAG).getSequence(), "[version]");
        assertEquals(values.get(Tokenizer.END_TAG).getSequence(), "[/version]");

    }
}
