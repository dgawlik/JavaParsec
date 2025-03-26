package org.jparsec.combinator;

import org.jparsec.Rule;
import org.jparsec.containers.*;

public class Seq2<T, U> extends Rule<Pair<T, U>> {

    private final Rule<T> that;
    private final Rule<U> other;

    public Seq2(Rule<T> that, Rule<U> other) {
        super("error in sequence");
        this.that = that;
        this.other = other;
    }


    @Override
    public ParseResult<Pair<T, U>> parse(ParseContext ctx) {
        var ctxIt = ctx;
        T t = null;
        U u = null;


        switch (that.parse(ctxIt)) {
            case Ok(T val1, ParseContext newCtx) -> {
                ctxIt = newCtx;
                t = val1;
            }
            case Err(String msg, ParseContext newCtx) -> {
                newCtx.addVerboseError(errorMessage);
                newCtx.appendTrace(errorMessage);
                return new Err<>(customError.orElse(msg), newCtx);
            }
        }

        switch (other.parse(ctxIt)) {
            case Ok(U val2, ParseContext newCtx) -> {
                ctxIt = newCtx;
                u = val2;
            }
            case Err(String msg, ParseContext newCtx) -> {
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
