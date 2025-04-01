//JAVA 24
//PREVIEW
//DEPS org.jparsec:JavaParsec:1.0.6

import org.jparsec.Ops;
import org.jparsec.Rule;

import java.util.ArrayList;
import java.util.Map;
import org.jparsec.combinator.Recursive;
import org.jparsec.combinator.Whitespace;
import org.jparsec.containers.*;
import org.jparsec.containers.Either.Left;
import org.jparsec.containers.Either.Right;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.System.out;
import static java.util.stream.Collectors.toMap;
import static org.jparsec.Api.*;

sealed interface Node {
    record Scalar(String value) implements Node {
    }

    record FlowKeyValues(Map<String, Node> value) implements Node {
    }

    record FlowList(List<Node> value) implements Node {
    }

    record BlockKeyValues(Map<String, Node> value) implements Node {
    }

    record BlockList(List<Node> value) implements Node {
    }
}

public void main() {

    var scalar = some(noneOf('\n', '}', ']', ',')).s();
    scalar.assertParses("\"hello\"  world ;");

    var doubleQuoted = seq(
            c('"'),
           many(
                   any(
                           seq(anyOf('\\'), anyOf('"')).s(),
                            noneOf('\n', '"').s()
                   )
           ).s(),
            anyOf('"')
    ).map(Ops::takeMiddle);

    doubleQuoted.assertParses("\"abc'\\n\\t xxx\"");
    doubleQuoted.assertFails("\"unclosed");
    doubleQuoted.assertFails("\" with newline \n \"");

    var singleQuoted = seq(
            c('\''),
            many(
                    any(
                            seq(anyOf('\\'), anyOf('\'')).s(),
                            noneOf('\n', '\'').s()
                    )
            ).s(),
            anyOf('\'')
    ).map(Ops::takeMiddle);

    singleQuoted.assertParses("'abc\"\\n\\t xxx\"'");
    singleQuoted.assertFails("'unclosed");
    singleQuoted.assertFails("' with newline \n '");

    Rule<String> blockScalar = seq(
            c('|').or(c('>'))
                    .map(e -> e.left().isEmpty()),
            c('\n'),
            some(seq(
                    many(anyOf(' ')).s(),
                    some(noneOf('\n')).s(),
                    c('\n').s()
            )),
            c('\n')
    ).map(block -> {
        var isFold = block.one();
        var lines = block.three();

        if (isFold) {
            return String.join(" ", lines.stream()
                    .map(Tuple3::two).toList());
        } else {
            return String.join("", lines.stream()
                    .map(Ops::concat).toList());
        }
    });

    blockScalar.assertParses("""
            |
            line 1
            line 2
            
            """);

    blockScalar.assertEquals(input("""
            |
            line 1
               line 2
            
            """), """
            line 1
               line 2
            """);

    blockScalar.assertEquals(input("""
            >
            line 1
               line 2
            
            """), "line 1 line 2");

    var identifier = choice(
            some(noneOf(' ', '\n', '\t', ':', '[', ']', '{', '}')).map(Ops::toString),
            singleQuoted,
            doubleQuoted)
            .map(Ops::takeAny);


    Recursive<Node> flowNode = recursive();

    var ws = spaces(Whitespace.Config.defaults().withWhitespace(' ', '\t'));

    var keyValue = seq(
            lexeme(identifier, ws),
            lexeme(c(':'), ws),
            lexeme(flowNode, ws)
    ).map(t -> new Pair<>(t.one(), t.three()));

    var flowKeyValues = seq(
            lexeme(c('{').s(), ws),
            sepBy(lexeme(keyValue, ws), lexeme(c(','), ws)),
            lexeme(c('}'), ws)
    ).map(Ops::takeMiddle)
            .map(v -> v.stream()
                    .collect(toMap(Pair::first, Pair::second)));

    var flowList = seq(
            lexeme(c('[').s(), ws),
            sepBy(lexeme(flowNode, ws), lexeme(c(','), ws)),
            c(']')
    ).map(Ops::takeMiddle);

    var flowScalar = choice(blockScalar, singleQuoted, doubleQuoted, scalar)
            .map(Ops::takeAny);

    flowNode.set(choice(flowKeyValues, flowList, flowScalar).map(v -> switch (v) {
        case One(Map<String, Node> v1) -> new Node.FlowKeyValues(v1);
        case Two(List<Node> v2) -> new Node.FlowList(v2);
        case Three(String s) -> new Node.Scalar(s);
    }));

    flowNode.assertParses("{ x: 1 , y: 1}");
    flowNode.assertParses("{ x: 1, y: {a: 3, b:4}}");
    flowNode.assertParses("[1,2,3]");
    flowNode.assertParses("[1,[2,3],[4,5,[6,7]]]");
    flowNode.assertParses("{a : [1, {b: 2}]}");

    Recursive<Node> blockNode = recursive();


    var blockKeyValue = seq(
            lexeme(identifier, ws),
            lexeme(c(':'), ws),
            lexeme(indent(
                    seq(nl(), blockNode)
                            .map(Ops::takeSecond),
                    "  ")
                    .or(flowNode).map(Ops::takeAny), ws)
    ).map(t -> new Pair<>(t.one(), t.three()));

    var blockKeyValues = sepBy(blockKeyValue, nl())
            .map(lst -> lst.stream().collect(Collectors.toMap(Pair::first, Pair::second)));


    var indentedBlockKeyValues = blockKeyValue.seq(
                    indent(many(nl().dropLeft(blockKeyValue)), "  "))
            .map(v -> {
                var concat = new ArrayList<Pair<String, Node>>();
                concat.add(v.first());
                concat.addAll(v.second());
                return concat;
            })
            .map(lst -> (Node) new Node.BlockKeyValues(
                    lst.stream().collect(Collectors.toMap(Pair::first, Pair::second))));


    var blockList = sepBy(
            seq(c("- "), indentedBlockKeyValues.or(flowNode).map(Ops::takeAny))
                    .map(Ops::takeSecond),
            nl()
    );

    blockList.assertParses("""
            - one
            - a: 1
              b: 2
              c: 3
            """);

    blockNode.set(
            choice(blockKeyValues, blockList).map(v -> switch (v) {
                case Right(List<Node> l) -> new Node.BlockList(l);
                case Left(Map<String, Node> m) -> new Node.BlockKeyValues(m);
            })
    );


    blockNode.assertParses(
            """
                    - one
                    - two
                    - {x:1, y:2}"""
    );


    blockNode.assertParses(
            """
                    a: 1
                    b: 2
                    c: 3"""
    );


    blockNode.assertParses(
            """
                    a: 1
                    b: 2
                    c:
                      - one
                      - two"""
    );


    var ws2 = spaces(Whitespace.Config.defaults().withSinglelineComment("#"));

    var yaml = ws2.dropLeft(many(lexeme(blockNode, ws2)));

    var test =
            """
                    
                    # This is a comment
                    
                    
                    - one:
                      - x : 1
                        z : {a:1, b:2} # inline comment
                        y : [1,2,[3,4]]
                      - [two, three] #comment
                      - four:
                            - {a: 2, b:1, c: [1,2]}
                            - some
                      - >
                    this is
                    mutiline folded
                    text
                    
                      - "six's'"
                      - 'seven"s"'
                    """;

    var result = yaml.parse(input(test));
    out.println(result);
}