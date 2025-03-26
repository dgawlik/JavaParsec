package org.jparsec.combinator;

import org.jparsec.Api;
import org.jparsec.Rule;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.jparsec.containers.ParseContext;
import org.jparsec.containers.ParseResult;

public class Newline extends Rule<Character> {

    public Newline() {
        super("error parsing newline");
    }

    @Override
    public ParseResult<Character> parse(ParseContext ctx) {
        if (Api.anyOf('\n').parse(ctx) instanceof Err e) {
            ctx.appendTrace(e.error());
            ctx.addVerboseError(e.error());
            return e;
        }

        ctx.index++;
        ctx.column = 0;
        ctx.line++;

        if (ctx.indentLevel > 0) {
            int indentCount = 0;
            ParseContext ctxIt = ctx;
            outer:
            while (true) {
                switch (Api.string(ctx.indentPattern).parse(ctxIt)) {
                    case Ok(_, ParseContext newCtx) -> {
                        ctxIt = newCtx;
                        indentCount++;
                    }
                    case Err e -> {
                        break outer;
                    }

                }
            }
            if (indentCount != ctx.indentLevel) {
                var errMsg = String.format("Wrong indentation level - expected %d of %s, found %d instead",
                        ctx.indentLevel, ctx.indentPattern, indentCount);
                ctx.addVerboseError(errMsg);
                ctx.appendTrace(errMsg);
                return new Err<>(errMsg, ctx);
            } else {
                return new Ok<>('\n', ctxIt);
            }
        } else {
            return new Ok<>('\n', ctx);
        }
    }

    @Override
    public String toString() {
        return "<nl>";
    }
}
