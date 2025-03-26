package org.jparsec.test;

import org.jparsec.combinator.Chars;
import org.junit.jupiter.api.Test;
import org.jparsec.containers.ParseContext;

import static org.jparsec.test.TestUtil.assertFailWithMessage;
import static org.jparsec.test.TestUtil.assertParsed;

class CharsTest {

    @Test
    public void test_wildcard() {
        var wildcard = Chars.any();
        var ctx = ParseContext.of("a");

        var result = wildcard.parse(ctx);
        assertParsed(result, 'a');
    }

    @Test
    public void test_wildcard_fail() {
        var wildcard = Chars.any();
        var ctx = ParseContext.of("");

        var result = wildcard.parse(ctx);
        assertFailWithMessage(result, "unexpected end of stream");
    }

    @Test
    public void test_char_anyOf() {
        var abc = Chars.anyOf('a', 'b', 'c');

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
        assertFailWithMessage(result, "a,b,c could not be matched");
    }

    @Test
    public void test_whitespace() {
        var ws = Chars.whitespace();

        var ctx1 = ParseContext.of("\t");
        var result = ws.parse(ctx1);
        assertParsed(result, '\t');

        var ctx2 = ParseContext.of("a");
        result = ws.parse(ctx2);
        assertFailWithMessage(result, "Expected whitespace char");
    }

    @Test
    public void test_digit() {
        var nonZeroDigit = Chars.nonZeroDigit();

        var ctx1 = ParseContext.of("1");
        var result = nonZeroDigit.parse(ctx1);
        assertParsed(result, '1');

        var ctx2 = ParseContext.of("0");
        result = nonZeroDigit.parse(ctx2);
        assertFailWithMessage(result, "1,2,3,4,5,6,7,8,9 could not be matched");

        var digit = Chars.digit();

        var ctx3 = ParseContext.of("0");
        result = digit.parse(ctx3);
        assertParsed(result, '0');

        var ctx4 = ParseContext.of("a");
        result = digit.parse(ctx4);
        assertFailWithMessage(result, "0,1,2,3,4,5,6,7,8,9 could not be matched");
    }

    @Test
    public void test_alpha() {
        var alpha = Chars.alpha();

        var ctx1 = ParseContext.of("a");
        var result = alpha.parse(ctx1);
        assertParsed(result, 'a');

        var ctx2 = ParseContext.of("1");
        result = alpha.parse(ctx2);
        assertFailWithMessage(result, "Expected alpha char");
    }

    @Test
    public void test_alphanum() {
        var alphanum = Chars.alphaNum();

        var ctx1 = ParseContext.of("a");
        var result = alphanum.parse(ctx1);
        assertParsed(result, 'a');

        var ctx2 = ParseContext.of("1");
        result = alphanum.parse(ctx2);
        assertParsed(result, '1');

        var ctx3 = ParseContext.of("@");
        result = alphanum.parse(ctx3);
        assertFailWithMessage(result, "Expected alphanum char");
    }

    @Test
    public void test_lower() {
        var lower = Chars.lower();

        var ctx1 = ParseContext.of("l");
        var result = lower.parse(ctx1);
        assertParsed(result, 'l');

        var ctx2 = ParseContext.of("L");
        result = lower.parse(ctx2);
        assertFailWithMessage(result, "Expected lowercase char");
    }

    @Test
    public void test_upper() {
        var upper = Chars.upper();

        var ctx1 = ParseContext.of("L");
        var result = upper.parse(ctx1);
        assertParsed(result, 'L');

        var ctx2 = ParseContext.of("l");
        result = upper.parse(ctx2);
        assertFailWithMessage(result, "Expected uppercase char");
    }

    @Test
    public void test_range() {
        var range = Chars.range('a', 'f');

        var ctx1 = ParseContext.of("a");
        var result = range.parse(ctx1);
        assertParsed(result, 'a');

        var ctx2 = ParseContext.of("g");
        result = range.parse(ctx2);
        assertFailWithMessage(result, "a,b,c,d,e,f could not be matched");
    }

    @Test
    public void test_none_anyOf() {
        var noneOf = Chars.noneOf('a', 'b');

        var ctx1 = ParseContext.of("a");
        var result = noneOf.parse(ctx1);
        assertFailWithMessage(result, "none of a,b should be matched");

        var ctx2 = ParseContext.of("c");
        result = noneOf.parse(ctx2);
        assertParsed(result, 'c');
    }
}