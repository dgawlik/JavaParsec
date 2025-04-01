package org.jparsec.combinator;

import org.jparsec.Rule;
import org.jparsec.containers.Err;
import org.jparsec.containers.ParseContext;
import org.jparsec.containers.ParseResult;

public class Recursive<T> extends Rule<T> {

    private Rule<T> inner;

    public Recursive() {
        super("error in recusive rule");
    }

    public void set(Rule<T> inner) {
        this.inner = inner;
    }

    @Override
    public ParseResult<T> parse(ParseContext ctx) {
        var result = this.inner.parse(ctx);
        if (result instanceof Err(String msg, ParseContext ctx2)){
            ctx.addVerboseError(errorMessage);
            ctx.appendTrace(errorMessage);
        }
        return result;
    }
}
