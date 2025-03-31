package org.jparsec.combinator;

import org.jparsec.Rule;
import org.jparsec.containers.*;

import java.util.Arrays;
import java.util.List;

public class Whitespace extends Rule<Empty> {

    public static class Config {
        private List<Character> ws = List.of(' ', '\t', '\n');
        private String singlelineComment = "//";
        private Pair<String, String> multilineComment = new Pair<>("/*", "*/");

        private Config() {
        }

        public static Config defaults() {
            return new Config();
        }

        public Config withWhitespace(Character... chars) {
            this.ws = Arrays.asList(chars);
            return this;
        }

        public Config withSinglelineComment(String singlelineComment) {
            this.singlelineComment = singlelineComment;
            return this;
        }

        public Config withMultilineComment(String start, String end) {
            this.multilineComment = new Pair<>(start, end);
            return this;
        }
    }

    public static Whitespace spaces(Config config) {
        return new Whitespace(config);
    }

    private Whitespace(Config config) {
        super("Could not match whitespace");
        this.config = config;
    }

    @Override
    public String toString() {
        return "<spaces>";
    }

    private final Config config;

    @Override
    public ParseResult<Empty> parse(ParseContext ctx) {
        boolean moved1, moved2, moved3, moved = false;
        var newCtx = ctx.copy();
        do {
            moved1 = takeWhileWhitespace(newCtx);
            moved2 = takeWhileSinglelineComment(newCtx);
            moved3 = takeWhileMultilineComment(newCtx);

            if (moved1 || moved2 || moved3) {
                moved = true;
            }
        } while (moved1 || moved2 || moved3);

        if (moved) {
            return new Ok<>(new Empty(), newCtx);
        } else {
            ctx.appendTrace(errorMessage);
            ctx.addVerboseError(errorMessage);
            return new Err<>(errorMessage, newCtx);
        }
    }

    private boolean takeWhileWhitespace(ParseContext ctx) {
        boolean moved = false;
        while (ctx.index < ctx.content.length() &&
                config.ws.contains(ctx.content.charAt(ctx.index))) {
            moved = true;
            if (ctx.content.charAt(ctx.index) == '\n') {
                ctx.line++;
                ctx.column = -1;
            }
            ctx.index++;
            ctx.column++;
        }

        return moved;
    }

    private boolean takeWhileSinglelineComment(ParseContext ctx) {
        boolean moved = false;

        var comment = config.singlelineComment;

        if (ctx.index + comment.length() < ctx.content.length() &&
                ctx.content.substring(ctx.index, ctx.index + comment.length())
                        .startsWith(comment)) {
            moved = true;
            ctx.index += comment.length();

            while (ctx.index < ctx.content.length() &&
                    ctx.content.charAt(ctx.index) != '\n') {
                ctx.column++;
                ctx.index++;
            }

            if (ctx.index < ctx.content.length()) {
                ctx.line++;
                ctx.column = 0;
                ctx.index++;
            }
        }

        return moved;
    }

    private boolean takeWhileMultilineComment(ParseContext ctx) {
        var commentStart = config.multilineComment.first();
        var commentEnd = config.multilineComment.second();

        if (ctx.index + commentStart.length() < ctx.content.length() &&
                ctx.content.substring(ctx.index, ctx.index + commentStart.length())
                        .equals(commentStart)) {
            ctx.index += commentStart.length();

            while (ctx.index + commentEnd.length() < ctx.content.length() &&
                    !ctx.content.substring(ctx.index, ctx.index + commentEnd.length())
                            .equals(commentEnd)) {
                if (ctx.index >= 1 && ctx.content.charAt(ctx.index - 1) == '\n') {
                    ctx.line++;
                    ctx.column = 0;
                }
                ctx.index++;
                ctx.column++;
            }

            if (ctx.content.substring(ctx.index, ctx.index + commentEnd.length())
                    .equals(commentEnd)) {
                ctx.index += commentEnd.length();
                return true;
            } else {
                return false;
            }
        }

        return false;
    }
}
