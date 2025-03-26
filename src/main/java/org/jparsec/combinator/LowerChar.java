package org.jparsec.combinator;

public class LowerChar extends Satisfy {

    public LowerChar() {
        super(Character::isLowerCase, "Expected lowercase char");
    }

    @Override
    public String toString() {
        return "[a-z]";
    }
}
