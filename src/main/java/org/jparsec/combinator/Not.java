package org.jparsec.combinator;

import org.jparsec.Matcher;
import org.jparsec.containers.*;

public class Not extends Matcher<Empty> {

    private Matcher<?> inner;

    public Not(Matcher<?> inner) {
        super("Not expecting " + inner.toString());
        this.inner = inner;
    }

    @Override
    public String toString() {
        return "not " + inner.toString();
    }

    @Override
    public MatchResult<Empty> parse(Context ctx) {
        return switch (inner.parse(ctx)) {
            case Ok(Object v, Context ctx2) -> {
                ctx.appendTrace(errorMessage);
                ctx.addVerboseError(errorMessage);
                yield new Err<>(errorMessage, ctx);
            }
            case Err(String msg, Context ctx2) -> new Ok<>(new Empty(), ctx);
        };
    }
}
