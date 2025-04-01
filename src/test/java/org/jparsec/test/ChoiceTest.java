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

        Function<ParseContext, Empty> test = (ctx) -> {
            switch(polymorphic.parse(ctx)) {
                case Ok(One(String v), ParseContext c) -> assertEquals("hello", v);
                case Ok(Two(Character c), ParseContext ct) -> {}
                case Ok(Three(Boolean b), ParseContext c) -> assertEquals(true,  b);
                case Ok(Four(Integer i), ParseContext c) -> assertEquals(1, (int) i);
                case Ok(Five(Double d), ParseContext c) -> assertEquals(1.2, (double) d);
                default -> fail();
            }
            return new Empty();
        };

        test.apply(ParseContext.of("hello"));
        test.apply(ParseContext.of("c"));
        test.apply(ParseContext.of("true"));
        test.apply(ParseContext.of("1"));
        test.apply(ParseContext.of("1.2"));

    }

    @Test
    public void test_uniform_choice() {
        var m = any(c('a'), c('b'), c('c'), c('d'));

        var r = m.parse("b");
        assertEquals('b', (char) r.ok());
    }

}