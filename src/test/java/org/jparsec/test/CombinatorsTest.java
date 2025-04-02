package org.jparsec.test;

import org.jparsec.Api;
import org.jparsec.Ops;
import org.jparsec.containers.*;
import org.jparsec.containers.choice.Either;
import org.jparsec.containers.seq.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.stream.Collectors;

import static org.jparsec.Api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class CombinatorsTest {

    @Test
    public void take_while_test() {
        var takeA = anyChar().takeWhile(c -> c == 'a').str();

        var ctx = Context.of("aaab");
        var result = takeA.parse(ctx);

        if (result instanceof Ok(String r, Context newCtx)) {
            assertEquals("aaa", r);
            assertEquals(newCtx.content.indexOf("b"), newCtx.index);
        } else {
            fail();
        }
    }

    @Test
    public void test_or() {
        var helloOrWorld = c("hello").or(c("world"));

        Function<Context, Empty> test = (Context ctx) -> {
            switch (helloOrWorld.parse(ctx)) {
                case Ok(Either.Left(String s), Context c) -> {
                    assertEquals("hello", s);
                }
                case Ok(Either.Right(String s), Context c) -> {
                    assertEquals("world", s);
                }
                case Err(String msg, Context newCtx) -> {
                    assertEquals(0, newCtx.index);
                }
            }
            return new Empty();
        };

        test.apply(Context.of("hello"));
        test.apply(Context.of("world"));
        test.apply(Context.of("baz"));
    }

    @Test
    public void test_seq() {
        var helloWorld = c("hello")
                .seq(spaces())
                .seq(c("world"));

        var ctx1 = Context.of("hello \n world");
        var result = helloWorld.parse(ctx1);

        if (result instanceof Ok(Pair(Pair(String h, Object e), String w), Context newCtx)) {
            assertEquals("hello", h);
            assertEquals("world", w);
            assertEquals(newCtx.content.length(), newCtx.index);
        } else {
            fail();
        }

        var ctx2 = Context.of("hello \n baz");
        result = helloWorld.parse(ctx2);

        if (result instanceof Err(String m, Context newCtx)) {
            assertEquals(8, newCtx.index);
        }
    }

    @Test
    public void test_trim_left() {
        var hello = c("hello");
        var ws = spaces();

        var trimmedLeft = ws.dropLeft(hello);
        var res = trimmedLeft.parse(Context.of("\n\t hello"));
        if (res instanceof Ok(String result, Context resCtx)) {
            assertEquals("hello", result);
            assertEquals(resCtx.content.length(), resCtx.index);
        } else {
            fail();
        }
    }

    @Test
    public void test_trim_right() {
        var hello = c("hello");
        var ws = many(spaces().or(comment("//")));

        var trimmedLeft = hello.dropRight(ws);
        var res = trimmedLeft.parse(Context.of("hello  // this is comment"));
        if (res instanceof Ok(String result, Context resCtx)) {
            assertEquals("hello", result);
            assertEquals(resCtx.content.length(), resCtx.index);
        } else {
            fail();
        }
    }

    @Test
    public void fail_on_predicate() {
        var day = Api.times(Api.digit(), 2)
                .map(Ops::toString)
                .map(Integer::valueOf)
                .failIf(i -> i > 31, "day number cant be greater than 31");

        var result = day.parse(Context.of("32"));
        if (result instanceof Err(String msg, Context c)) {
            assertEquals("day number cant be greater than 31", msg);
        } else {
            fail();
        }
    }
}
