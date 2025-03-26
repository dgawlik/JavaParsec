package org.jparsec.combinator;

import org.jparsec.Api;
import org.jparsec.Rule;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.jparsec.containers.ParseContext;
import org.jparsec.containers.ParseResult;

public class Indent<T> extends Rule<T> {

    private final Rule<T> inner;
    private final String indentPattern;

    public Indent(Rule<T> inner, String indentPattern) {
        super("indentation error");
        this.indentPattern = indentPattern;
        this.inner = inner;
    }

    @Override
    public ParseResult<T> parse(ParseContext ctx) {
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
            case Ok(T r, ParseContext newCtx) -> {
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
