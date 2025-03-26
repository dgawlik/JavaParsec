package org.jparsec.combinator;

import org.jparsec.Rule;
import org.jparsec.containers.*;
import org.jparsec.containers.Either.Left;
import org.jparsec.containers.Either.Right;

public interface Choice {

    static <T, U> Rule<Either<T, U>> choice(Rule<T> c1, Rule<U> c2) {
        return c1.or(c2);
    }

    static <T, U, W> Rule<Choice3<T, U, W>> choice(Rule<T> c1, Rule<U> c2, Rule<W> c3) {
        return c1.or(c2).or(c3).map(nested -> switch (nested) {
            case Left(Left(T val)) -> new One<>(val);
            case Left(Right(U val)) -> new Two<>(val);
            case Right(W val) -> new Three<>(val);
        });
    }

    static <T, U, W, Z> Rule<Choice4<T, U, W, Z>> choice(Rule<T> c1, Rule<U> c2,
                                                         Rule<W> c3, Rule<Z> c4) {
        return c1.or(c2).or(c3).or(c4).map(nested -> switch (nested) {
            case Left(Left(Left(T val))) -> new One<>(val);
            case Left(Left(Right(U val))) -> new Two<>(val);
            case Left(Right(W val)) -> new Three<>(val);
            case Right(Z val) -> new Four<>(val);
        });
    }

    static <T, U, W, Z, X> Rule<Choice5<T, U, W, Z, X>> choice(Rule<T> c1, Rule<U> c2,
                                                               Rule<W> c3, Rule<Z> c4,
                                                               Rule<X> c5) {
        return c1.or(c2).or(c3).or(c4).or(c5).map(nested -> switch (nested) {
            case Left(Left(Left(Left(T val)))) -> new One<>(val);
            case Left(Left(Left(Right(U val)))) -> new Two<>(val);
            case Left(Left(Right(W val))) -> new Three<>(val);
            case Left(Right(Z val)) -> new Four<>(val);
            case Right(X val) -> new Five<>(val);
        });
    }

    static <T, U, W, Z, X, G> Rule<Choice6<T, U, W, Z, X, G>> choice(Rule<T> c1, Rule<U> c2,
                                                                     Rule<W> c3, Rule<Z> c4,
                                                                     Rule<X> c5, Rule<G> c6) {
        return c1.or(c2).or(c3).or(c4).or(c5).or(c6).map(nested -> switch (nested) {
            case Left(Left(Left(Left(Left(T val))))) -> new One<>(val);
            case Left(Left(Left(Left(Right(U val))))) -> new Two<>(val);
            case Left(Left(Left(Right(W val)))) -> new Three<>(val);
            case Left(Left(Right(Z val))) -> new Four<>(val);
            case Left(Right(X val)) -> new Five<>(val);
            case Right(G val) -> new Six<>(val);
        });
    }

    static <T, U, W, Z, X, G, H> Rule<Choice7<T, U, W, Z, X, G, H>> choice(Rule<T> c1, Rule<U> c2,
                                                                           Rule<W> c3, Rule<Z> c4,
                                                                           Rule<X> c5, Rule<G> c6,
                                                                           Rule<H> c7) {
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
