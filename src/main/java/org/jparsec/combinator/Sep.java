package org.jparsec.combinator;

import org.jparsec.Rule;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.jparsec.containers.ParseContext;
import org.jparsec.containers.ParseResult;

import java.util.ArrayList;
import java.util.List;


public class Sep<T, U> extends Rule<List<T>> {

    private final Rule<T> inner;
    private final Rule<U> sep;

    private Sep(Rule<T> inner, Rule<U> sep) {
        super(inner.toString() + " could not be separated by " + sep.toString());
        this.sep = sep;
        this.inner = inner;
    }

    public static <V, U> Rule<List<V>> sepBy(Rule<V> inner, Rule<U> sep) {
        return new Sep<>(inner, sep);
    }


    @Override
    public String toString() {
        return "sepBy " + sep.toString();
    }

    @Override
    public ParseResult<List<T>> parse(ParseContext ctx) {
        var lst = new ArrayList<T>();
        var ctxIt = ctx;

        var head = inner.parse(ctxIt);
        switch (head) {
            case Err e -> {
                ctxIt.appendTrace("error in "+this);
                ctxIt.addVerboseError("error in "+this);
                return e;
            }
            case Ok(T result, ParseContext newCtx) -> {
                lst.add(result);
                ctxIt = newCtx;
            }
        }

        outer:
        while (true) {
            ParseContext beforeSep = ctxIt;
            switch (sep.parse(ctxIt)) {
                case Err e -> {
                    ctxIt.addVerboseError("error in "+this);
                    break outer;
                }
                case Ok(Object r, ParseContext newCtx) -> {
                    ctxIt = newCtx;
                }
            }

            switch (inner.parse(ctxIt)) {
                case Err e -> {
                    ctxIt.addVerboseError("error in "+this);
                    ctxIt = beforeSep;
                    break outer;
                }
                case Ok(T result, ParseContext newCtx) -> {
                    lst.add(result);
                    ctxIt = newCtx;
                }
            }
        }

        ctxIt.clearTrace();
        return new Ok<>(lst, ctxIt);
    }
}
