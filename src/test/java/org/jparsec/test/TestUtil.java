package org.jparsec.test;

import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.jparsec.containers.ParseContext;
import org.jparsec.containers.ParseResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestUtil {

    public static <T> void assertParsed(ParseResult<T> result, T t) {
        if (result instanceof Ok(T c, ParseContext ctx2)) {
            assertEquals(t, c);
        } else {
            fail();
        }
    }

    public static <T> void assertFailWithMessage(ParseResult<T> result, String t) {
        if (result instanceof Err(String msg, ParseContext ctx2)) {
            assertEquals(t, msg);
        } else {
            fail();
        }
    }
}
