package org.jparsec.test;

import org.jparsec.containers.Context;
import org.jparsec.containers.Ok;
import org.jparsec.containers.Tuple5;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.jparsec.Api.*;
import static org.jparsec.combinator.Seq.seq;
import static org.jparsec.combinator.Times.times;

class TimesTest {

    @Test
    public void test_success() {
        var date = seq(
                times(digit(), 2).map(this::join),
                anyOf('/'),
                times(digit(), 2).map(this::join),
                anyOf('/'),
                times(digit(), 4).map(this::join)
        );

        var result = date.parse(Context.of("12/07/2025"));
        if (result instanceof Ok(Tuple5(String day, Character c1, String month, Character c2, String year), Context ctx)){
            Assertions.assertEquals("12/07/2025", day+"/"+month+"/"+year);
        }
    }

    private String join(List<Character> lc ) {
        return lc.stream().map(Object::toString).collect(Collectors.joining());
    }

}