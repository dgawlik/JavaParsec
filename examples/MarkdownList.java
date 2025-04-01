//JAVA 24
//PREVIEW
//DEPS org.jparsec:JavaParsec:1.0.5

import org.jparsec.Ops;
import org.jparsec.Rule;
import org.jparsec.combinator.Recursive;
import org.jparsec.containers.Ok;
import org.jparsec.containers.ParseContext;

import java.util.List;

import static java.lang.System.out;
import static org.jparsec.Api.*;
import static org.jparsec.combinator.Seq.seq;

interface ListItem {
    record Single(String value) implements ListItem {}
    record Group(String heading, List<ListItem> children) implements ListItem{}
}


public void main() {
    Rule<String> singleElem = seq(
            c("* "),
            some(noneOf('\n')).s()
    )
    .map(Ops::takeSecond);

    Recursive<List<ListItem>> list = recursive();

    Rule<ListItem> container = seq(
            singleElem, indent(
                            seq(nl(), list).map(Ops::takeSecond),
                    "  ")
             )
            .map(t -> new ListItem.Group(t.first(), t.second()));

    Rule<ListItem> elemOrContainer = any(container,
            singleElem.map(e -> (ListItem) new ListItem.Single(e)));

    list.set(
            sepBy(elemOrContainer, nl())
    );

    var result = list.parse(ParseContext.of("""
* books
  * classic
    * Adventures of Don Kichote
    * Sun sets again"""));

    if (result instanceof Ok o) {
        out.println(o.value());
    } else {
        out.println(result.errorPrettyPrint());
    }
}