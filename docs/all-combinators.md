---
description: All parsers available
---

# All combinators

For convenient access add this to your source

```java
import static org.jparsec.Api.*
```

#### any()

Matches any single character.

```java
var r1 = any().parse("ab");
if (r1 instanceof Ok(Character c, ParseContext ctx)) {
    // c == 'a'
    // ctx.index == 1
}

var r2 = any().parse("");
if (r2 instanceof Err(String msg, _)) {
    // msg == "unexpected end of stream"
}
```



#### anyOf(Character... matchers)

Matches single occurrence of any of specified characters.

```java
var r = anyOf('a', 'b').parseThrow("a");
// r == 'a'
var r2 = anyOf('a', 'b').parseThrow("b");
// r2 == 'b'
var r3 = anyOf('a', 'b').parseThrow("c");
// throws ParseException: a,b could not be matched
```



#### whitespace()

Matches single whitespace character.

```java
var r = whitespace().parseMaybe(" ");
var r2 = whitespace().parseMaybe("\t");
var r3 = whitespace().parseMaybe("\n");

// r1.isPresent() == true
// r2.isPresent() == true
// r3.isPresent() == true
```



#### digit()

Matches single digit character including zero.

```java
for (int i=0;i<10;i++){
    var text = String.valueOf(i);
    var parser = digit()
            .map(Ops::toString)
            .map(Integer::valueOf);
    var num = parser.parseThrow(text);
    // num == i
}

digit().parseThrow("a"); // ParseException
```



#### nonZeroDigit()

Matches single digit excluding zero.

<pre class="language-java"><code class="lang-java"><strong>var r1 = nonZeroDigit().parse("1");
</strong>if (r1 instanceof Ok(Character c, _)){
    // r1 == '1'
}

var r2 = nonZeroDigit().parse("0");
if (r2 instanceof Err(String msg, _)) {
    // msg == "1,2,3,4,5,6,7,8,9 could not be matched"
}
</code></pre>



#### alpha()

Matches single alphabetic character.

```java
var r = alpha().parseMaybe("A");
var r2 = alpha().parseMaybe("z");
var e = alpha().parseMaybe("0");

// r.isPresent() == true
// r2.isPresent() == true
// e.isPresent() == false
```



#### alphaNum()

Matches single alphabetic character or digit.

```java
var r1 = alphaNum().parseThrow("A");
var r2 = alphaNum().parseThrow("0");
var e = alphaNum().parseThrow("@"); // ParseException
```



#### lower()

Matches single lowercase alphabetic character.

```java
lower().parseMaybe("a").isPresent() == true
lower().parseMaybe("A").isEmpty() == true
```



#### upper()

Matches single uppercase alphabetic character.

```java
upper().parseMaybe("A").isPresent() == true
upper().parseMaybe("a").isEmpty() == true
```



#### satisfy(Predicate\<Character> fn)

Matches supplied predicate on single character.

```java
Predicate<Character> evenDigit =
        (Character c) -> Character.digit(c, 10) % 2 == 0;

var r = satisfy(evenDigit).parseThrow("2");
// r == '2'
var e = satisfy(evenDigit).parseThrow("1"); // ParseException
```



#### range(Character start, Character end)

Matches single character if is between start and end inclusive.

```java
for (char i = 'a'; i <= 'f'; i++){
    var c = range('a', 'f').parseThrow(""+i);
    // c == i
}

var e = range('a', 'f').parse("g");
if (e instanceof Err(String msg, _)) {
    // msg == "a,b,c,d,e,f could not be matched"
}
```



#### noneOf(Character... inverseM)

Matches if single character if none of the specified.

```java
var e = noneOf('a', 'b').parse("a");
if (e instanceof Err(String msg, _)){
    // msg == "none of a,b should be matched"
}
```



#### choice(Rule\<T> r1, Rule\<U> r2)

#### choice(Rule\<T> r1, Rule\<U> r2, Rule\<W> r3)

#### choice(Rule\<T> r1, Rule\<U> r2, Rule\<W> r3, Rule\<Z> r4)

