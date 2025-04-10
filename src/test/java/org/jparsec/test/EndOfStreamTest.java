package org.jparsec.test;

import org.jparsec.containers.Context;
import org.jparsec.containers.Err;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.jparsec.Api.*;

public class EndOfStreamTest {

    @Test
    public void should_fail() {
        var singleA = anyChar().seq(eos());

        var result = singleA.parse(input("aa"));
        if (result instanceof Err(String msg, Context ctx)) {
            Assertions.assertEquals("error - characters remaining", msg);
        } else {
            Assertions.fail();
        }
    }
}
