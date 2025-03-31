package org.jparsec.containers;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.function.Function;

public sealed interface ParseResult<T> permits Ok, Err {

    default <U> ParseResult<U> map(Function<T, U> fn) {
        return switch (this) {
            case Err<T> e -> (Err<U>) e;
            case Ok<T>(T value, ParseContext ctx) -> new Ok<>(fn.apply(value), ctx);
        };
    }

    default T ok() {
        if (this instanceof Ok(T value, _)) {
            return value;
        }
        throw new IllegalStateException("Cannot access result");
    }

    default String error() {
        if (this instanceof Err(String msg, _ )){
            return msg;
        }
        throw new IllegalStateException("Cannot access error on ok");
    }

    default String errorTrace() {
        if (this instanceof Err(String err, ParseContext ctx)) {
            var baos = new ByteArrayOutputStream();
            var out = new PrintWriter(baos);
            var nestCount = 0;

            for (var e : ctx.traceErrors) {
                if (nestCount > 0) {
                    out.print(" ".repeat(nestCount));
                    out.print("+- ");
                }
                out.println(e);
                nestCount++;
            }

            out.flush();

            return baos.toString();

        } else {
            throw new IllegalStateException("not supported");
        }
    }

    default String verboseErrors() {
        var baos = new ByteArrayOutputStream();
        var out = new PrintWriter(baos);

        switch (this) {
            case Ok(_, ParseContext ctx) -> {
                ctx.allErrors.forEach(out::println);
            }
            case Err(_, ParseContext ctx) -> {
                ctx.allErrors.forEach(out::println);
            }
        }
        out.flush();

        return baos.toString();
    }

    default String errorPrettyPrint() {

        if (this instanceof Err(String error, ParseContext ctx)) {
            List<String> lines = ctx.content.lines().toList();

            var baos = new ByteArrayOutputStream();
            var out = new PrintWriter(baos);

            if (ctx.line - 1 >= 0) {
                out.print(lines.get(ctx.line - 1) + "\n");
            }

            var indicator = lines.get(ctx.line);
            var prefix = ctx.column == 0 ? "" : indicator.substring(0, ctx.column);
            var suffix = ctx.column == ctx.content.length() - 2 ? "" : indicator.substring(ctx.column + 1);
            indicator = prefix + ">>" + indicator.substring(ctx.column, ctx.column + 1)
                    + suffix + "\n";

            out.print(indicator);

            if (ctx.line + 1 < lines.size()) {
                out.print(lines.get(ctx.line + 1) + "\n");
            }
            out.flush();

            var textCtx = baos.toString();

            return String.format("%s--------\nLine: %d, Column: %d\n%s",
                    textCtx, ctx.line, ctx.column, error);
        } else {
            throw new IllegalStateException("Cannot form error message for Ok");
        }
    }
}
