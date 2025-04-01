package org.jparsec.combinator;

import org.jparsec.Matcher;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.jparsec.containers.Context;
import org.jparsec.containers.MatchResult;

public class DropRight<T, U> extends Matcher<T> {

    private final Matcher<T> that;
    private final Matcher<U> other;

    public DropRight(Matcher<T> that, Matcher<U> other) {
        super("error in drop right");
        this.that = that;
        this.other = other;
    }

    @Override
    public MatchResult<T> parse(Context ctx) {
        var keepRes = that.parse(ctx);

        return switch (keepRes) {
            case Err(String msg, Context ctx2) -> {
                ctx.addVerboseError(errorMessage);
                ctx.appendTrace(errorMessage);
                yield new Err<>(msg, ctx);
            }
            case Ok(T res, Context newCtx) -> switch (other.parse(newCtx)) {
                case Ok(U res2, Context newCtx2) -> new Ok<>(res, newCtx2);
                case Err(String msg, Context c) -> {
                    newCtx.appendTrace(errorMessage);
                    newCtx.addVerboseError(errorMessage);
                    yield new Err<>(customError.orElse(msg), newCtx);
                }
            };
        };
    }


    @Override
    public String toString() {
        return "drop right";
    }
}
