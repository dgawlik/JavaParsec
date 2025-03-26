package org.jparsec.combinator;

import java.util.stream.Stream;

public class CharRange extends CharsOf {

    private final Character start;
    private final Character end;

    public CharRange(Character start, Character end) {
        super(rangeCtor(start, end));
        this.start = start;
        this.end = end;
    }

    private static Character[] rangeCtor(Character start, Character end) {
        return Stream.iterate(start, c -> c <= end, c -> (char) (c + 1))
                .toList().toArray(new Character[0]);
    }

    @Override
    public String toString() {
        return "[" + start + ".." + end + "]";
    }
}
