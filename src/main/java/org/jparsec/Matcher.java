package org.jparsec;


import org.jparsec.combinator.*;
import org.jparsec.containers.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.jparsec.Api.eos;
import static org.jparsec.Api.input;


public abstract class Matcher<T> {

    public abstract MatchResult<T> parse(Context ctx);

    public MatchResult<T> parse(String text) {
        return parse(Api.input(text));
    }

    public T parseThrow(String text) {
        return switch (parse(Api.input(text))) {
            case Ok(T val, Context ctx) -> val;
            case Err e -> throw new ParseException(e.error());
        };
    }

    public Optional<T> parseMaybe(Context ctx) {
        return switch (parse(ctx)) {
            case Ok(T val, Context ctx2) -> Optional.of(val);
            case Err e -> Optional.empty();
        };
    }

    public Optional<T> parseMaybe(String text) {
        return switch (parse(input(text))) {
            case Ok(T val, Context ctx) -> Optional.of(val);
            case Err e -> Optional.empty();
        };
    }

    public void assertParses(String text) {
        if (this.dropRight(eos()).parse(input(text)) instanceof Err(String msg, Context ctx2)) {
            throw new ParseException(msg);
        }
    }

    public void assertFails(String text) {
        if (this.dropRight(eos()).parse(input(text)) instanceof Ok) {
            throw new ParseException("should fail");
        }
    }

    public void assertEquals(Context input, T value) {
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

    public Matcher(String errorMessage) {
        this.errorMessage = errorMessage;
        this.customError = Optional.empty();
        this.internalDescription = Optional.empty();
    }

    public Matcher<T> setErrorMessage(String message) {
        this.customError = Optional.of(message);
        return this;
    }

    public Matcher<T> setInternalDescription(String message) {
        this.internalDescription = Optional.of(message);
        return this;
    }

    public <U> Matcher<U> map(Function<T, U> fn) {
        return new Map<>(this, fn);
    }

    public Matcher<T> failIf(Predicate<T> fn, String errorMessage) {
        return new FailIf<>(this, fn, errorMessage);
    }

    public <U> Matcher<U> mapOrError(Function<MatchResult<T>, MatchResult<U>> fn) {
        return new MapOrFail<>(this, fn);
    }

    public Matcher<List<T>> takeWhile(Predicate<T> fn) {
        return new TakeWhile<>(this, fn);
    }

    public <U> Matcher<Either<T, U>> or(Matcher<U> other) {
        return new Or<>(this, other);
    }

    public Matcher<T> any(Matcher<T> other) {
        return this.or(other).map(e -> e.left().isPresent() ? e.left().get() : e.right().get());
    }

    public <U> Matcher<Pair<T, U>> seq(Matcher<U> other) {
        return new Seq2<>(this, other);
    }

    public <U> Matcher<U> dropLeft(Matcher<U> other) {
        return new DropLeft<>(this, other);
    }

    public <U> Matcher<T> dropRight(Matcher<U> other) {
        return new DropRight<>(this, other);
    }
}
