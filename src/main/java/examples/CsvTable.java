import org.jparsec.Ops;
import org.jparsec.containers.Ok;
import org.jparsec.containers.Tuple4;

import java.util.List;

import static java.lang.System.out;
import static org.jparsec.Api.*;

record Employee(String firstName, String lastName, int age) {
}

record Table(List<Employee> people) {
}

public void main() {

    var escapedString = seq(
            c('"'),
            many(any(join(c('\\'), c('"'),
                    noneOf('"').s()))),
            c('"').s()
    ).map(Ops::takeMiddle).map(Ops::concat);

    var normalString = many(noneOf(',', '\n'))
            .map(Ops::toString);

    var string = any(escapedString, normalString);

    var header = seq(
            c("firstName"),
            c(','),
            c("lastName"),
            c(','),
            c("age")
    );

    var row = seq(
            string,
            c(','),
            string,
            c(','),
            string.map(Integer::valueOf)
    ).setInternalDescription("row");

    var rows = sepBy(row, c('\n'))
            .map(l ->
                    l.stream().map(t -> new Employee(t.one(), t.three(), t.five()))
                            .toList()
            );

    var table = seq(
            header,
            c('\n'),
            rows,
            opt(c('\n'))
    ).map(Tuple4::three).map(Table::new);

    var result = table.parse(input("""
            firstName,lastName,age
            Dominik,Gawlik,34
            Guido,"van Rossum",50
            """));
    if (result instanceof Ok(Table r, _)) {
        out.println(r);
    } else {
        out.println(result.errorPrettyPrint());
    }
}