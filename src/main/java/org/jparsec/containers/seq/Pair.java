package org.jparsec.containers.seq;

import java.util.List;

public record Pair<T, U>(T first, U second) implements SeqOps {

    @Override
    public String s(String sep) {
        return String.join(sep, List.of(first.toString(), second.toString()));
    }
}
