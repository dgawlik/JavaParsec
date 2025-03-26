package org.jparsec.containers;

public record Ok<T>(T value, ParseContext ctx) implements ParseResult<T> {}
