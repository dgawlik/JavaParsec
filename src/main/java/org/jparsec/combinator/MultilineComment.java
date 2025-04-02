package org.jparsec.combinator;

import org.jparsec.Matcher;
import org.jparsec.containers.Context;
import org.jparsec.containers.Err;
import org.jparsec.containers.MatchResult;
import org.jparsec.containers.Ok;

public class MultilineComment extends Matcher<String> {

    private final String commentStart;
    private final String commentEnd;

    public MultilineComment(String commentStart, String commentEnd) {
        super("expected multiline comment starting with " + commentStart
                + " ending with " + commentEnd);
        this.commentEnd = commentEnd;
        this.commentStart = commentStart;
    }

    @Override
    public MatchResult<String> parse(Context ctx) {

        var newCtx = ctx.copy();

        if (newCtx.index + commentStart.length() < newCtx.content.length() &&
                newCtx.content.substring(newCtx.index, newCtx.index + commentStart.length())
                        .equals(commentStart)) {
            newCtx.index += commentStart.length();

            while (newCtx.index + commentEnd.length() < newCtx.content.length() &&
                    !newCtx.content.substring(newCtx.index, newCtx.index + commentEnd.length())
                            .equals(commentEnd)) {
                if (newCtx.content.charAt(newCtx.index) == '\n') {
                    newCtx.line++;
                    newCtx.column = -1;
                }
                newCtx.index++;
                newCtx.column++;
            }

            if (newCtx.content.substring(newCtx.index, newCtx.index + commentEnd.length())
                    .equals(commentEnd)) {
                newCtx.index += commentEnd.length();
                return new Ok<>(newCtx.content.substring(ctx.index, newCtx.index), newCtx);
            } else {
                return new Err<>(errorMessage, ctx);
            }
        } else {
            return new Err<>(errorMessage, ctx);
        }
    }
}
