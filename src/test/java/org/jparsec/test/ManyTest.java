package org.jparsec.test;

import org.jparsec.combinator.Many;
import org.jparsec.combinator.Strings;
import org.jparsec.containers.Context;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ManyTest {

    @Test
    public void test_many() {
        var two = Many.many(Strings.c("hello"));

        var res = two.parse(Context.of("hellohello"));
        if (res instanceof Ok(List<String> r, Context ctx)) {
            assertEquals(2, r.size());
        } else {
            fail();
        }

        res = two.parse(Context.of(""));
        if (res instanceof Ok(List<String> r, Context ctx)) {
            assertEquals(0, r.size());
        } else {
            fail();
        }
    }

    @Test
    public void test_some() {
        var two = Many.some(Strings.c("hello"));

        var res = two.parse(Context.of(""));
        if (res instanceof Err(String msg, Context ctx)) {
            assertEquals("unexpected end of stream", msg);
        } else {
            fail();
        }
    }

}