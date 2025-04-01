package org.jparsec.test;

import org.jparsec.combinator.Strings;
import org.jparsec.containers.Context;
import org.junit.jupiter.api.Test;

import static org.jparsec.test.TestUtil.assertFailWithMessage;
import static org.jparsec.test.TestUtil.assertParsed;

public class StringsTest {

    @Test
    public void test_exact() {
        var exact = Strings.c("abc");

        var ctx1 = Context.of("abc");
        var result = exact.parse(ctx1);
        assertParsed(result, "abc");

        var ctx2 = Context.of("ab");
        result = exact.parse(ctx2);
        assertFailWithMessage(result, "unexpected end of stream");

        var ctx3 = Context.of("abd");
        result = exact.parse(ctx3);
        assertFailWithMessage(result, "expected \"abc\"");
    }

    @Test
    public void test_case_insensitive() {
        var ignoreCase = Strings.stringIgnoreCase("abc");

        var ctx1 = Context.of("AbC");
        var result = ignoreCase.parse(ctx1);
        assertParsed(result, "AbC");
    }
}
