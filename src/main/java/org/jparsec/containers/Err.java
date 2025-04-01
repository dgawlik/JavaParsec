package org.jparsec.containers;

public record Err<T>(String error, Context ctx) implements MatchResult<T> {
}
