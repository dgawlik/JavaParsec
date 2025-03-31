package org.jparsec.combinator;

import org.jparsec.Ops;
import org.jparsec.Rule;
import org.jparsec.containers.ParseContext;
import org.jparsec.containers.ParseResult;

public class Concat extends Rule<String> {

    private final Rule<String> inner;
    private final Rule<String> other;

    public Concat(Rule<String> inner, Rule<String> other) {
        super("error in concat");
        this.inner = inner;
        this.other = other;
    }

    public Concat(Rule<String> inner) {
        this(inner, null);
    }

    public Concat join(Satisfy ch) {
        return new Concat(this, ch.map(Ops::toString));
    }

    public Concat join(Str str) {
        return new Concat(this, str);
    }

    @Override
    public ParseResult<String> parse(ParseContext ctx) {
        if (other != null) {
            return inner.seq(other)
                    .map(p -> p.first() + p.second())
                    .parse(ctx);
        } else {
            return inner.parse(ctx);
        }
    }
}
