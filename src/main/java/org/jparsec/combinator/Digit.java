package org.jparsec.combinator;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Digit extends CharRange {

    private final boolean nonZero;

    public Digit(boolean nonZero) {
        super(nonZero ? '1' : '0', '9');
        this.nonZero = nonZero;
    }

    @Override
    public String toString() {
        return Stream.iterate(nonZero ? '1' : '0',
                        (Character it) -> it <= '9',
                        (Character it) -> (char) (it + 1)
                ).map(x -> "" + x)
                .collect(Collectors.joining(","));
    }
}
