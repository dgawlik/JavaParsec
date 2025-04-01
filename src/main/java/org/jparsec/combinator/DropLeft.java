package org.jparsec.combinator;

import org.jparsec.Matcher;
import org.jparsec.containers.Context;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.jparsec.containers.MatchResult;

public class DropLeft<T, U> extends Matcher<U> {

    private final Matcher<T> that;
    private final Matcher<U> other;

    public DropLeft(Matcher<T> that, Matcher<U> other) {
        super("error in drop left");
        this.that = that;
        this.other = other;
    }

    @Override
    public MatchResult<U> parse(Context ctx) {
        var discardRes = that.parse(ctx);

        return switch (discardRes) {
            case Ok(T r, Context newCtx) -> {
                if (that.customError.isPresent()) {
                    other.setErrorMessage(errorMessage);
                }
                yield other.parse(newCtx);
            }
            case Err(String msg, Context ctx2) -> {
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