#### choice(Rule\<T> r1, Rule\<U> r2, Rule\<W> r3, Rule\<Z> r4, Rule\<X> r5)

#### choice(Rule\<T> r1, Rule\<U> r2, Rule\<W> r3, Rule\<Z> r4, Rule\<X> r5, Rule\<Y> r6)

#### choice(Rule\<T> r1, Rule\<U> r2, Rule\<W> r3, Rule\<Z> r4, Rule\<X> r5, Rule\<Y> r6, Rule\<G> r7)

Matches if any of the inner rule matches in this order.

Returns Either\<T, U> or ChoiceN\<T, U, W, ..., G>

```java
 var parser = choice(
       digit().map(Ops::toString),
       anyOf('a', 'b', 'c').map(Ops::toString),
       string("Hello")
).map(Ops::takeAny);

var r1 = parser.parseThrow("a");
var r2 = parser.parseThrow("2");
var r3 = parser.parseThrow("Hello");

var e = parser.parseMaybe("x").isEmpty();
```



#### seq(Rule\<T> r1, Rule\<U> r2)

#### seq(Rule\<T> r1, Rule\<U> r2, Rule\<W> r3)

#### seq(Rule\<T> r1, Rule\<U> r2, Rule\<W> r3, Rule\<Z> r4)

#### seq(Rule\<T> r1, Rule\<U> r2, Rule\<W> r3, Rule\<Z> r4, Rule\<X> r5)

#### seq(Rule\<T> r1, Rule\<U> r2, Rule\<W> r3, Rule\<Z> r4, Rule\<X> r5, Rule\<Y> r6

#### seq(Rule\<T> r1, Rule\<U> r2, Rule\<W> r3, Rule\<Z> r4, Rule\<X> r5, Rule\<Y> r6, Rule\<G> r7)

Matches if all of the inner rules match in this order.

Returns Pair\<T, U> or TupleN\<T, U, W, ..., G>

```java
 var identifier = seq(
       alpha().or(
               anyOf('$', '_')
       ).map(Ops::takeAny).map(Ops::toString),
       many(
               alphaNum().or(
                       anyOf('$', '_')
               ).map(Ops::takeAny)
       ).map(Ops::toString)
).map(Ops::concat);

var r1 = identifier.parseThrow("$id_1");
var r2 = identifier.parseThrow("_id223$");
var e = identifier.parseThrow("0id3"); // ParseException
```



#### many(Rule\<U> inner)

Matches zero or more occurrences of inner parser. Always succeeds.

Returns Rule\<List\<U>>

```java
var parser = many(anyOf('a', 'b')).map(Ops::toString);

var r = parser.parse("aabbababcccc");
if (r instanceof Ok(String r2, ParseContext ctx)){
   assert r2.equals("aabbabab");
   assert ctx.index == ctx.content.indexOf("c");
}
```



#### some(Rule\<U> inner)

Matches one or more occurrences of inner parser, fails on zero occurrences.

Returns Rule\<List\<U>>

```java
var parser = some(anyOf('a', 'b')).map(Ops::toString);

var e = parser.parse("ccccc");
if (e instanceof Err(String msg, _)) {
    // msg == "a,b could not be matched"
}
```



#### times(Rule\<U> inner, int number)

#### times(Rule\<U> inner, int fromNumber, int toNumber)

Matches the number of occurrences of inner rule

Returns Rule\<List\<U>>

```java
var year = times(digit(), 4)
       .map(Ops::toString).map(Integer::valueOf);

var month = times(digit(), 2)
        .map(Ops::toString).map(Integer::valueOf);

var day = times(digit(), 2)
        .map(Ops::toString).map(Integer::valueOf);

var date = seq(
        year,
        anyOf('/'),
        month,
        anyOf('/'),
        day
).map(t ->
   LocalDate.of(t.one(), t.three(), t.five())
);

var r1 = date.parseMaybe("2025/03/28");
var e = date.parseMaybe("20255/333/1");

assert r1.isPresent();
assert e.isEmpty();
```



#### opt(Rule\<U> inner)

Optionally matches inner rule. Always succeeds.

Returns Rule\<Optional\<U>>

