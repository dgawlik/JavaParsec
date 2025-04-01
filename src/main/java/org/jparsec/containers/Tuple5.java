package org.jparsec.containers;

import java.util.List;

public record Tuple5<T, U, W, Z, X>(T one, U two, W three, Z four, X five)
    implements SeqOps {
    @Override
    public String s(String sep) {
        return String.join(sep, List.of(one.toString(), two.toString(),
                three.toString(), four.toString(), five.toString()));
    }
}
