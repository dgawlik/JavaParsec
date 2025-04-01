package org.jparsec.combinator;

import org.jparsec.Matcher;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.jparsec.containers.Context;
import org.jparsec.containers.MatchResult;

public class Indent<T> extends Matcher<T> {

    private final Matcher<T> inner;
    private final String indentPattern;

    public Indent(Matcher<T> inner, String indentPattern) {
        super("indentation error");
        this.indentPattern = indentPattern;
        this.inner = inner;
    }

    @Override
    public MatchResult<T> parse(Context ctx) {
        String prevIndentPattern = ctx.indentPattern;
        ctx.indentLevel++;
        ctx.indentPattern = indentPattern;

        return switch (inner.parse(ctx)) {
            case Err e -> {
                ctx.indentLevel--;
                ctx.indentPattern = prevIndentPattern;
                ctx.appendTrace(errorMessage);
                ctx.addVerboseError(errorMessage);
                yield e;
            }
            case Ok(T r, Context newCtx) -> {
                newCtx.indentLevel--;
                newCtx.indentPattern = prevIndentPattern;
                yield new Ok<>(r, newCtx);
            }
        };
    }

    @Override
    public String toString() {
        return "<indent>";
    }
}
