package org.jparsec.test;

import org.jparsec.combinator.Opt;
import org.jparsec.containers.Context;
import org.jparsec.containers.Ok;
import org.junit.jupiter.api.Test;
import static org.jparsec.Api.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class OptTest {

    @Test
    public void test_optional() {
        var optA = Opt.opt(anyOf('a'));

        var ctx1 = Context.of("a");
        var result = optA.parse(ctx1);
        if (result instanceof Ok(Optional<Character> oC, Context newCtx)){
            assertTrue(oC.isPresent());
            assertEquals('a', (char)oC.get());
            assertEquals(newCtx.content.length(), newCtx.index);
        } else {
            fail();
        }

        var ctx2 = Context.of("b");
        result = optA.parse(ctx2);
        if (result instanceof Ok(Optional<Character> oC, Context newCtx)){
            assertTrue(oC.isEmpty());
            assertEquals(0, newCtx.index);
        } else {
            fail();
        }
    }

}