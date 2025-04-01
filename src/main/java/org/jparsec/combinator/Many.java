package org.jparsec.combinator;

import org.jparsec.Rule;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.jparsec.containers.ParseContext;
import org.jparsec.containers.ParseResult;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;


public class Many<T> extends Rule<List<T>> {

    private final boolean isSome;
    private final Rule<T> inner;

    private Many(Rule<T> inner, boolean isSome) {
        super(isSome ? "More than zero of " + inner.toString() + " required" : "error in many");
        this.isSome = isSome;
        this.inner = inner;
    }

    public <U> Rule<U> reduce(U init, BiFunction<T, U, U> reducer) {
        return this.map(lst -> {
            U it = init;
            for (T el : lst) {
                it = reducer.apply(el, it);
            }
            return it;
        });
    }

    public Rule<String> s() {
        return this.reduce("", (T el, String acc) -> acc + el.toString());
    }

    public static <U> Many<U> many(Rule<U> inner) {
        return new Many<>(inner, false);
    }

    public static <U> Many<U> some(Rule<U> inner) {
        return new Many<>(inner, true);
    }

    @Override
    public String toString() {
        return "(" + inner.toString() + ")" + (isSome ? "+" : "*");
    }

    @Override
    public ParseResult<List<T>> parse(ParseContext ctx) {
        List<T> results = new ArrayList<>();
        ParseContext ctxIt = ctx;

        if (isSome) {
            switch (inner.parse(ctxIt)) {
                case Ok(T r, ParseContext newCtx) -> {
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
