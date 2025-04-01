package org.jparsec.test;

import org.jparsec.Api;
import org.jparsec.Ops;
import org.jparsec.containers.Ok;
import org.jparsec.containers.ParseContext;
import org.junit.jupiter.api.Test;

import static org.jparsec.Ops.ops;
import static org.junit.jupiter.api.Assertions.*;

class OpsTest {

    @Test
    public void test_any7() {
        var abc = Api.choice(
                        Api.anyOf('a'),
                        Api.anyOf('b'),
                        Api.anyOf('c'),
                        Api.anyOf('d'),
                        Api.anyOf('e'),
                        Api.anyOf('f'),
                        Api.anyOf('g'))
                .map(Ops::takeAny);

        var result = abc.parse(ParseContext.of("g"));
        if (result instanceof Ok(Character r, ParseContext c)) {
            assertEquals('g', (char) r);
        }

    }

    @Test
    public void test_concat() {
        var cText = Api.seq(
                Api.anyOf('a').map(Ops::toString),
                Api.c(" or "),
                Api.anyOf('b').map(Ops::toString),
                Api.c(" or "),
                Api.anyOf('c').map(Ops::toString),
                Api.c(" or "),
                Api.anyOf('d').map(Ops::toString)

        ).map(Ops::concat);

        var result = cText.parse(ParseContext.of("a or b or c or d"));
        if (result instanceof Ok(String r, ParseContext c)) {
            assertEquals("a or b or c or d", r);
        } else {
            fail();
        }
    }

    @Test
    public void test_concat2() {
        var cText = Api.many(
                Api.c("ab")
        ).map(Ops::concat);

        var result = cText.parse(ParseContext.of("ababab"));
        if (result instanceof Ok(String r, ParseContext c)) {
            assertEquals("ababab", r);
        } else {
            fail();
        }
    }

    @Test
    public void test_sep() {
        var cText = Api.sepBy(
                Api.c("ab"),
                Api.anyOf(',')
        ).map(ops(",")::sepJoin);

        var result = cText.parse(ParseContext.of("ab,ab,ab"));
        if (result instanceof Ok(String r, ParseContext c)) {
            assertEquals("ab,ab,ab", r);
        } else {
            fail();
        }
    }

    @Test
    public void test_reduce() {
        var numbers = Api.sepBy(
                Api.some(Api.digit()).map(Ops::toString).map(Integer::valueOf),
                Api.anyOf('+')
        ).map(ops(4, Integer::sum)::reduce);

        var result = numbers.parse(ParseContext.of("1+2+3"));
        if (result instanceof Ok(Integer r, ParseContext c)) {
            assertEquals(10, (int) r);
        } else {
            fail();
        }
    }

    @Test
    public void test_flatten() {
        var quotes = Api.many(
                Api.seq(
                        Api.anyOf('"').map(Ops::singleton),
                        Api.many(Api.noneOf('\"')),
                        Api.anyOf('"').map(Ops::singleton)
                ).map(Ops::takeMiddle)
        ).map(Ops::flatten).map(Ops::toString);

        var result = quotes.parse(ParseContext.of("\"hello\"\"world\""));
        if (result instanceof Ok(String r, ParseContext c)) {
            assertEquals("helloworld", r);
        } else {
            fail();
        }
    }

}