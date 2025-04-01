//JAVA 24
//PREVIEW
//DEPS org.jparsec:JavaParsec:1.0.6

import org.jparsec.Ops;
import org.jparsec.containers.Ok;
import org.jparsec.containers.ParseContext;
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
            many(any(seq(c('\\'), c('"')).s(),
                    noneOf('"').s())).s(),
            c('"')
    ).map(Ops::takeMiddle);

    var normalString = many(noneOf(',', '\n')).s();

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
    if (result instanceof Ok(Table r, ParseContext ctx)) {
        out.println(r);
    } else {
        out.println(result.errorPrettyPrint());
    }
}