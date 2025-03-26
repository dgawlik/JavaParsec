package org.jparsec.combinator;

public interface Strings {

    static Str string(String pattern) {
        return new Str(pattern, false);
    }

    static Str stringIgnoreCase(String pattern) {
        return new Str(pattern, true);
    }
}
