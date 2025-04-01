package org.jparsec.combinator;

import org.jparsec.Rule;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.jparsec.containers.ParseContext;
import org.jparsec.containers.ParseResult;

public class DropLeft<T, U> extends Rule<U> {

    private final Rule<T> that;
    private final Rule<U> other;

    public DropLeft(Rule<T> that, Rule<U> other) {
        super("error in drop left");
        this.that = that;
        this.other = other;
    }

    @Override
    public ParseResult<U> parse(ParseContext ctx) {
        var discardRes = that.parse(ctx);

        return switch (discardRes) {
            case Ok(T r, ParseContext newCtx) -> {
                if (that.customError.isPresent()) {
                    other.setErrorMessage(errorMessage);
                }
                yield other.parse(newCtx);
            }
            case Err(String msg, ParseContext ctx2) -> {
                var errMsg = customError.orElse(msg);
                ctx.addVerboseError(errorMessage);
                ctx.appendTrace(errorMessage);
                yield new Err<>(errMsg, ctx);
            }
        };
    }

    @Override
    public String toString() {
        return "drop left";
    }
}
