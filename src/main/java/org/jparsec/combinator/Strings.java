package org.jparsec.combinator;

public interface Strings {

    static Str c(String pattern) {
        return new Str(pattern, false);
    }

    static Str stringIgnoreCase(String pattern) {
        return new Str(pattern, true);
    }
}
