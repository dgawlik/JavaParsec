package org.jparsec.combinator;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CharsOf extends Satisfy {

    private Character[] matchers;

    public CharsOf(Character... matchers) {
        this(String.format("%s could not be matched", join(matchers)),
                matchers);
        this.matchers = matchers;
    }

    public CharsOf(String errorMessage, Character... matchers) {
        super(Arrays.asList(matchers)::contains, errorMessage);
    }

    @Override
    public String toString() {
        return join(matchers);
    }

    private static String join(Character[] matchers) {
        return Arrays.stream(matchers).map(Object::toString)
                .collect(Collectors.joining(","));
    }
}
