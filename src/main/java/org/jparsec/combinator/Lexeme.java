package org.jparsec.combinator;

import org.jparsec.Matcher;
import org.jparsec.containers.*;

public class Lexeme<T> extends Matcher<T> {

    private final Matcher<T> inner;
    private final Matcher<?> ws;

    public Lexeme(Matcher<T> inner, Matcher<?> ws) {
        super("error in lexeme");
        this.inner = inner;
        this.ws = ws;
    }

    @Override
    public String toString() {
        return inner.toString();
    }


    @Override
    public MatchResult<T> parse(Context ctx) {
        var result = this.inner.parse(ctx);

        switch (result) {
            case Ok(T value, Context newCtx) -> {
                if (ws.parse(newCtx) instanceof Ok(Object e, Context newCtx2)) {
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
