package org.jparsec.combinator;

import org.jparsec.Matcher;
import org.jparsec.containers.Context;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.jparsec.containers.MatchResult;

import java.util.ArrayList;
import java.util.List;


public class Sep<T, U> extends Matcher<List<T>> {

    private final Matcher<T> inner;
    private final Matcher<U> sep;

    private Sep(Matcher<T> inner, Matcher<U> sep) {
        super(inner.toString() + " could not be separated by " + sep.toString());
        this.sep = sep;
        this.inner = inner;
    }

    public static <V, U> Matcher<List<V>> sepBy(Matcher<V> inner, Matcher<U> sep) {
        return new Sep<>(inner, sep);
    }


    @Override
    public String toString() {
        return "sepBy " + sep.toString();
    }

    @Override
    public MatchResult<List<T>> parse(Context ctx) {
        var lst = new ArrayList<T>();
        var ctxIt = ctx;

        var head = inner.parse(ctxIt);
        switch (head) {
            case Err e -> {
                ctxIt.appendTrace("error in "+this);
                ctxIt.addVerboseError("error in "+this);
                return e;
            }
            case Ok(T result, Context newCtx) -> {
                lst.add(result);
                ctxIt = newCtx;
            }
        }

        outer:
        while (true) {
            Context beforeSep = ctxIt;
            switch (sep.parse(ctxIt)) {
                case Err e -> {
                    ctxIt.addVerboseError("error in "+this);
                    break outer;
                }
                case Ok(Object r, Context newCtx) -> {
                    ctxIt = newCtx;
                }
            }

            switch (inner.parse(ctxIt)) {
                case Err e -> {
                    ctxIt.addVerboseError("error in "+this);
                    ctxIt = beforeSep;
                    break outer;
                }
                case Ok(T result, Context newCtx) -> {
                    lst.add(result);
                    ctxIt = newCtx;
                }
            }
        }

        ctxIt.clearTrace();
        return new Ok<>(lst, ctxIt);
    }
}
