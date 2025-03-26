package org.jparsec.combinator;

import org.jparsec.Rule;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.jparsec.containers.ParseContext;
import org.jparsec.containers.ParseResult;

import java.util.function.Predicate;

public class FailIf<U> extends Rule<U> {

    private final Rule<U> that;
    private final Predicate<U> fn;


    public FailIf(Rule<U> that, Predicate<U> fn, String errorMessage) {
        super("error in failIf");
        this.that = that;
        this.fn = fn;
        this.setErrorMessage(errorMessage);
    }

    @Override
    public ParseResult<U> parse(ParseContext ctx) {
        var result = that.parse(ctx);

        return switch (result) {
            case Ok(U r, ParseContext newCtx) -> {
                if (fn.test(r)) {
                    newCtx.appendTrace(errorMessage);
                    newCtx.addVerboseError(errorMessage);
                    yield new Err<>(customError.orElse(errorMessage), ctx);
                }
                yield new Ok<>(r, newCtx);
            }
            case Err(String msg, ParseContext newCtx) -> {
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
