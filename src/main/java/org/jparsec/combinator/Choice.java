package org.jparsec.combinator;

import org.jparsec.Matcher;
import org.jparsec.containers.choice.*;
import org.jparsec.containers.choice.Either.Left;
import org.jparsec.containers.choice.Either.Right;

public interface Choice {

    static <T, U> Matcher<Either<T, U>> choice(Matcher<T> c1, Matcher<U> c2) {
        return c1.or(c2);
    }

    static <T, U, W> Matcher<Choice3<T, U, W>> choice(Matcher<T> c1, Matcher<U> c2, Matcher<W> c3) {
        return c1.or(c2).or(c3).map(nested -> switch (nested) {
            case Left(Left(T val)) -> new One<>(val);
            case Left(Right(U val)) -> new Two<>(val);
            case Right(W val) -> new Three<>(val);
        });
    }

    static <T, U, W, Z> Matcher<Choice4<T, U, W, Z>> choice(Matcher<T> c1, Matcher<U> c2,
                                                            Matcher<W> c3, Matcher<Z> c4) {
        return c1.or(c2).or(c3).or(c4).map(nested -> switch (nested) {
            case Left(Left(Left(T val))) -> new One<>(val);
            case Left(Left(Right(U val))) -> new Two<>(val);
            case Left(Right(W val)) -> new Three<>(val);
            case Right(Z val) -> new Four<>(val);
        });
    }

    static <T, U, W, Z, X> Matcher<Choice5<T, U, W, Z, X>> choice(Matcher<T> c1, Matcher<U> c2,
                                                                  Matcher<W> c3, Matcher<Z> c4,
                                                                  Matcher<X> c5) {
        return c1.or(c2).or(c3).or(c4).or(c5).map(nested -> switch (nested) {
            case Left(Left(Left(Left(T val)))) -> new One<>(val);
            case Left(Left(Left(Right(U val)))) -> new Two<>(val);
            case Left(Left(Right(W val))) -> new Three<>(val);
            case Left(Right(Z val)) -> new Four<>(val);
            case Right(X val) -> new Five<>(val);
        });
    }

    static <T, U, W, Z, X, G> Matcher<Choice6<T, U, W, Z, X, G>> choice(Matcher<T> c1, Matcher<U> c2,
                                                                        Matcher<W> c3, Matcher<Z> c4,
                                                                        Matcher<X> c5, Matcher<G> c6) {
        return c1.or(c2).or(c3).or(c4).or(c5).or(c6).map(nested -> switch (nested) {
            case Left(Left(Left(Left(Left(T val))))) -> new One<>(val);
            case Left(Left(Left(Left(Right(U val))))) -> new Two<>(val);
            case Left(Left(Left(Right(W val)))) -> new Three<>(val);
            case Left(Left(Right(Z val))) -> new Four<>(val);
            case Left(Right(X val)) -> new Five<>(val);
            case Right(G val) -> new Six<>(val);
        });
    }

    static <T, U, W, Z, X, G, H> Matcher<Choice7<T, U, W, Z, X, G, H>> choice(Matcher<T> c1, Matcher<U> c2,
                                                                              Matcher<W> c3, Matcher<Z> c4,
                                                                              Matcher<X> c5, Matcher<G> c6,
                                                                              Matcher<H> c7) {
        return c1.or(c2).or(c3).or(c4).or(c5).or(c6).or(c7).map(nested -> switch (nested) {
            case Left(Left(Left(Left(Left(Left(T val)))))) -> new One<>(val);
            case Left(Left(Left(Left(Left(Right(U val)))))) -> new Two<>(val);
            case Left(Left(Left(Left(Right(W val))))) -> new Three<>(val);
            case Left(Left(Left(Right(Z val)))) -> new Four<>(val);
            case Left(Left(Right(X val))) -> new Five<>(val);
            case Left(Right(G val)) -> new Six<>(val);
            case Right(H val) -> new Seven<>(val);
        });
    }
}
