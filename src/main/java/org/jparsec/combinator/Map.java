package org.jparsec.combinator;

import org.jparsec.Matcher;
import org.jparsec.containers.Context;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.jparsec.containers.MatchResult;

import java.util.function.Function;

public class Map<T, U> extends Matcher<U> {

    private final Function<T, U> fn;
    private final Matcher<T> that;

    public Map(Matcher<T> that, Function<T, U> fn) {
        super("error in mapping");
        this.fn = fn;
        this.that = that;
    }

    @Override
    public MatchResult<U> parse(Context ctx) {
        var result = that.parse(ctx);

        return switch (result) {
            case Ok(T r, Context newCtx) -> new Ok<>(fn.apply(r), newCtx);
            case Err(String msg, Context newCtx) -> {
                newCtx.appendTrace(errorMessage);
                newCtx.addVerboseError(errorMessage);
                yield new Err<>(customError.orElse(msg), newCtx);
            }
        };
    }

    @Override
    public String toString() {
        return internalDescription.orElse(that.toString());
    }
}
