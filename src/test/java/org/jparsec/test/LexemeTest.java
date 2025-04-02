package org.jparsec.test;

import org.jparsec.combinator.Lexeme;
import org.jparsec.containers.Context;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.jparsec.Api.*;

public class LexemeTest {

    @Test
    public void lexeme_test(){
        var ch = anyOf('a');
        var ws = spaces();
        var lexeme = lexeme(ch, ws);

        var ctx1 = Context.of("a");
        var result = lexeme.parse(ctx1);

        if (result instanceof Ok(Character c, Context resCtx)){
            Assertions.assertEquals('a', (char) c);
            Assertions.assertEquals(resCtx.content.length(), resCtx.index);
        } else {
            Assertions.fail();
        }

        var ctx2 = Context.of("a \n");
        var result2 = lexeme.parse(ctx2);

        if (result2 instanceof Ok(Character c, Context resCtx)){
            Assertions.assertEquals('a', (char) c);
            Assertions.assertEquals(resCtx.content.length(), resCtx.index);
        } else {
            Assertions.fail();
        }

        var ctx3 = Context.of("b \n");
        var result3 = lexeme.parse(ctx3);

        if (result3 instanceof Err(String msg, Context resCtx)){
            Assertions.assertEquals("expected 'a'", msg);
            Assertions.assertEquals(0, resCtx.index);
        } else {
            Assertions.fail();
        }
    }
}
