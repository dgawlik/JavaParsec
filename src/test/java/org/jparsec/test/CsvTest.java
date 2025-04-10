package org.jparsec.test;

import org.jparsec.containers.*;
import org.jparsec.containers.choice.Either;
import org.jparsec.containers.seq.Tuple3;
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
                c('"'),
                many(noneOf('"')).map(this::join),
                c('"')).map(this::dropQuotes);

        var sep = c(',');
        var nl = c('\n');

        var normalString = many(noneOf(',', '\n')).map(this::join);

        var line = sepBy(escapedString.or(normalString).map(this::takeAny), sep);

        var csv = sepBy(line, nl).mapOrError(this::checkSameNumberOfColumns);

        var text = """
                firstName,lastName,age
                Dominik,"#C,Gawlik",34/""";

        var result = csv.parse(Context.of(text));
        if (result instanceof Ok(List<List<String>> r, Context ctx)){
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

    private MatchResult<List<List<String>>> checkSameNumberOfColumns(MatchResult<List<List<String>>> pr) {
        switch (pr) {
            case Err e -> {
                return e;
            }
            case Ok(List<List<String>> listOfLists, Context pc) -> {
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
