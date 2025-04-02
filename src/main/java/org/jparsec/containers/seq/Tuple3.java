package org.jparsec.containers.seq;

import java.util.List;

public record Tuple3<T, U, W>(T one, U two, W three) implements SeqOps {
    @Override
    public String str(String sep) {
        return String.join(sep, List.of(one.toString(), two.toString(),
                three.toString()));
    }
}
