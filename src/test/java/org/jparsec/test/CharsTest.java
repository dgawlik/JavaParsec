package org.jparsec.test;

import static org.jparsec.Api.*;
import org.junit.jupiter.api.Test;
import org.jparsec.containers.ParseContext;

import static org.jparsec.test.TestUtil.assertFailWithMessage;
import static org.jparsec.test.TestUtil.assertParsed;

class CharsTest {

    @Test
    public void test_wildcard() {
        var wildcard = any();
        var ctx = ParseContext.of("a");

        var result = wildcard.parse(ctx);
        assertParsed(result, 'a');
    }

    @Test
    public void test_wildcard_fail() {
        var wildcard = any();
        var ctx = ParseContext.of("");

        var result = wildcard.parse(ctx);
        assertFailWithMessage(result, "unexpected end of stream");
    }

    @Test
    public void test_char_anyOf() {
        var abc = anyOf('a', 'b', 'c');

        var ctx1 = ParseContext.of("a");
        var result = abc.parse(ctx1);
        assertParsed(result, 'a');

        var ctx2 = ParseContext.of("b");
        result = abc.parse(ctx2);
        assertParsed(result, 'b');

        var ctx3 = ParseContext.of("c");
        result = abc.parse(ctx3);
        assertParsed(result, 'c');

        var ctx4 = ParseContext.of("d");
        result = abc.parse(ctx4);
        assertFailWithMessage(result, "expected 'a','b','c'");
    }

    @Test
    public void test_whitespace() {
        var ws = whitespace();

        var ctx1 = ParseContext.of("\t");
        var result = ws.parse(ctx1);
        assertParsed(result, '\t');

        var ctx2 = ParseContext.of("a");
        result = ws.parse(ctx2);
        assertFailWithMessage(result, "expected whitespace");
    }

    @Test
    public void test_digit() {
        var nonZeroDigit = nonZeroDigit();

        var ctx1 = ParseContext.of("1");
        var result = nonZeroDigit.parse(ctx1);
        assertParsed(result, '1');

        var ctx2 = ParseContext.of("0");
        result = nonZeroDigit.parse(ctx2);
        assertFailWithMessage(result, "expected non zero digit");

        var digit = digit();

        var ctx3 = ParseContext.of("0");
        result = digit.parse(ctx3);
        assertParsed(result, '0');

        var ctx4 = ParseContext.of("a");
        result = digit.parse(ctx4);
        assertFailWithMessage(result, "expected digit");
    }

    @Test
    public void test_alpha() {
        var alpha = letter();

        var ctx1 = ParseContext.of("a");
        var result = alpha.parse(ctx1);
        assertParsed(result, 'a');

        var ctx2 = ParseContext.of("1");
        result = alpha.parse(ctx2);
        assertFailWithMessage(result, "expected letter");
    }

    @Test
    public void test_alphanum() {
        var alphanum = alphaNum();

        var ctx1 = ParseContext.of("a");
        var result = alphanum.parse(ctx1);
        assertParsed(result, 'a');

        var ctx2 = ParseContext.of("1");
        result = alphanum.parse(ctx2);
        assertParsed(result, '1');

        var ctx3 = ParseContext.of("@");
        result = alphanum.parse(ctx3);
        assertFailWithMessage(result, "expected letter or digit");
    }

    @Test
    public void test_lower() {
        var lower = lower();

        var ctx1 = ParseContext.of("l");
        var result = lower.parse(ctx1);
        assertParsed(result, 'l');

        var ctx2 = ParseContext.of("L");
        result = lower.parse(ctx2);
        assertFailWithMessage(result, "expected lower char");
    }

    @Test
    public void test_upper() {
        var upper = upper();

        var ctx1 = ParseContext.of("L");
        var result = upper.parse(ctx1);
        assertParsed(result, 'L');

        var ctx2 = ParseContext.of("l");
        result = upper.parse(ctx2);
        assertFailWithMessage(result, "expected upper char");
    }

    @Test
    public void test_range() {
        var range = range('a', 'f');

        var ctx1 = ParseContext.of("a");
        var result = range.parse(ctx1);
        assertParsed(result, 'a');

        var ctx2 = ParseContext.of("g");
        result = range.parse(ctx2);
        assertFailWithMessage(result, "expected 'a'..'f'");
    }

    @Test
    public void test_none_anyOf() {
        var noneOf = noneOf('a', 'b');

        var ctx1 = ParseContext.of("a");
        var result = noneOf.parse(ctx1);
        assertFailWithMessage(result, "not expecting 'a','b'");

        var ctx2 = ParseContext.of("c");
        result = noneOf.parse(ctx2);
        assertParsed(result, 'c');
    }
}