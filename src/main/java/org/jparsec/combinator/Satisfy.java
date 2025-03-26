package org.jparsec.combinator;

import org.jparsec.Rule;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.jparsec.containers.ParseContext;
import org.jparsec.containers.ParseResult;

import java.util.function.Predicate;

public class Satisfy extends Rule<Character> {

    private final Predicate<Character> matcher;

    public Satisfy(Predicate<Character> matcher) {
        this(matcher, "Satisfy: predicate fail");
    }

    public Satisfy(Predicate<Character> matcher, String message) {
        super(message);
        this.matcher = matcher;
    }

    @Override
    public String toString() {
        return "<pred>";
    }

    @Override
    public ParseResult<Character> parse(ParseContext ctx) {
        if (ctx.index == ctx.content.length()) {
            ctx.appendTrace("unexpected end of stream");
            ctx.addVerboseError("unexpected end of stream");
            return new Err("unexpected end of stream", ctx);
        } else if (matcher.test(ctx.content.charAt(ctx.index))) {
            var newCtx = ctx.copy();
            if (Character.isWhitespace(ctx.content.charAt(ctx.index))) {
                newCtx.column = -1;
                newCtx.line++;
            }
            newCtx.index++;
            newCtx.column++;
            return new Ok<>(ctx.content.charAt(ctx.index), newCtx);
        } else {
            ctx.appendTrace(errorMessage);
            ctx.addVerboseError(errorMessage);
            return new Err<>(errorMessage, ctx);
        }
    }
}
