package org.jparsec.combinator;

import org.jparsec.Rule;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.jparsec.containers.ParseContext;
import org.jparsec.containers.ParseResult;

public class DropRight<T, U> extends Rule<T> {

    private final Rule<T> that;
    private final Rule<U> other;

    public DropRight(Rule<T> that, Rule<U> other) {
        super("error in drop right");
        this.that = that;
        this.other = other;
    }

    @Override
    public ParseResult<T> parse(ParseContext ctx) {
        var keepRes = that.parse(ctx);

        return switch (keepRes) {
            case Err(String msg, _) -> {
                ctx.addVerboseError(errorMessage);
                ctx.appendTrace(errorMessage);
                yield new Err<>(msg, ctx);
            }
            case Ok(T res, ParseContext newCtx) -> switch (other.parse(newCtx)) {
                case Ok(_, ParseContext newCtx2) -> new Ok<>(res, newCtx2);
                case Err(String msg, _) -> {
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
