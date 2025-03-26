package org.jparsec.combinator;

import org.jparsec.Rule;
import org.jparsec.containers.*;

public class Eos extends Rule<Empty> {

    public Eos() {
        super("error - characters remaining");
    }

    @Override
    public String toString() {
        return "<eos>";
    }

    @Override
    public ParseResult<Empty> parse(ParseContext ctx) {
        if (ctx.index < ctx.content.length()){
            ctx.appendTrace(errorMessage);
            ctx.addVerboseError(errorMessage);
            return new Err<>(errorMessage, ctx);
        } else {
            return new Ok<>(new Empty(), ctx);
        }
    }
}
