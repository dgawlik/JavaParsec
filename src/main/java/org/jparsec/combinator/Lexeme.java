package org.jparsec.combinator;

import org.jparsec.Rule;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.jparsec.containers.ParseContext;
import org.jparsec.containers.ParseResult;

public class Lexeme<T> extends Rule<T> {

    private final Rule<T> inner;
    private final Whitespace ws;

    private Lexeme(Rule<T> inner, Whitespace ws) {
        super("error in lexeme");
        this.inner = inner;
        this.ws = ws;
    }

    public static <T> Lexeme<T> lexeme(Rule<T> inner, Whitespace ws) {
        return new Lexeme<>(inner, ws);
    }

    @Override
    public String toString() {
        return inner.toString();
    }


    @Override
    public ParseResult<T> parse(ParseContext ctx) {
        var result = this.inner.parse(ctx);

        switch (result) {
            case Ok(T value, ParseContext newCtx) -> {
                if (ws.parse(newCtx) instanceof Ok(_, ParseContext newCtx2)) {
                    return new Ok<>(value, newCtx2);
                } else {
                    return new Ok<>(value, newCtx);
                }
            }
            case Err<T> e -> {
                ctx.addVerboseError(errorMessage);
                ctx.appendTrace(errorMessage);
                return e;
            }
        }
    }
}
