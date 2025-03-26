package org.jparsec.combinator;

import java.util.function.Predicate;

public interface Chars {

    static AnyChar any() {
        return new AnyChar();
    }

    static CharsOf anyOf(Character... chars) {
        return new CharsOf(chars);
    }

    static WhitespaceChar whitespace() {
        return new WhitespaceChar();
    }

    static Digit nonZeroDigit() {
        return new Digit(true);
    }

    static Digit digit() {
        return new Digit(false);
    }

    static Satisfy alpha() {
        return new Alphabetic();
    }

    static Satisfy alphaNum() {
        return new AlphaNum();
    }

    static Satisfy lower() {
        return new LowerChar();
    }

    static Satisfy upper() {
        return new UpperChar();
    }

    static Satisfy satisfy(Predicate<Character> pred) {
        return new Satisfy(pred);
    }

    static CharRange range(Character start, Character end) {
        return new CharRange(start, end);
    }

    static NoneOf noneOf(Character... inverseMatchers) {
        return new NoneOf(inverseMatchers);
    }
}
