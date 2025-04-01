package org.jparsec.combinator;

import org.jparsec.Matcher;
import org.jparsec.containers.Context;
import org.jparsec.containers.Ok;
import org.jparsec.containers.MatchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class TakeWhile<T> extends Matcher<List<T>> {

    private final Matcher<T> that;
    private final Predicate<T> fn;

    public TakeWhile(Matcher<T> that, Predicate<T> fn) {
        super("error in takeWhile " + that.toString());
        this.that = that;
        this.fn = fn;
    }

    @Override
    public MatchResult<List<T>> parse(Context ctx) {
        List<T> results = new ArrayList<>();
        Context itCtx = ctx;
        itCtx.traceSnapshot();
        do {
            if (that.parse(itCtx) instanceof Ok(T r, Context newCtx)
                    && fn.test(r)) {
                results.add(r);
                itCtx = newCtx;
            } else {
                break;
            }
        } while (true);

        itCtx.recoverTraceSnapshot();
        return new Ok<>(results, itCtx);
    }

    @Override
    public String toString() {
        return "takeWhile[" + that.toString() + "]";
    }
}
