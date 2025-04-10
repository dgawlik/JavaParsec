package org.jparsec.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.jparsec.Api.*;

public class Concat {

    @Test
    public void test_concat() {
        var m = seq(c('a'),c('b'),c("hello")).str();

        var r = m.parse("abhello");
        Assertions.assertEquals("abhello", r.ok());

        var m2 = seq(c('a'), c("hello"), c('b')).str();
        var r2 = m2.parse("ahellob");
        Assertions.assertEquals("ahellob", r2.ok());
    }

    @Test
    public void test_reduce() {
        var m = many(c('a')).reduce("hello ", (el, acc) -> acc + el);

        var r = m.parse("aaa");
        Assertions.assertEquals("hello aaa", r.ok());
    }

    @Test
    public void test_list_of_chars_to_string() {
        var m = many(any(c('a'), c('b'))).str();

        var r = m.parse("abbbaab");
        Assertions.assertEquals("abbbaab", r.ok());
    }

    @Test
    public void test_times() {
        var m = times(any(c('a'), c('b')), 1 ,3).str();

        var r = m.parse("aab");
        Assertions.assertEquals("aab", r.ok());
    }
}
