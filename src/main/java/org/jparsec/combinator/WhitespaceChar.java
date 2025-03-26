package org.jparsec.combinator;

public final class WhitespaceChar extends Satisfy {

    public WhitespaceChar() {
        super(Character::isWhitespace,
                "Expected whitespace char");
    }

    @Override
    public String toString() {
        return "<ws>";
    }
}
