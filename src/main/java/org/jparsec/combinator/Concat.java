package org.jparsec.combinator;

import org.jparsec.Matcher;
import org.jparsec.Ops;
import org.jparsec.containers.Context;
import org.jparsec.containers.MatchResult;

public class Concat extends Matcher<String> {

    private final Matcher<String> inner;
    private final Matcher<String> other;

    public Concat(Matcher<String> inner, Matcher<String> other) {
        super("error in concat");
        this.inner = inner;
        this.other = other;
    }

    public Concat(Matcher<String> inner) {
        this(inner, null);
    }

    public Concat join(Satisfy ch) {
        return new Concat(this, ch.map(Ops::toString));
    }

    public Concat join(Str str) {
        return new Concat(this, str);
    }

    @Override
    public MatchResult<String> parse(Context ctx) {
        if (other != null) {
            return inner.seq(other)
                    .map(p -> p.first() + p.second())
                    .parse(ctx);
        } else {
            return inner.parse(ctx);
        }
    }
}
