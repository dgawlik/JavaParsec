package org.jparsec.combinator;

import org.jparsec.Matcher;
import org.jparsec.containers.Context;
import org.jparsec.containers.Err;
import org.jparsec.containers.MatchResult;
import org.jparsec.containers.Ok;

public class SinglelineComment extends Matcher<String> {

    private final String activator;

    public SinglelineComment(String activator) {
        super("expected single line commment starting with " + activator);
        this.activator = activator;
    }

    @Override
    public MatchResult<String> parse(Context ctx) {
        var newCtx = ctx.copy();

        if (newCtx.index + activator.length() < newCtx.content.length() &&
                newCtx.content.substring(newCtx.index, newCtx.index + activator.length())
                        .startsWith(activator)) {
            newCtx.index += activator.length();

            while (newCtx.index < newCtx.content.length() &&
                    newCtx.content.charAt(newCtx.index) != '\n') {
                newCtx.column++;
                newCtx.index++;
            }

            if (newCtx.index < newCtx.content.length()) {
                newCtx.line++;
                newCtx.column = 0;
                newCtx.index++;
            }
        }

        if (newCtx.index == ctx.index) {
            return new Err<>("expected single line comment", ctx);
        } else {
            return new Ok<>(ctx.content.substring(ctx.index, newCtx.index), newCtx);
        }
    }
}
