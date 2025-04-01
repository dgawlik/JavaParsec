package org.jparsec.combinator;

import org.jparsec.Matcher;
import org.jparsec.containers.Context;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.jparsec.containers.MatchResult;

import java.util.function.Function;

public class MapOrFail<T,U> extends Matcher<U> {

    private final Matcher<T> that;
    private final Function<MatchResult<T>, MatchResult<U>> fn;

    public MapOrFail(Matcher<T> that, Function<MatchResult<T>, MatchResult<U>> fn) {
        super("error in mapOrFail");
        this.that = that;
        this.fn = fn;
    }

    @Override
    public MatchResult<U> parse(Context ctx) {
        var result = that.parse(ctx);

        return switch (result) {
            case Ok o -> fn.apply(o);
            case Err(String msg, Context newCtx) -> {
                newCtx.appendTrace(errorMessage);
                newCtx.addVerboseError(errorMessage);
                yield new Err<>(customError.orElse(msg), newCtx);
            }
        };
    }

    @Override
    public String toString() {
        return that.toString();
    }
}
