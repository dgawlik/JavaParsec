package org.jparsec.test;

import org.jparsec.combinator.Chars;
import org.jparsec.containers.*;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.jparsec.combinator.Choice.choice;
import static org.jparsec.combinator.Strings.string;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ChoiceTest {

    @Test
    public void test_id(){
        var polymorphic = choice(
                string("hello"),
                Chars.any(),
                string("true").map(Boolean::valueOf),
                string("1").map(Integer::valueOf),
                string("1.2").map(Double::valueOf)
        );

        Function<ParseContext, Empty> test = (ctx) -> {
            switch(polymorphic.parse(ctx)) {
                case Ok(One(String v), _) -> assertEquals("hello", v);
                case Ok(Two(Character c), _) -> {}
                case Ok(Three(Boolean b), _) -> assertEquals(true,  b);
                case Ok(Four(Integer i), _) -> assertEquals(1, (int) i);
                case Ok(Five(Double d), _) -> assertEquals(1.2, (double) d);
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

}