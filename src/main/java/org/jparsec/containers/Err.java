package org.jparsec.containers;

public record Err<T>(String error, ParseContext ctx) implements ParseResult<T> {
}
