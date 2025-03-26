package org.jparsec.containers;

public record One<T, U, W, Z, X, G, H>(T value) implements Choice3<T, U, W> ,
     Choice4<T, U, W, Z>, Choice5<T, U, W, Z, X>, Choice6<T, U, W, Z, X, G>,
     Choice7<T, U, W, Z, X, G, H>{
}
