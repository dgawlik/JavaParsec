package org.jparsec.test;

import org.junit.jupiter.api.Test;

import static org.jparsec.Api.c;
import static org.jparsec.Api.stringIgnoreCase;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringsTest {

    @Test
    public void test_exact() {
        var exact = c("abc");

        assertEquals("abc", exact.parse("abc").ok());
        assertEquals("unexpected end of stream", exact.parse("ab").error());
        assertEquals("expected \"abc\"", exact.parse("abd").error());
    }

    @Test
    public void test_case_insensitive() {
        var ignoreCase = stringIgnoreCase("abc");
        assertEquals("AbC", ignoreCase.parse("AbC").ok());
    }
}
