package org.jparsec.test;

import org.jparsec.containers.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.jparsec.Api.*;
import static org.jparsec.combinator.Many.many;
import static org.jparsec.combinator.Sep.sepBy;
import static org.jparsec.combinator.Seq.seq;
import static org.junit.jupiter.api.Assertions.fail;

public class CsvTest {

    @Test
    public void test_success() {
        var escapedString = seq(
                anyOf('"'),
                many(noneOf('"')).map(this::join),
                anyOf('"')).map(this::dropQuotes);

        var sep = anyOf(',');
        var nl = anyOf('\n');

        var normalString = many(noneOf(',', '\n')).map(this::join);

        var line = sepBy(escapedString.or(normalString).map(this::takeAny), sep);

        var csv = sepBy(line, nl).mapOrError(this::checkSameNumberOfColumns);

        var text = """
                firstName,lastName,age
                Dominik,"#C,Gawlik",34/""";

        var result = csv.parse(ParseContext.of(text));
        if (result instanceof Ok(_, _)){
            Assertions.assertTrue(true);
        } else {
            fail();
        }
    }

    public String dropQuotes(Tuple3<Character, String, Character> val) {
        return val.two();
    }

    public String join(List<Character> lc) {
        return lc.stream().map(String::valueOf).collect(Collectors.joining());
    }

    public String takeAny(Either<String, String> e) {
        return switch (e) {
            case Either.Left(String val) -> val;
            case Either.Right(String val) -> val;
        };
    }

    private ParseResult<List<List<String>>> checkSameNumberOfColumns(ParseResult<List<List<String>>> pr) {
        switch (pr) {
            case Err e -> {
                return e;
            }
            case Ok(List<List<String>> listOfLists, ParseContext pc) -> {
                if (listOfLists.isEmpty()) {
                    return new Ok(List.of(), pc);
                }

                var firstSize = listOfLists.get(0).size();
                if (listOfLists.stream().anyMatch(l -> l.size() != firstSize)) {
                    return new Err("Every row has to have same number of columns", pc);
                } else {
                    return new Ok(listOfLists, pc);
                }
            }
        }
    }
}
