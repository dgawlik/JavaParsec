package org.jparsec.test;

import org.jparsec.combinator.Chars;
import org.jparsec.combinator.Opt;
import org.jparsec.containers.Ok;
import org.jparsec.containers.ParseContext;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class OptTest {

    @Test
    public void test_optional() {
        var optA = Opt.opt(Chars.anyOf('a'));

        var ctx1 = ParseContext.of("a");
        var result = optA.parse(ctx1);
        if (result instanceof Ok(Optional<Character> oC, ParseContext newCtx)){
            assertTrue(oC.isPresent());
            assertEquals('a', (char)oC.get());
            assertEquals(newCtx.content.length(), newCtx.index);
        } else {
            fail();
        }

        var ctx2 = ParseContext.of("b");
        result = optA.parse(ctx2);
        if (result instanceof Ok(Optional<Character> oC, ParseContext newCtx)){
            assertTrue(oC.isEmpty());
            assertEquals(0, newCtx.index);
        } else {
            fail();
        }
    }

}