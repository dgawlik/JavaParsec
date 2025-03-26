package org.jparsec.combinator;

import org.jparsec.Rule;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.jparsec.containers.ParseContext;
import org.jparsec.containers.ParseResult;

public class Str extends Rule<String> {

    private final String pattern;
    private final boolean caseInsensitive;

    public Str(String pattern, boolean caseInsensitive) {
        super("expected \"" + pattern + "\"");
        this.pattern = pattern;
        this.caseInsensitive = caseInsensitive;
    }

    @Override
    public String toString() {
        return "\"" + pattern + "\"";
    }

    @Override
    public ParseResult<String> parse(ParseContext ctx) {
        if (ctx.index + pattern.length() > ctx.content.length()) {
            ctx.appendTrace("unexpected end of stream");
            ctx.addVerboseError("unexpected end of stream");
            return new Err<>("unexpected end of stream", ctx);
        } else if (!matches(ctx)) {
            ctx.appendTrace(errorMessage);
            ctx.addVerboseError(errorMessage);
            return new Err<>(errorMessage, ctx);
        } else {
            var newCtx = ctx.copy();
            newCtx.column += pattern.length();
            newCtx.index += pattern.length();

            return new Ok<>(ctx.content.substring(
                    ctx.index,
                    ctx.index + pattern.length()
            ), newCtx);
        }
    }

    private boolean matches(ParseContext ctx) {
        var target = ctx.content.substring(
                ctx.index,
                ctx.index + pattern.length()
        );

        if (caseInsensitive) {
            return target.equalsIgnoreCase(pattern);
        } else {
            return target.equals(pattern);
        }
    }
}
