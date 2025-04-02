package org.jparsec.test;

import org.jparsec.containers.Context;
import org.jparsec.containers.Empty;
import org.jparsec.containers.Ok;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.jparsec.Api.*;

public class WhitespaceTest {

    @Test
    public void test_accept_whitespaces() {
        var ws = spaces();

        var ctx = Context.of("  \t\n");
        var result = ws.parse(ctx);

        if (result instanceof Ok(Object e, Context newCtx)) {
            Assertions.assertEquals(newCtx.content.length(), newCtx.index);
        } else {
            Assertions.fail();
        }
    }

    @Test
    public void test_accept_comments() {
        var ws = comment("--");

        var ctx = Context.of("""
                -- this is a comment
                a""");
        var result = ws.parse(ctx);

        if (result instanceof Ok(Object e, Context newCtx)) {
            Assertions.assertEquals(newCtx.content.lastIndexOf("a"), newCtx.index);
        } else {
            Assertions.fail();
        }
    }

    @Test
    public void test_accept_multiline_comments() {
        var ws = many(spaces().or(multilineComment("#{", "#}")));

        var ctx = Context.of("""
                #{
                  this is
                  multiline comment
                #}
                a""");
        var result = ws.parse(ctx);

        if (result instanceof Ok(Object e, Context newCtx)) {
            Assertions.assertEquals(newCtx.content.lastIndexOf("a"), newCtx.index);
        } else {
            Assertions.fail();
        }
    }
}
