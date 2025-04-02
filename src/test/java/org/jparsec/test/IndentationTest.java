package org.jparsec.test;

import org.jparsec.Api;
import org.jparsec.Ops;
import org.jparsec.combinator.Recursive;
import org.jparsec.containers.*;
import org.jparsec.containers.choice.Either;
import org.jparsec.containers.seq.Pair;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.jparsec.Api.*;
import static org.jparsec.combinator.Seq.seq;

public class IndentationTest {

    @Test
    public void test_yaml_lists() {
        var elem = seq(Api.c("- "), some(Api.noneOf('\n', ':')))
                .map(Ops::takeSecond).map(Ops::toString);
        Recursive<List<Either<Pair, String>>> list = recursive();
        var container = seq(elem, Api.anyOf(':'), indent(seq(nl(), list)
                .map(Ops::takeSecond), "  "))
                .map(t -> new Pair(t.one(), t.three()));
        var elemOrContainer = container.or(elem);
        list.set(sepBy(elemOrContainer, nl()));

        var result = list.parse(Context.of("""
- books:
  - classic:
    - Adventures of Don Kichote
    - Sun sets again"""));

        if (result instanceof Ok o) {
            System.out.println(o.value());
        } else {
            System.out.println(result.errorPrettyPrint());
        }
    }
}
