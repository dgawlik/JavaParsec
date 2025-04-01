package org.jparsec.test;

import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.jparsec.containers.Context;
import org.jparsec.containers.MatchResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestUtil {

    public static <T> void assertParsed(MatchResult<T> result, T t) {
        if (result instanceof Ok(T c, Context ctx2)) {
            assertEquals(t, c);
        } else {
            fail();
        }
    }

    public static <T> void assertFailWithMessage(MatchResult<T> result, String t) {
        if (result instanceof Err(String msg, Context ctx2)) {
            assertEquals(t, msg);
        } else {
            fail();
        }
    }
}
