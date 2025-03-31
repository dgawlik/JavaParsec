package org.jparsec.test;

import org.jparsec.combinator.Strings;
import org.jparsec.combinator.Whitespace;
import org.jparsec.containers.Err;
import org.jparsec.containers.ParseContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.jparsec.Api.*;
import static org.jparsec.combinator.Lexeme.lexeme;
import static org.jparsec.combinator.Seq.seq;

public class ErrorPrettyPrintTest {

    @Test
    public void correct_line_and_column() {
        var ws = spaces(Whitespace.Config.defaults());
        var breakOnLine3 = seq(lexeme(anyChar(), ws),
                                lexeme(anyChar(), ws),
                                lexeme(anyOf('a'), ws));

        var text = """
                a
                b
                c
                """;

        var result = breakOnLine3.parse(ParseContext.of(text));
        if (result instanceof Err e) {
            Assertions.assertEquals("""
                    b
                    >>c
                    --------
                    Line: 2, Column: 0
                    expected 'a'""", e.errorPrettyPrint());
        } else {
            Assertions.fail();
        }
    }

    @Test
    public void correct_line_and_column_3() {
        var ws = Whitespace.spaces(Whitespace.Config.defaults());
        var breakOnLine2 = seq(lexeme(anyChar(), ws),
                lexeme(seq(anyOf('a'), Strings.c("xxx")), ws),
                lexeme(anyChar(), ws));

        var text = """
                a
                ayyy
                c
                """;

        var result = breakOnLine2.parse(ParseContext.of(text));
        if (result instanceof Err e) {
            Assertions.assertEquals("""
                    a
                    a>>yyy
                    c
                    --------
                    Line: 1, Column: 1
                    expected "xxx\"""", e.errorPrettyPrint());
        } else {
            Assertions.fail();
        }
    }

    @Test
    public void correct_line_and_column_2() {
        var ws = Whitespace.spaces(Whitespace.Config.defaults());
        var breakOnLine2 = seq(lexeme(anyChar(), ws),
                lexeme(anyOf('a'), ws),
                lexeme(anyChar(), ws));

        var text = """
                a
                b
                c
                """;

        var result = breakOnLine2.parse(ParseContext.of(text));
        if (result instanceof Err e) {
            Assertions.assertEquals("""
                    a
                    >>b
                    c
                    --------
                    Line: 1, Column: 0
                    expected 'a'""", e.errorPrettyPrint());
        } else {
            Assertions.fail();
        }
    }
}
