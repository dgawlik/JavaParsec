package org.jparsec.test;

import org.jparsec.containers.Ok;
import org.jparsec.containers.ParseContext;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;


import static org.jparsec.Api.*;
import static org.jparsec.combinator.Many.some;
import static org.jparsec.combinator.Sep.sepBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class SepTest {

    @Test
    public void test_correct() {
        var commaSep = sepBy(some(noneOf(',')).map(this::join), anyOf(','));

        var result = commaSep.parse(ParseContext.of("one,two,three"));
        if (result instanceof Ok(List<String> r, _)) {
            assertEquals("one", r.get(0));
            assertEquals("two", r.get(1));
            assertEquals("three", r.get(2));
        } else {
            fail();
        }
    }


    private String join(List<Character> lc) {
        return lc.stream().map(String::valueOf).collect(Collectors.joining());
    }

}