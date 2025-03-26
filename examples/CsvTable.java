import org.jparsec.Ops;
import org.jparsec.containers.Ok;
import org.jparsec.containers.Tuple4;

import java.util.List;

import static org.jparsec.Api.*;

record Employee(String firstName, String lastName, int age) {
}

record Table(List<Employee> people) {
}

public void main() {

    var escapedString = seq(
            anyOf('"'),
            many(seq(anyOf('\\'), anyOf('"')).map(Ops::seqToList)
                    .or(noneOf('"').map(Ops::singleton))
                    .map(Ops::takeAny))
                    .map(Ops::flatten)
                    .map(Ops::toString),
            anyOf('"')
    ).map(Ops::takeMiddle);

    var normalString = many(noneOf(',', '\n'))
            .map(Ops::toString);

    var string = escapedString.or(normalString).map(Ops::takeAny);

    var header = seq(
            string("firstName"),
            anyOf(','),
            string("lastName"),
            anyOf(','),
            string("age")
    );

    var row = seq(
            string,
            anyOf(','),
            string,
            anyOf(','),
            string.map(Integer::valueOf)
    ).setInternalDescription("row");

    var rows = sepBy(row, anyOf('\n'))
            .map(l ->
                    l.stream().map(t -> new Employee(t.one(), t.three(), t.five()))
                            .toList()
            );

    var table = seq(
            header,
            anyOf('\n'),
            rows,
            opt(anyOf('\n'))
    ).map(Tuple4::three).map(Table::new);

    var result = table.parse(input("""
            firstName,lastName,age
            Dominik,Gawlik,34
            Guido,"van Rossum",50
            """));
    if (result instanceof Ok(Table r, _)) {
        println(r);
    } else {
        println(result.errorPrettyPrint());
    }
}