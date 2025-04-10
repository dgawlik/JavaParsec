package org.jparsec.containers.seq;

import java.util.List;

public record Tuple7<T, U, W, Z, X, Y, G>(T one, U two, W three, Z four, X five,
                                          Y six, G seven) implements SeqOps {
    @Override
    public String str(String sep) {
        return String.join(sep, List.of(one.toString(), two.toString(),
                three.toString(), four.toString(), five.toString(),
                six.toString(), seven.toString()));
    }
}
