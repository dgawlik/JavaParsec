package org.jparsec.combinator;

import org.jparsec.Rule;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.jparsec.containers.ParseContext;
import org.jparsec.containers.ParseResult;

import java.util.function.Function;

public class Map<T, U> extends Rule<U> {

    private final Function<T, U> fn;
    private final Rule<T> that;

    public Map(Rule<T> that, Function<T, U> fn) {
        super("error in mapping");
        this.fn = fn;
        this.that = that;
    }

    @Override
    public ParseResult<U> parse(ParseContext ctx) {
        var result = that.parse(ctx);

        return switch (result) {
            case Ok(T r, ParseContext newCtx) -> new Ok<>(fn.apply(r), newCtx);
            case Err(String msg, ParseContext newCtx) -> {
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
