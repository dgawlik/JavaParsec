package org.jparsec.combinator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NoneOf extends Satisfy {

    private final Character[] matchers;

    public NoneOf(Character... matchers) {
        super(c -> !List.of(matchers).contains(c),
                String.format("none of %s should be matched",
                        join(matchers)));
        this.matchers = matchers;
    }

    @Override
    public String toString() {
        return "not " + join(matchers);
    }

    private static String join(Character[] matchers) {
        return Arrays.stream(matchers).map(Object::toString)
                .collect(Collectors.joining(","));
    }
}
