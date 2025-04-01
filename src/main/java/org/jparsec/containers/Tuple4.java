package org.jparsec.containers;

import java.util.List;

public record Tuple4<T, U, W, Z>(T one, U two, W three, Z four) implements SeqOps {
    @Override
    public String s(String sep) {
        return String.join(sep, List.of(one.toString(), two.toString(),
                three.toString(), four.toString()));
    }
}
