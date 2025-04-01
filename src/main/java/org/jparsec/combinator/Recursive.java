package org.jparsec.combinator;

import org.jparsec.Matcher;
import org.jparsec.containers.Context;
import org.jparsec.containers.Err;
import org.jparsec.containers.MatchResult;

public class Recursive<T> extends Matcher<T> {

    private Matcher<T> inner;

    public Recursive() {
        super("error in recusive rule");
    }

    public void set(Matcher<T> inner) {
        this.inner = inner;
    }

    @Override
    public MatchResult<T> parse(Context ctx) {
        var result = this.inner.parse(ctx);
        if (result instanceof Err(String msg, Context ctx2)){
            ctx.addVerboseError(errorMessage);
            ctx.appendTrace(errorMessage);
        }
        return result;
    }
}
