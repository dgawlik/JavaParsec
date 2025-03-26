package org.jparsec.containers;

public record Six<T, U, W, Z, X, G, H>(G value) implements Choice6<T, U, W, Z, X, G>,
        Choice7<T, U, W, Z, X, G, H> {
}
