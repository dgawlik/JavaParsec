package org.jparsec.combinator;

import org.jparsec.Rule;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.jparsec.containers.ParseContext;
import org.jparsec.containers.ParseResult;

import java.util.List;


public class Times<T> extends Rule<List<T>> {

    private final Rule<T> inner;
    private final int from;
    private final int to;

    private Times(Rule<T> inner, int times) {
        this(inner, times, times);
    }

    private Times(Rule<T> inner, int from, int to) {
        super((from == to
                ? String.valueOf(from) : from + " to " + to)
                + " occurences of" + inner.toString() + " required");
        this.inner = inner;
        this.from = from;
        this.to = to;
    }

    public static <U> Rule<List<U>> times(Rule<U> inner, int times) {
        return new Times<>(inner, times);
    }

    public static <U> Rule<List<U>> times(Rule<U> inner, int from, int to) {
        return new Times<>(inner, from, to);
    }

    @Override
    public String toString() {
        return "(" + inner.toString() + ")"
                + (from == to ? "{" + from + "}" : "{" + from + "," + to + "}");
    }

    @Override
    public ParseResult<List<T>> parse(ParseContext ctx) {
        var res = (Ok<List<T>>) inner.takeWhile(x -> true).parse(ctx);

        if (res.value().size() < from || res.value().size() > to) {
            ctx.addVerboseError(errorMessage);
            ctx.appendTrace(errorMessage);
            return new Err<>(errorMessage, ctx);
        } else {
            ctx.clearTrace();
            return res;
        }
    }
}
