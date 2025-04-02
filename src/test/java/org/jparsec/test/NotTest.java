package org.jparsec.test;

import org.jparsec.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.jparsec.Api.*;

public class NotTest {

    @Test
    public void test_not_followed() {
        var zero = c("0").dropRight(not(digit()));
        var nonZero = seq(nonZeroDigit(), times(digit(), 1,3)).str();

        var m = any(zero, nonZero).map(Integer::valueOf);

        Assertions.assertThrows(ParseException.class, () -> m.parseThrow("001"));
    }
}
