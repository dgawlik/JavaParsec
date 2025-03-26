package org.jparsec.combinator;

import org.jparsec.Rule;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.jparsec.containers.ParseContext;
import org.jparsec.containers.ParseResult;

import java.util.Optional;

public class Opt<T> extends Rule<Optional<T>> {

    private final Rule<T> inner;

    private Opt(Rule<T> inner) {
        super("error in <opt>");
        this.inner = inner;
    }

    public static <T> Opt<T> opt(Rule<T> inner) {
        return new Opt<>(inner);
    }

    @Override
    public String toString() {
        return inner.toString();
    }

    @Override
    public ParseResult<Optional<T>> parse(ParseContext ctx) {
        var result = this.inner.parse(ctx);

        switch (result) {
            case Ok(T value, ParseContext newCtx) -> {
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
