package org.jparsec.test;

import org.jparsec.containers.Err;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.jparsec.Api.*;

public class ErrorTracingTest {

    @Test
    public void simple_trace() {
        var abcd = seq(anyOf('a').or(anyOf('b')), anyOf('c').or(anyOf('d')));

        var some = some(abcd);

        var result = some.parse(input("ax"));

        if (result instanceof Err e) {
            Assertions.assertEquals("""
                    Line: 0, Column: 0 => More than zero of 'a'::'c' required
                     +- Line: 0, Column: 1 => error in sequence
                      +- Line: 0, Column: 1 => error in alternative
                       +- Line: 0, Column: 1 => expected 'c'
                    """, e.errorTrace());
            Assertions.assertEquals("""
                 Line: 0, Column: 1 => expected 'c'
                 Line: 0, Column: 1 => error in alternative
                 Line: 0, Column: 1 => expected 'd'
                 Line: 0, Column: 1 => error in sequence
                 Line: 0, Column: 0 => More than zero of 'a'::'c' required
                 """, e.verboseErrors());
        } else {
            Assertions.fail();
        }
    }
}
