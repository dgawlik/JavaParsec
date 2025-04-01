package org.jparsec.combinator;

import org.jparsec.Matcher;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.jparsec.containers.Context;
import org.jparsec.containers.MatchResult;

import java.util.Optional;

public class Opt<T> extends Matcher<Optional<T>> {

    private final Matcher<T> inner;

    private Opt(Matcher<T> inner) {
        super("error in <opt>");
        this.inner = inner;
    }

    public static <T> Opt<T> opt(Matcher<T> inner) {
        return new Opt<>(inner);
    }

    @Override
    public String toString() {
        return inner.toString();
    }

    @Override
    public MatchResult<Optional<T>> parse(Context ctx) {
        var result = this.inner.parse(ctx);

        switch (result) {
            case Ok(T value, Context newCtx) -> {
                return new Ok<>(Optional.of(value), newCtx);
            }
            case Err<T> e -> {
                ctx.clearTrace();
                ctx.addVerboseError(errorMessage);
                return new Ok<>(Optional.empty(), ctx);
            }
        }
    }
}
