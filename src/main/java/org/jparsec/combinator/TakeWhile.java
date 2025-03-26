package org.jparsec.combinator;

import org.jparsec.Rule;
import org.jparsec.containers.Ok;
import org.jparsec.containers.ParseContext;
import org.jparsec.containers.ParseResult;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class TakeWhile<T> extends Rule<List<T>> {

    private final Rule<T> that;
    private final Predicate<T> fn;

    public TakeWhile(Rule<T> that, Predicate<T> fn) {
        super("error in takeWhile " + that.toString());
        this.that = that;
        this.fn = fn;
    }

    @Override
    public ParseResult<List<T>> parse(ParseContext ctx) {
        List<T> results = new ArrayList<>();
        ParseContext itCtx = ctx;
        itCtx.traceSnapshot();
        do {
            if (that.parse(itCtx) instanceof Ok(T r, ParseContext newCtx)
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
