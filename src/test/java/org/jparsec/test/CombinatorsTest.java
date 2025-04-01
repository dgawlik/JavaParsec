package org.jparsec.test;

import org.jparsec.Api;
import org.jparsec.Ops;
import org.jparsec.combinator.Whitespace;
import org.jparsec.containers.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.stream.Collectors;

import static org.jparsec.Api.*;

public class CombinatorsTest {

    @Test
    public void take_while_test() {
        var takeA = anyChar().takeWhile(c -> c == 'a')
                .map(lc -> lc.stream()
                        .map(c -> "" + c)
                        .collect(Collectors.joining()));

        var ctx = ParseContext.of("aaab");
        var result = takeA.parse(ctx);

        if (result instanceof Ok(String r, ParseContext newCtx)) {
            Assertions.assertEquals("aaa", r);
            Assertions.assertEquals(newCtx.content.indexOf("b"), newCtx.index);
        }
    }

    @Test
    public void test_or() {
        var helloOrWorld = Api.c("hello").or(Api.c("world"));

        Function<ParseContext, Empty> test = (ParseContext ctx) -> {
            switch (helloOrWorld.parse(ctx)) {
                case Ok(Either.Left(String s), ParseContext c) -> {
                    Assertions.assertEquals("hello", s);
                }
                case Ok(Either.Right(String s), ParseContext c) -> {
                    Assertions.assertEquals("world", s);
                }
                case Err(String msg, ParseContext newCtx) -> {
                    Assertions.assertEquals(0, newCtx.index);
                }
            }
            return new Empty();
        };

        test.apply(ParseContext.of("hello"));
        test.apply(ParseContext.of("world"));
        test.apply(ParseContext.of("baz"));
    }

    @Test
    public void test_seq() {
        var helloWorld = Api.c("hello")
                .seq(spaces(Whitespace.Config.defaults()))
                .seq(Api.c("world"));

        var ctx1 = ParseContext.of("hello \n world");
        var result = helloWorld.parse(ctx1);

        if (result instanceof Ok(Pair(Pair(String h, Empty e), String w), ParseContext newCtx)) {
            Assertions.assertEquals("hello", h);
            Assertions.assertEquals("world", w);
            Assertions.assertEquals(newCtx.content.length(), newCtx.index);
        } else {
            Assertions.fail();
        }

        var ctx2 = ParseContext.of("hello \n baz");
        result = helloWorld.parse(ctx2);

        if (result instanceof Err(String m, ParseContext newCtx)) {
            Assertions.assertEquals(8, newCtx.index);
        }
    }

    @Test
    public void test_trim_left() {
        var hello = Api.c("hello");
        var ws = Whitespace.spaces(Whitespace.Config.defaults());

        var trimmedLeft = ws.dropLeft(hello);
        var res = trimmedLeft.parse(ParseContext.of("\n\t hello"));
        if (res instanceof Ok(String result, ParseContext resCtx)) {
            Assertions.assertEquals("hello", result);
            Assertions.assertEquals(resCtx.content.length(), resCtx.index);
        } else {
            Assertions.fail();
        }
    }

    @Test
    public void test_trim_right() {
        var hello = Api.c("hello");
        var ws = Whitespace.spaces(Whitespace.Config.defaults());

        var trimmedLeft = hello.dropRight(ws);
        var res = trimmedLeft.parse(ParseContext.of("hello  // this is comment"));
        if (res instanceof Ok(String result, ParseContext resCtx)) {
            Assertions.assertEquals("hello", result);
            Assertions.assertEquals(resCtx.content.length(), resCtx.index);
        } else {
            Assertions.fail();
        }
    }

    @Test
    public void fail_on_predicate() {
        var day = Api.times(Api.digit(), 2)
                .map(Ops::toString)
                .map(Integer::valueOf)
                .failIf(i -> i > 31, "day number cant be greater than 31");

        var result = day.parse(ParseContext.of("32"));
        if (result instanceof Err(String msg, ParseContext c)) {
            Assertions.assertEquals("day number cant be greater than 31", msg);
        } else {
            Assertions.fail();
        }
    }
}
