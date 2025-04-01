package org.jparsec.combinator;

import org.jparsec.Matcher;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.jparsec.containers.Context;
import org.jparsec.containers.MatchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;


public class Many<T> extends Matcher<List<T>> {

    private final boolean isSome;
    private final Matcher<T> inner;

    private Many(Matcher<T> inner, boolean isSome) {
        super(isSome ? "More than zero of " + inner.toString() + " required" : "error in many");
        this.isSome = isSome;
        this.inner = inner;
    }

    public <U> Matcher<U> reduce(U init, BiFunction<T, U, U> reducer) {
        return this.map(lst -> {
            U it = init;
            for (T el : lst) {
                it = reducer.apply(el, it);
            }
            return it;
        });
    }

    public Matcher<String> s() {
        return this.reduce("", (T el, String acc) -> acc + el.toString());
    }

    public static <U> Many<U> many(Matcher<U> inner) {
        return new Many<>(inner, false);
    }

    public static <U> Many<U> some(Matcher<U> inner) {
        return new Many<>(inner, true);
    }

    @Override
    public String toString() {
        return "(" + inner.toString() + ")" + (isSome ? "+" : "*");
    }

    @Override
    public MatchResult<List<T>> parse(Context ctx) {
        List<T> results = new ArrayList<>();
        Context ctxIt = ctx;

        if (isSome) {
            switch (inner.parse(ctxIt)) {
                case Ok(T r, Context newCtx) -> {
                    ctxIt = newCtx;
                    results.add(r);
                }
                case Err e -> {
                    ctxIt.appendTrace(errorMessage);
                    ctxIt.addVerboseError(errorMessage);
                    return e;
                }
            }
        }

        var res = (Ok<List<T>>) inner.takeWhile(x -> true).parse(ctxIt);

        results.addAll(res.value());
        return new Ok<>(results, res.ctx());
    }
}
