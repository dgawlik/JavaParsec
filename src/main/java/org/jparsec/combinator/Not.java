package org.jparsec.combinator;

import org.jparsec.Rule;
import org.jparsec.containers.*;

public class Not extends Rule<Empty> {

    private Rule<?> inner;

    public Not(Rule<?> inner) {
        super("Not expecting " + inner.toString());
    }

    @Override
    public String toString() {
        return "not " + inner.toString();
    }

    @Override
    public ParseResult<Empty> parse(ParseContext ctx) {
        return switch (inner.parse(ctx)) {
            case Ok(_, _) -> {
                ctx.appendTrace(errorMessage);
                ctx.addVerboseError(errorMessage);
                yield new Err<>(errorMessage, ctx);
            }
            case Err(_, _) -> new Ok<>(new Empty(), ctx);
        };
    }
}
