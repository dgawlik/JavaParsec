package org.jparsec.combinator;

public class AlphaNum extends Satisfy {

    public AlphaNum() {
        super(Character::isLetterOrDigit, "Expected alphanum char");
    }

    @Override
    public String toString() {
        return "[0-9a-zA-Z]";
    }
}
