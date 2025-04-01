package org.jparsec.test;

import org.jparsec.combinator.Whitespace;
import org.jparsec.containers.Context;
import org.jparsec.containers.Empty;
import org.jparsec.containers.Ok;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WhitespaceTest {

    @Test
    public void test_accept_whitespaces() {
        var ws = Whitespace.spaces(Whitespace.Config.defaults());

        var ctx = Context.of("  \t\n");
        var result = ws.parse(ctx);

        if (result instanceof Ok(Empty e, Context newCtx)) {
            Assertions.assertEquals(newCtx.content.length(), newCtx.index);
        } else {
            Assertions.fail();
        }
    }

    @Test
    public void test_accept_comments() {
        var ws = Whitespace.spaces(Whitespace.Config.defaults()
                .withSinglelineComment("--"));

        var ctx = Context.of("""
                -- this is a comment
                a""");
        var result = ws.parse(ctx);

        if (result instanceof Ok(Empty e, Context newCtx)) {
            Assertions.assertEquals(newCtx.content.lastIndexOf("a"), newCtx.index);
        } else {
            Assertions.fail();
        }
    }

    @Test
    public void test_accept_multiline_comments() {
        var ws = Whitespace.spaces(Whitespace.Config.defaults()
                .withMultilineComment("#{", "#}"));

        var ctx = Context.of("""
                #{
                  this is
                  multiline comment
                #}
                a""");
        var result = ws.parse(ctx);

        if (result instanceof Ok(Empty e, Context newCtx)) {
            Assertions.assertEquals(newCtx.content.lastIndexOf("a"), newCtx.index);
        } else {
            Assertions.fail();
        }
    }
}
