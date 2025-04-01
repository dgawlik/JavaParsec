package org.jparsec.containers;

public record Ok<T>(T value, Context ctx) implements MatchResult<T> {}
