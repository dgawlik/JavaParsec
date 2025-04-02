package org.jparsec.containers.choice;


import java.util.Optional;

public sealed interface Either<T, U> {

    record Left<T, V>(T value) implements Either<T, V> {
    }

    record Right<U, V>(U value) implements Either<V, U> {
    }

    default Optional<T> left() {
        if (this instanceof Left(T value)) {
            return Optional.of(value);
        } else {
            return Optional.empty();
        }
    }

    default Optional<U> right() {
        if (this instanceof Right(U value)) {
            return Optional.of(value);
        } else {
            return Optional.empty();
        }
    }
}
