package org.jparsec.combinator;

public class Alphabetic extends Satisfy {

    public Alphabetic() {
        super(Character::isAlphabetic, "Expected alpha char");
    }

    @Override
    public String toString() {
        return "[a-zA-Z]";
    }
}
