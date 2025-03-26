package org.jparsec.containers;

public record Four<T, U, W, Z, X, G, H>(Z value) implements
        Choice4<T, U, W, Z>, Choice5<T, U, W, Z, X>, Choice6<T, U, W, Z, X, G>,
        Choice7<T, U, W, Z, X, G, H>{
}
