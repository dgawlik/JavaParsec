package org.jparsec.containers.seq;

import java.util.List;

public record Tuple6<T, U, W, Z, X, Y>(T one, U two, W three, Z four, X five, Y six)
    implements SeqOps {
    @Override
    public String s(String sep) {
        return String.join(sep, List.of(one.toString(), two.toString(),
                three.toString(), four.toString(), five.toString(),
                six.toString()));
    }
}
