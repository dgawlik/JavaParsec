package org.jparsec.test;

import org.jparsec.combinator.Many;
import org.jparsec.combinator.Strings;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.jparsec.containers.ParseContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ManyTest {

    @Test
    public void test_many() {
        var two = Many.many(Strings.c("hello"));

        var res = two.parse(ParseContext.of("hellohello"));
        if (res instanceof Ok(List<String> r, ParseContext ctx)) {
            assertEquals(2, r.size());
        } else {
            fail();
        }

        res = two.parse(ParseContext.of(""));
        if (res instanceof Ok(List<String> r, ParseContext ctx)) {
            assertEquals(0, r.size());
        } else {
            fail();
        }
    }

    @Test
    public void test_some() {
        var two = Many.some(Strings.c("hello"));

        var res = two.parse(ParseContext.of(""));
        if (res instanceof Err(String msg, ParseContext ctx)) {
            assertEquals("unexpected end of stream", msg);
        } else {
            fail();
        }
    }

}