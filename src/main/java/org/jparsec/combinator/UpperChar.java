package org.jparsec.combinator;

public class UpperChar extends Satisfy {

    public UpperChar() {
        super(Character::isUpperCase, "Expected uppercase char");
    }

    @Override
    public String toString() {
        return "[A-Z]";
    }
}
