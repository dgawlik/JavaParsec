package org.jparsec.combinator;

import org.jparsec.Ops;
import org.jparsec.Rule;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.jparsec.containers.ParseContext;
import org.jparsec.containers.ParseResult;

import java.util.function.Predicate;

public class Satisfy extends Rule<Character> {

    private final Predicate<Character> matcher;
    private final String toString;

    public Satisfy(Predicate<Character> matcher) {
        this(matcher, "Satisfy: predicate fail");
    }

    public Satisfy(Predicate<Character> matcher, String message) {
        this(matcher, message, "<predicate>");
    }

    public Satisfy(Predicate<Character> matcher, String message, String toString) {
        super(message);
        this.matcher = matcher;
        this.toString = toString;
    }

    public Concat join(Satisfy c) {
        return new Concat(this.map(Ops::toString), c.map(Ops::toString));
    }

    public Rule<String> s() {
        return this.map(Object::toString);
    }

    public Concat join(Str s) {
        return new Concat(this.map(Ops::toString), s);
    }

    @Override
    public String toString() {
        return toString;
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
