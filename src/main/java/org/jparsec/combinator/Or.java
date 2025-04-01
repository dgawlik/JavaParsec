package org.jparsec.combinator;

import org.jparsec.Rule;
import org.jparsec.containers.*;

public class Or<T, U> extends Rule<Either<T, U>> {

    private final Rule<T> that;
    private final Rule<U> other;

    public Or(Rule<T> that, Rule<U> other) {
        super("error in alternative");
        this.that = that;
        this.other = other;
    }


    @Override
    public ParseResult<Either<T, U>> parse(ParseContext ctx) {
        String firstError = null;
        switch (that.parse(ctx)) {
            case Ok(T res, ParseContext newCtx) -> {
                return new Ok<>(new Either.Left<>(res), newCtx);
            }
            case Err(String m, ParseContext ctx2) -> {
                firstError = m;
                ctx.addVerboseError(errorMessage);
                ctx.appendTrace(errorMessage);
                ctx.traceSnapshot();
            }
        }

        if (other.parse(ctx) instanceof Ok(U right, ParseContext newCtx)) {
            newCtx.clearTrace();
            return new Ok<>(new Either.Right<>(right), newCtx);
        } else {
            ctx.recoverTraceSnapshot();
            return new Err<>(customError.orElse(firstError), ctx);
        }
    }

    @Override
    public String toString() {
        return internalDescription.orElse(that.toString());
    }
}
