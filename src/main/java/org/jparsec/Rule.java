package org.jparsec;


import org.jparsec.combinator.*;
import org.jparsec.containers.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.jparsec.Api.eos;
import static org.jparsec.Api.input;


public abstract class Rule<T> {

    public abstract ParseResult<T> parse(ParseContext ctx);

    public ParseResult<T> parse(String text) {
        return parse(Api.input(text));
    }

    public T parseThrow(String text) {
        return switch (parse(Api.input(text))) {
            case Ok(T val, _) -> val;
            case Err e -> throw new ParseException(e.error());
        };
    }

    public Optional<T> parseMaybe(ParseContext ctx) {
        return switch (parse(ctx)) {
            case Ok(T val, _) -> Optional.of(val);
            case Err e -> Optional.empty();
        };
    }

    public Optional<T> parseMaybe(String text) {
        return switch (parse(input(text))) {
            case Ok(T val, _) -> Optional.of(val);
            case Err e -> Optional.empty();
        };
    }

    public void assertParses(String text) {
        if (this.dropRight(eos()).parse(input(text)) instanceof Err(String msg, _)) {
            throw new ParseException(msg);
        }
    }

    public void assertFails(String text) {
        if (this.dropRight(eos()).parse(input(text)) instanceof Ok) {
            throw new ParseException("should fail");
        }
    }

    public void assertEquals(ParseContext input, T value) {
        var result = this.parseMaybe(input);
        if (result.isEmpty()) {
            throw new ParseException("could not parse");
        } else if (!result.get().equals(value)) {
            throw new ParseException(String.format("%s expected, %s got", result.get(), value));
        }
    }


    public String errorMessage;
    public Optional<String> customError;
    public Optional<String> internalDescription;

    public Rule(String errorMessage) {
        this.errorMessage = errorMessage;
        this.customError = Optional.empty();
        this.internalDescription = Optional.empty();
    }

    public Rule<T> setErrorMessage(String message) {
        this.customError = Optional.of(message);
        return this;
    }

    public Rule<T> setInternalDescription(String message) {
        this.internalDescription = Optional.of(message);
        return this;
    }

    public <U> Rule<U> map(Function<T, U> fn) {
        return new Map<>(this, fn);
    }


    public Rule<T> failIf(Predicate<T> fn, String errorMessage) {
        return new FailIf<>(this, fn, errorMessage);
    }

    public <U> Rule<U> mapOrError(Function<ParseResult<T>, ParseResult<U>> fn) {
        return new MapOrFail<>(this, fn);
    }

    public Rule<List<T>> takeWhile(Predicate<T> fn) {
        return new TakeWhile<>(this, fn);
    }

    public <U> Rule<Either<T, U>> or(Rule<U> other) {
        return new Or<>(this, other);
    }

    public <U> Rule<Pair<T, U>> seq(Rule<U> other) {
        return new Seq2<>(this, other);
    }

    public <U> Rule<U> dropLeft(Rule<U> other) {
        return new DropLeft<>(this, other);
    }

    public <U> Rule<T> dropRight(Rule<U> other) {
        return new DropRight<>(this, other);
    }
}
