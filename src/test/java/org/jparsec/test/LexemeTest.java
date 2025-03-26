package org.jparsec.test;

import org.jparsec.combinator.Chars;
import org.jparsec.combinator.Lexeme;
import org.jparsec.combinator.Whitespace;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.jparsec.containers.ParseContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LexemeTest {

    @Test
    public void lexeme_test(){
        var ch = Chars.anyOf('a');
        var ws = Whitespace.spaces(Whitespace.Config.defaults());
        var lexeme = Lexeme.lexeme(ch, ws);

        var ctx1 = ParseContext.of("a");
        var result = lexeme.parse(ctx1);

        if (result instanceof Ok(Character c, ParseContext resCtx)){
            Assertions.assertEquals('a', (char) c);
            Assertions.assertEquals(resCtx.content.length(), resCtx.index);
        } else {
            Assertions.fail();
        }

        var ctx2 = ParseContext.of("a \n");
        var result2 = lexeme.parse(ctx2);

        if (result2 instanceof Ok(Character c, ParseContext resCtx)){
            Assertions.assertEquals('a', (char) c);
            Assertions.assertEquals(resCtx.content.length(), resCtx.index);
        } else {
            Assertions.fail();
        }

        var ctx3 = ParseContext.of("b \n");
        var result3 = lexeme.parse(ctx3);

        if (result3 instanceof Err(String msg, ParseContext resCtx)){
            Assertions.assertEquals("a could not be matched", msg);
            Assertions.assertEquals(0, resCtx.index);
        } else {
            Assertions.fail();
        }
    }
}
