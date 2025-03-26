package org.jparsec.containers;

public record Five<T, U, W, Z, X, G, H>(X value) implements
        Choice5<T, U, W, Z, X>, Choice6<T, U, W, Z, X, G>,
        Choice7<T, U, W, Z, X, G, H> {
}
