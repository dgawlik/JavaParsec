package org.jparsec.containers;

import java.util.Optional;

public sealed interface Choice3<T, U, W> permits One, Two, Three {

    default Optional<T> one() {
        if (this instanceof One(T value)) {
            return Optional.of(value);
        } else {
            return Optional.empty();
        }
    }

    default Optional<U> two() {
        if (this instanceof Two(U value)) {
            return Optional.of(value);
        } else {
            return Optional.empty();
        }
    }

    default Optional<W> three() {
        if (this instanceof Three(W value)) {
            return Optional.of(value);
        } else {
            return Optional.empty();
        }
    }
}