```java
var optA = opt(anyOf('a'));
assert optA.parseMaybe("").get().isEmpty();
assert optA.parseMaybe("a").get().isPresent();
```



#### string(String pattern)

Matches exactly the pattern.

Returns Rule\<String>

```java
var world = string("world");
assert world.parseMaybe("world").isPresent();
assert world.parseMaybe("worl").isEmpty();
```



#### stringIgnoreCase(String pattern)

Matches the pattern case insensitive.

Returns Rule\<String>

```java
var helloCS = stringCaseInsensitive("hello");
assert helloCS.parseMaybe("HeLlO").isPresent();
```



#### spaces(Whitespace.Config config)

Takes spaces to exhaustion according to passed configuration. Always succeeds.

Returns Rule\<Empty>

```java
var ws = spaces(Whitespace.Config.defaults()
        .withWhitespace(' ', '\n')
        .withSinglelineComment("//")
        .withMultilineComment("/*", "*/"));

var line = choice(string("//"), string("\n"), any())
    .takeWhile(p -> {
    return p.three().isPresent();
        })
                .map(lst -> lst.stream()
                        .map(e -> e.three().get()).toList())
                        .map(Ops::toString)
        .failIf(String::isEmpty, "stop condition");

var lexeme = lexeme(line, ws);


var text = ws.dropLeft(some(lexeme));

var r1 = text.parseThrow("""
        /*
          multiline
          comment
        */
        this is a line 1 // comment
        //comment2
        this is a line 2
        """);

assert r1.size() == 2;
assert r1.get(0).equals("this is a line 1");
assert r1.get(1).equals("this is a line 2");
```



#### lexeme(Rule\<T> inner, Whitespace ws)

Acts as a composite of inner rule and trailing whitespace. While inner rule must succeed ws is discarded.

Returns Rule\<T>

```java
var trim = lexeme(string("hello"), spaces(Whitespace.Config.defaults()))
trim.parseMaybe(input("hello  ")).get().equals("hello")
```



#### sepBy(Rule\<T> inner, Rule\<U> sep)

Takes zero or more of _inner_ rule separated by _sep_ rule.

Returns Rule\<List\<T>>

```java
var commaSep = sepBy(anyOf('a', 'b', 'c'), anyOf(',')).map(Ops::toString)
commaSep.parse(input("a,b,c")).get().equals("abc")
```



#### not(Rule\<T> inner)

Fails if inner rule matches.&#x20;

Returns Rule\<Empty>

```java
var notA = not(anyOf('a'))
notA.parseMaybe("a").isPresent() == false
```



#### recursive()

This is a helper to define cyclic dependencies between rules.

Returns Recursive\<T>

```java
sealed interface Expression {
    record Value(Integer val) implements Expression {}
    record Parens(Expression inner) implements Expression {}
}

Recursive<Expression> expr = recursive()
var number = some(digit()).map(Ops::toString).map(Integer::valueOf).map(Value::new)
expr.set(number.or(seq(anyOf('('), expr, anyOf(')')).map(Ops::takeMiddle).map(Parens::new))

expr.parsseMaybe("((123))").isPresent() == true
```



#### indent(Rule\<U> inner, String pattern)

Combined with _nl()_ combinator allows to parse indented patterns.

Returns Rule\<U>

```java
var listElems = many(seq(
    nl(), 
    string("- "), 
    some(noneOf('\n').).map(Ops::toString)
    ).map(Ops::takeLast))
Tuple3<String, Character, List<String>> group = seq(
    some(noneOf('\n', ':')).map(Ops::toString), 
    anyOf(':'), 
    indent(listElems, "  "))
    
group.parseMaybe(input("""
header:
  - one
  - two
""")).isPresent() == true
```



#### nl()

See [#indent-rule-less-than-u-greater-than-inner-string-pattern](all-combinators.md#indent-rule-less-than-u-greater-than-inner-string-pattern "mention")



#### eos()

In normal mode parser takes only part of text that is needed. Combining pattern with this rule asserts exhaustion of stream.

```java
var singleA = seq(anyOf('a'), eos())
singleA.parseMayve(input("aaa")).isPresent == false
```



