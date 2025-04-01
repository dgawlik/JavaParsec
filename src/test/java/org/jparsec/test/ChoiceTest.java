package org.jparsec.test;

import org.jparsec.Api;
import org.jparsec.containers.*;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.jparsec.combinator.Choice.choice;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.jparsec.Api.*;

class ChoiceTest {

    @Test
    public void test_id(){
        var polymorphic = choice(
                Api.c("hello"),
                anyChar(),
                Api.c("true").map(Boolean::valueOf),
                Api.c("1").map(Integer::valueOf),
                Api.c("1.2").map(Double::valueOf)
        );

        Function<Context, Empty> test = (ctx) -> {
            switch(polymorphic.parse(ctx)) {
                case Ok(One(String v), Context c) -> assertEquals("hello", v);
                case Ok(Two(Character c), Context ct) -> {}
                case Ok(Three(Boolean b), Context c) -> assertEquals(true,  b);
                case Ok(Four(Integer i), Context c) -> assertEquals(1, (int) i);
                case Ok(Five(Double d), Context c) -> assertEquals(1.2, (double) d);
                default -> fail();
            }
            return new Empty();
        };

        test.apply(Context.of("hello"));
        test.apply(Context.of("c"));
        test.apply(Context.of("true"));
        test.apply(Context.of("1"));
        test.apply(Context.of("1.2"));

    }

    @Test
    public void test_uniform_choice() {
        var m = any(c('a'), c('b'), c('c'), c('d'));

        var r = m.parse("b");
        assertEquals('b', (char) r.ok());
    }

}