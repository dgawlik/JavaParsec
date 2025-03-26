package org.jparsec.combinator;

import org.jparsec.Rule;
import org.jparsec.containers.*;

public interface Seq {

    static <T, U> Rule<Pair<T, U>> seq(Rule<T> one, Rule<U> two) {
        return one.seq(two);
    }

    static <T, U, W> Rule<Tuple3<T, U, W>> seq(Rule<T> one, Rule<U> two, Rule<W> three) {
        return one
                .seq(two)
                .seq(three)
                .map(nested -> new Tuple3<>(nested.first().first(),
                        nested.first().second(),
                        nested.second()));
    }

    static <T, U, W, Z> Rule<Tuple4<T, U, W, Z>> seq(Rule<T> one, Rule<U> two,
                                                     Rule<W> three, Rule<Z> four) {
        return one
                .seq(two)
                .seq(three)
                .seq(four)
                .map(nested -> new Tuple4<>(nested.first().first().first(),
                        nested.first().first().second(),
                        nested.first().second(),
                        nested.second()));
    }

    static <T, U, W, Z, Y> Rule<Tuple5<T, U, W, Z, Y>> seq(Rule<T> one, Rule<U> two,
                                                           Rule<W> three, Rule<Z> four,
                                                           Rule<Y> five) {
        return one
                .seq(two)
                .seq(three)
                .seq(four)
                .seq(five)
                .map(nested -> new Tuple5<>(nested.first().first().first().first(),
                        nested.first().first().first().second(),
                        nested.first().first().second(),
                        nested.first().second(),
                        nested.second()));
    }

    static <T, U, W, Z, Y, X> Rule<Tuple6<T, U, W, Z, Y, X>> seq(Rule<T> one, Rule<U> two,
                                                                 Rule<W> three, Rule<Z> four,
                                                                 Rule<Y> five, Rule<X> six) {
        return one
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
                        nested.second()));
    }

    static <T, U, W, Z, Y, X, G> Rule<Tuple7<T, U, W, Z, Y, X, G>> seq(Rule<T> one, Rule<U> two,
                                                                       Rule<W> three, Rule<Z> four,
                                                                       Rule<Y> five, Rule<X> six,
                                                                       Rule<G> seven) {
        return one
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
                        nested.second()));
    }
}
