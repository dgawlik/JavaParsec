package org.jparsec.combinator;

public class AnyChar extends Satisfy {

    public AnyChar() {
        super(c -> true, "");
    }

    @Override
    public String toString() {
        return "any char";
    }
}
