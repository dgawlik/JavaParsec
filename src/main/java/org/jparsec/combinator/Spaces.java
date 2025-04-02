package org.jparsec.combinator;

import org.jparsec.Matcher;
import org.jparsec.containers.Context;
import org.jparsec.containers.Err;
import org.jparsec.containers.MatchResult;
import org.jparsec.containers.Ok;

import java.util.Arrays;
import java.util.List;

public class Spaces extends Matcher<String> {

    private final List<Character> ws;

    public Spaces(Character... ws) {
        super("error matching whitespace");
        this.ws = Arrays.asList(ws);
    }


    @Override
    public MatchResult<String> parse(Context ctx) {
        Context newCtx = ctx.copy();
        while (newCtx.index < newCtx.content.length() &&
                isWhitespace(ctx.content.charAt(newCtx.index))) {
            if (newCtx.content.charAt(newCtx.index) == '\n') {
                newCtx.line++;
                newCtx.column = -1;
            }
            newCtx.index++;
            newCtx.column++;
        }

        if (newCtx.index == ctx.index) {
            return new Err<>("no whitespace found", ctx);
        } else {
            return new Ok<>(ctx.content.substring(ctx.index, newCtx.index), newCtx);
        }
    }

    private boolean isWhitespace(Character ch) {
        if (ws.isEmpty()) {
            return Character.isWhitespace(ch);
        } else {
            return ws.contains(ch);
        }
    }
}
