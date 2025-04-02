package org.jparsec.combinator;

import org.jparsec.Matcher;
import org.jparsec.containers.*;
import org.jparsec.containers.seq.Pair;

public class Seq2<T, U> extends Matcher<Pair<T, U>> {

    private final Matcher<T> that;
    private final Matcher<U> other;

    public Seq2(Matcher<T> that, Matcher<U> other) {
        super("error in sequence");
        this.that = that;
        this.other = other;
    }

    public Matcher<String> s() {
        return this.map(p -> p.str(""));
    }


    @Override
    public MatchResult<Pair<T, U>> parse(Context ctx) {
        var ctxIt = ctx;
        T t = null;
        U u = null;


        switch (that.parse(ctxIt)) {
            case Ok(T val1, Context newCtx) -> {
                ctxIt = newCtx;
                t = val1;
            }
            case Err(String msg, Context newCtx) -> {
                newCtx.addVerboseError(errorMessage);
                newCtx.appendTrace(errorMessage);
                return new Err<>(customError.orElse(msg), newCtx);
            }
        }

        switch (other.parse(ctxIt)) {
            case Ok(U val2, Context newCtx) -> {
                ctxIt = newCtx;
                u = val2;
            }
            case Err(String msg, Context newCtx) -> {
                newCtx.addVerboseError(errorMessage);
                newCtx.appendTrace(errorMessage);
                return new Err<>(customError.orElse(msg), newCtx);
            }
        }

        return new Ok<>(new Pair<>(t, u), ctxIt);
    }

    @Override
    public String toString() {
        return internalDescription.orElse(that.toString() + "::" + other.toString());
    }
}
