import org.jparsec.Ops;
import org.jparsec.Rule;
import org.jparsec.combinator.Recursive;
import org.jparsec.containers.Ok;
import org.jparsec.containers.ParseContext;

import java.util.List;

import static org.jparsec.Api.*;
import static org.jparsec.combinator.Seq.seq;

interface ListItem {
    record Single(String value) implements ListItem {}
    record Group(String heading, List<ListItem> children) implements ListItem{}
}


public void main() {
    Rule<String> singleElem = seq(
            string("* "),
            some(noneOf('\n'))
    )
    .map(Ops::takeSecond)
    .map(Ops::toString);

    Recursive<List<ListItem>> list = recursive();

    Rule<ListItem> container = seq(
            singleElem, indent(
                            seq(nl(), list).map(Ops::takeSecond),
                    "  ")
             )
            .map(t -> new ListItem.Group(t.first(), t.second()));

    Rule<ListItem> elemOrContainer = container
            .or(singleElem.map(e -> (ListItem) new ListItem.Single(e)))
            .map(Ops::takeAny);

    list.set(
            sepBy(elemOrContainer, nl())
    );

    var result = list.parse(ParseContext.of("""
* books
  * classic
    * Adventures of Don Kichote
    * Sun sets again"""));

    if (result instanceof Ok o) {
        System.out.println(o.value());
    } else {
        System.out.println(result.errorPrettyPrint());
    }
}