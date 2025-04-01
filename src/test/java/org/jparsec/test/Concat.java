package org.jparsec.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.jparsec.Api.*;

public class Concat {

    @Test
    public void test_concat() {
        var m = c('a').join(c('b')).join(c("hello"));

        var r = m.parse("abhello");
        Assertions.assertEquals("abhello", r.ok());

        var m2 = s(c('a'), c("hello"), c('b'));
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
        var m = many(any(c('a'), c('b'))).s();

        var r = m.parse("abbbaab");
        Assertions.assertEquals("abbbaab", r.ok());
    }
}
