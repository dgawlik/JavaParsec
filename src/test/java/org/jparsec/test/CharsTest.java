package org.jparsec.test;

import org.junit.jupiter.api.Test;

import static org.jparsec.Api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CharsTest {

    @Test
    public void test_wildcard() {
        var wildcard = anyChar().parse("a");
        assertEquals('a', (char) wildcard.ok());
    }

    @Test
    public void test_wildcard_fail() {
        var wildcard = anyChar().parse("");
        assertEquals("unexpected end of stream", wildcard.error());
    }

    @Test
    public void test_char_anyOf() {
        var abc = anyOf('a', 'b', 'c');

        assertEquals('a', (char) abc.parse("a").ok());
        assertEquals('b', (char) abc.parse("b").ok());
        assertEquals('c', (char) abc.parse("c").ok());
        assertEquals("expected 'a','b','c'", abc.parse("d").error());
    }

    @Test
    public void test_whitespace() {
        var ws = whitespace();

        assertEquals('\t', (char) ws.parse("\t").ok());
        assertEquals("expected whitespace", ws.parse("a").error());
    }

    @Test
    public void test_digit() {
        var nonZeroDigit = nonZeroDigit();

        assertEquals('1', (char) nonZeroDigit.parse("1").ok());
        assertEquals("expected non zero digit", nonZeroDigit.parse("0").error());

        var digit = digit();

        assertEquals('0', (char) digit.parse("0").ok());
        assertEquals("expected digit", digit.parse("a").error());
    }

    @Test
    public void test_alpha() {
        var alpha = letter();

        assertEquals('a', (char) alpha.parse("a").ok());
        assertEquals("expected letter", alpha.parse("1").error());
    }

    @Test
    public void test_alphanum() {
        var alphanum = alphaNum();

        assertEquals('1', (char) alphanum.parse("1").ok());
        assertEquals('a', (char) alphanum.parse("a'").ok());
        assertEquals("expected letter or digit", alphanum.parse("@").error());
    }

    @Test
    public void test_lower() {
        var lower = lower();

        assertEquals('l', (char) lower.parse("l").ok());
        assertEquals("expected lower char", lower.parse("L").error());
    }

    @Test
    public void test_upper() {
        var upper = upper();

        assertEquals('L', (char) upper.parse("L").ok());
        assertEquals("expected upper char", upper.parse("l").error());
    }

    @Test
    public void test_range() {
        var range = range('a', 'f');

        assertEquals('c', (char) range.parse("c").ok());
        assertEquals("expected 'a'..'f'", range.parse("g").error());
    }

    @Test
    public void test_none_anyOf() {
        var noneOf = noneOf('a', 'b');

        assertEquals("not expecting 'a','b'", noneOf.parse("a").error());
        assertTrue(noneOf.parse("c").isOk());
    }
}