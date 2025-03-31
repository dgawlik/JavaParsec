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

        var m2 = join(c('a'), c("hello"), c('b'));
        var r2 = m2.parse("ahellob");
        Assertions.assertEquals("ahellob", r2.ok());
    }
}
