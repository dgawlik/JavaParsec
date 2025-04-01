package org.jparsec.combinator;

import org.jparsec.Matcher;
import org.jparsec.containers.Context;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.jparsec.containers.MatchResult;

import java.util.function.Predicate;

public class FailIf<U> extends Matcher<U> {

    private final Matcher<U> that;
    private final Predicate<U> fn;


    public FailIf(Matcher<U> that, Predicate<U> fn, String errorMessage) {
        super("error in failIf");
        this.that = that;
        this.fn = fn;
        this.setErrorMessage(errorMessage);
    }

    @Override
    public MatchResult<U> parse(Context ctx) {
        var result = that.parse(ctx);

        return switch (result) {
            case Ok(U r, Context newCtx) -> {
                if (fn.test(r)) {
                    newCtx.appendTrace(errorMessage);
                    newCtx.addVerboseError(errorMessage);
                    yield new Err<>(customError.orElse(errorMessage), ctx);
                }
                yield new Ok<>(r, newCtx);
            }
            case Err(String msg, Context newCtx) -> {
                newCtx.addVerboseError(errorMessage);
                newCtx.appendTrace(errorMessage);
                yield new Err<>(customError.orElse(msg), newCtx);
            }
        };
    }

    @Override
    public String toString() {
        return internalDescription.orElse(that.toString());
    }
}
