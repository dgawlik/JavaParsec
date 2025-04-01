package org.jparsec.combinator;

import org.jparsec.Matcher;
import org.jparsec.Ops;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.jparsec.containers.Context;
import org.jparsec.containers.MatchResult;

public class Str extends Matcher<String> {

    private final String pattern;
    private final boolean caseInsensitive;

    public Str(String pattern, boolean caseInsensitive) {
        super("expected \"" + pattern + "\"");
        this.pattern = pattern;
        this.caseInsensitive = caseInsensitive;
    }

    public Concat join(Satisfy c) {
        return new Concat(this, c.map(Ops::toString));
    }

    public Concat join(Str s) {
        return new Concat(this, s);
    }

    @Override
    public String toString() {
        return "\"" + pattern + "\"";
    }

    @Override
    public MatchResult<String> parse(Context ctx) {
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

    private boolean matches(Context ctx) {
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
