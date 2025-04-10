package org.jparsec.test;

import org.jparsec.containers.Context;
import org.jparsec.containers.Err;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.jparsec.Api.*;
import static org.jparsec.combinator.Choice.choice;
import static org.jparsec.combinator.Opt.opt;
import static org.jparsec.combinator.Seq.seq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ErrorHandlingTest {


    @Test
    public void seq_error_handling() {
        var seq3 = seq(anyOf('a'), anyOf('b'), anyOf('c'));

        var result = seq3.parse(Context.of("bbc"));
        if (result instanceof Err(String msg, Context ctx)) {
            Assertions.assertEquals("expected 'a'", msg);
        } else {
            fail();
        }

        result = seq3.parse(Context.of("acc"));
        if (result instanceof Err(String msg, Context ctx)) {
            Assertions.assertEquals("expected 'b'", msg);
        } else {
            fail();
        }

        result = seq3.parse(Context.of("abd"));
        if (result instanceof Err(String msg, Context ctx)) {
            Assertions.assertEquals("expected 'c'", msg);
        } else {
            fail();
        }
    }

    @Test
    public void or_error_handling() {
        var aOrB = choice(anyOf('a'), anyOf('b'));

        var result = aOrB.parse(Context.of("c"));
        if (result instanceof Err(String msg, Context ctx)) {
            assertEquals("expected 'a'", msg);
        } else {
            fail();
        }
    }

    @Test
    public void take_first_error_from_bottom() {
        var complex = seq(anyOf('a'),
                choice(anyOf('b'), anyOf('c')),
                opt(anyOf('d')));

        var result = complex.parse(Context.of("add"));
        if (result instanceof Err(String msg, Context ctx)) {
            Assertions.assertEquals("expected 'b'", msg);
        } else {
            fail();
        }
    }

    @Test
    public void custom_error_message() {
        var complex = seq(anyOf('a'),
                choice(anyOf('b'), anyOf('c')).setErrorMessage("special error message"),
                opt(anyOf('d')));

        var result = complex.parse(Context.of("add"));
        if (result instanceof Err(String msg, Context ctx)) {
            Assertions.assertEquals("special error message", msg);
        } else {
            fail();
        }
    }

    @Test
    public void custom_error_message_shadowing() {
        var complex = seq(anyOf('a'),
                choice(anyOf('b'), anyOf('c')).setErrorMessage("shadowed error"),
                opt(anyOf('d'))).setErrorMessage("visible error");

        var result = complex.parse(Context.of("add"));
        if (result instanceof Err(String msg, Context ctx)) {
            Assertions.assertEquals("visible error", msg);
        } else {
            fail();
        }
    }

}