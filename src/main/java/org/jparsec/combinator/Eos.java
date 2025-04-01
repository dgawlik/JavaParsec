package org.jparsec.combinator;

import org.jparsec.Matcher;
import org.jparsec.containers.*;

public class Eos extends Matcher<Empty> {

    public Eos() {
        super("error - characters remaining");
    }

    @Override
    public String toString() {
        return "<eos>";
    }

    @Override
    public MatchResult<Empty> parse(Context ctx) {
        if (ctx.index < ctx.content.length()){
            ctx.appendTrace(errorMessage);
            ctx.addVerboseError(errorMessage);
            return new Err<>(errorMessage, ctx);
        } else {
            return new Ok<>(new Empty(), ctx);
        }
    }
}
