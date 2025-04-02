package org.jparsec.combinator;

import org.jparsec.Matcher;
import org.jparsec.containers.*;
import org.jparsec.containers.seq.*;

public interface Seq {

    class SeqX<T extends SeqOps> extends Matcher<T> {

        private final Matcher<T> inner;

        public SeqX(Matcher<T> inner) {
            super("error in SeqOps");
            this.inner = inner;
        }

        public Matcher<String> s() {
            return inner.map(ops -> ops.s(""));
        }

        @Override
        public MatchResult<T> parse(Context ctx) {
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

    static <T, U> SeqX<Pair<T, U>> seq(Matcher<T> one, Matcher<U> two) {
        return new SeqX<>(one.seq(two));
    }

    static <T, U, W> SeqX<Tuple3<T, U, W>> seq(Matcher<T> one, Matcher<U> two, Matcher<W> three) {
        return new SeqX<>(one
                .seq(two)
                .seq(three)
                .map(nested -> new Tuple3<>(nested.first().first(),
                        nested.first().second(),
                        nested.second())));
    }

    static <T, U, W, Z> SeqX<Tuple4<T, U, W, Z>> seq(Matcher<T> one, Matcher<U> two,
                                                     Matcher<W> three, Matcher<Z> four) {
        return new SeqX<>(one
                .seq(two)
                .seq(three)
                .seq(four)
                .map(nested -> new Tuple4<>(nested.first().first().first(),
                        nested.first().first().second(),
                        nested.first().second(),
                        nested.second())));
    }

    static <T, U, W, Z, Y> SeqX<Tuple5<T, U, W, Z, Y>> seq(Matcher<T> one, Matcher<U> two,
                                                           Matcher<W> three, Matcher<Z> four,
                                                           Matcher<Y> five) {
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

    static <T, U, W, Z, Y, X> SeqX<Tuple6<T, U, W, Z, Y, X>> seq(Matcher<T> one, Matcher<U> two,
                                                                 Matcher<W> three, Matcher<Z> four,
                                                                 Matcher<Y> five, Matcher<X> six) {
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

    static <T, U, W, Z, Y, X, G> SeqX<Tuple7<T, U, W, Z, Y, X, G>> seq(Matcher<T> one, Matcher<U> two,
                                                                       Matcher<W> three, Matcher<Z> four,
                                                                       Matcher<Y> five, Matcher<X> six,
                                                                       Matcher<G> seven) {
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
