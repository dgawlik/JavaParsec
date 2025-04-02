package org.jparsec.combinator;

import org.jparsec.Matcher;
import org.jparsec.containers.*;
import org.jparsec.containers.choice.Either;

public class Or<T, U> extends Matcher<Either<T, U>> {

    private final Matcher<T> that;
    private final Matcher<U> other;

    public Or(Matcher<T> that, Matcher<U> other) {
        super("error in alternative");
        this.that = that;
        this.other = other;
    }


    @Override
    public MatchResult<Either<T, U>> parse(Context ctx) {
        String firstError = null;
        switch (that.parse(ctx)) {
            case Ok(T res, Context newCtx) -> {
                return new Ok<>(new Either.Left<>(res), newCtx);
            }
            case Err(String m, Context ctx2) -> {
                firstError = m;
                ctx.addVerboseError(errorMessage);
                ctx.appendTrace(errorMessage);
                ctx.traceSnapshot();
            }
        }

        if (other.parse(ctx) instanceof Ok(U right, Context newCtx)) {
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
