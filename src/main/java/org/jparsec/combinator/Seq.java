package org.jparsec.combinator;

import org.jparsec.Rule;
import org.jparsec.containers.*;

public interface Seq {

    class SeqX<T extends SeqOps> extends Rule<T> {

        private final Rule<T> inner;

        public SeqX(Rule<T> inner) {
            super("error in SeqOps");
            this.inner = inner;
        }

        public Rule<String> s() {
            return inner.map(ops -> ops.s(""));
        }

        @Override
        public ParseResult<T> parse(ParseContext ctx) {
            return this.inner.parse(ctx);
        }

        @Override
        public String toString() {
            return inner.toString();
        }

        @Override
        public SeqX<T> setInternalDescription(String desc) {
            inner.setInternalDescription(desc);
            return this;
        }

        @Override
        public SeqX<T> setErrorMessage(String errMsg) {
            inner.setErrorMessage(errMsg);
            return this;
        }
    }

    static <T, U> SeqX<Pair<T, U>> seq(Rule<T> one, Rule<U> two) {
        return new SeqX<>(one.seq(two));
    }

    static <T, U, W> SeqX<Tuple3<T, U, W>> seq(Rule<T> one, Rule<U> two, Rule<W> three) {
        return new SeqX<>(one
                .seq(two)
                .seq(three)
                .map(nested -> new Tuple3<>(nested.first().first(),
                        nested.first().second(),
                        nested.second())));
    }

    static <T, U, W, Z> SeqX<Tuple4<T, U, W, Z>> seq(Rule<T> one, Rule<U> two,
                                                     Rule<W> three, Rule<Z> four) {
        return new SeqX<>(one
                .seq(two)
                .seq(three)
                .seq(four)
                .map(nested -> new Tuple4<>(nested.first().first().first(),
                        nested.first().first().second(),
                        nested.first().second(),
                        nested.second())));
    }

    static <T, U, W, Z, Y> SeqX<Tuple5<T, U, W, Z, Y>> seq(Rule<T> one, Rule<U> two,
                                                           Rule<W> three, Rule<Z> four,
                                                           Rule<Y> five) {
        return new SeqX<>(one
                .seq(two)
                .seq(three)
                .seq(four)
                .seq(five)
                .map(nested -> new Tuple5<>(nested.first().first().first().first(),
                        nested.first().first().first().second(),
                        nested.first().first().second(),
                        nested.first().second(),
                        nested.second())));
    }

    static <T, U, W, Z, Y, X> SeqX<Tuple6<T, U, W, Z, Y, X>> seq(Rule<T> one, Rule<U> two,
                                                                 Rule<W> three, Rule<Z> four,
                                                                 Rule<Y> five, Rule<X> six) {
        return new SeqX<>(one
                .seq(two)
                .seq(three)
                .seq(four)
                .seq(five)
                .seq(six)
                .map(nested -> new Tuple6<>(
                        nested.first().first().first().first().first(),
                        nested.first().first().first().first().second(),
                        nested.first().first().first().second(),
                        nested.first().first().second(),
                        nested.first().second(),
                        nested.second())));
    }

    static <T, U, W, Z, Y, X, G> SeqX<Tuple7<T, U, W, Z, Y, X, G>> seq(Rule<T> one, Rule<U> two,
                                                                       Rule<W> three, Rule<Z> four,
                                                                       Rule<Y> five, Rule<X> six,
                                                                       Rule<G> seven) {
        return new SeqX<>(one
                .seq(two)
                .seq(three)
                .seq(four)
                .seq(five)
                .seq(six)
                .seq(seven)
                .map(nested -> new Tuple7<>(
                        nested.first().first().first().first().first().first(),
                        nested.first().first().first().first().first().second(),
                        nested.first().first().first().first().second(),
                        nested.first().first().first().second(),
                        nested.first().first().second(),
                        nested.first().second(),
                        nested.second())));
    }
}
