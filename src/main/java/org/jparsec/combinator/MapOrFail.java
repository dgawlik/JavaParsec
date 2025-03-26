package org.jparsec.combinator;

import org.jparsec.Rule;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.jparsec.containers.ParseContext;
import org.jparsec.containers.ParseResult;

import java.util.function.Function;

public class MapOrFail<T,U> extends Rule<U> {

    private final Rule<T> that;
    private final Function<ParseResult<T>, ParseResult<U>> fn;

    public MapOrFail(Rule<T> that, Function<ParseResult<T>, ParseResult<U>> fn) {
        super("error in mapOrFail");
        this.that = that;
        this.fn = fn;
    }

    @Override
    public ParseResult<U> parse(ParseContext ctx) {
        var result = that.parse(ctx);

        return switch (result) {
            case Ok o -> fn.apply(o);
            case Err(String msg, ParseContext newCtx) -> {
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
