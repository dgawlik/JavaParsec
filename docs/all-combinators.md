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
var result = anyChar().parse("a");
if (result instanceof Ok(Character c, Context ctx)){
    assert (char) c == 'a';
    assert (int) ctx.index == 1;
}

var err = anyChar().parse("");
if (err instanceof Err(String msg, Context ctx)) {
    assert "unexpected end of stream".equals(msg);
    assert (int) ctx.index == 0;
}    
```



#### anyOf(Character... matchers)

Matches single occurrence of any of specified characters.

```java
var r1 = anyOf('a', 'b').parse("a");
assert r1.ok() == 'a';

var r2 = anyOf('a', 'b').parse("b");
assert r2.ok() == 'b';

var r3 = anyOf('a', 'b').parse("c");
assert !r3.isOk()
```

#### whitespace()

Matches single whitespace character.

```java
var r1 = whitespace().parseMaybe(" ");
var r2 = whitespace().parseMaybe("\t");
var r3 = whitespace().parseMaybe("\n");

assert r1.isPresent() == true;
assert r2.isPresent() == true;
assert r3.isPresent() == true;
```



#### digit()

Matches single digit character including zero.

```java
for (int i=0;i<10;i++){
    var text = String.valueOf(i);
    var parser = digit().s().map(Integer::valueOf);
    var num = parser.parseThrow(text);
    assert (int) num == i;
}

digit().parseThrow("a"); // ParseException
```



#### nonZeroDigit()

Matches single digit excluding zero.

```java
var r1 = nonZeroDigit().parse("1");
if (r1 instanceof Ok(Character c, Context ctx)){
    assert (char) c == '1';
}

var r2 = nonZeroDigit().parse("0");
if (r2 instanceof Err(String msg, Context ctx)) {
    assert "expected non zero digit".equals(msg);
}
```



#### alpha()

Matches single alphabetic character.

```java
var r = letter().parseMaybe("A");
var r2 = letter().parseMaybe("z");
var e = letter().parseMaybe("0");

assert r.isPresent() == true;
assert r2.isPresent() == true;
assert e.isPresent() == false;
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
assert lower().parseMaybe("a").isPresent() == true;
assert lower().parseMaybe("A").isEmpty() == true;
```



#### upper()

Matches single uppercase alphabetic character.

```java
assert upper().parseMaybe("A").isPresent() == true;
assert upper().parseMaybe("a").isEmpty() == true;
```



#### satisfy(Predicate\<Character> fn)

Matches supplied predicate on single character.

```java
Predicate<Character> evenDigit =
        (Character c) -> Character.digit(c, 10) % 2 == 0;

var r = satisfy(evenDigit).parse("2");
assert (char) r.ok() == '2';
var e = satisfy(evenDigit).parse("1");
assert e.isOk() == false;
```



#### range(Character start, Character end)

Matches single character if is between start and end inclusive.

```java
for (char i = 'a'; i <= 'f'; i++){
    var c = range('a', 'f').parseThrow(""+i);
    assert (char) c == i;
}

var e = range('a', 'f').parse("g");
if (e instanceof Err(String msg, Context ctx)) {
    assert "expected 'a'..'f'".equals(msg);
}
```



#### noneOf(Character... inverseM)

Matches if single character if none of the specified.

```java
var e = noneOf('a', 'b').parse("a");
assert "not expecting 'a','b'".equals(e.error());
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
    digit().s(),
    anyOf('a', 'b', 'c').s(),
    c("Hello")
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
    any(letter(), anyOf('$', '_')),
    many(
        any(alphaNum(), anyOf('$', '_'))
    ).s()
).s();

var r1 = identifier.parseThrow("$id_1");
var r2 = identifier.parseThrow("_id223$");
var e = identifier.parseThrow("0id3"); // ParseException
```



#### many(Rule\<U> inner)

Matches zero or more occurrences of inner parser. Always succeeds.

Returns Rule\<List\<U>>

```java
var parser = many(anyOf('a', 'b')).s();

var r = parser.parse("aabbababcccc");
if (r instanceof Ok(String r2, Context ctx)){
   assert r2.equals("aabbabab");
   assert ctx.index == ctx.content.indexOf("c");
}
```



#### some(Rule\<U> inner)

Matches one or more occurrences of inner parser, fails on zero occurrences.

Returns Rule\<List\<U>>

```java
var parser = some(anyOf('a', 'b')).s();

var e = parser.parse("ccccc");
if (e instanceof Err(String msg, Context ctx)) {
    assert "expected 'a','b'".equals(msg);
}
```



#### times(Rule\<U> inner, int number)

#### times(Rule\<U> inner, int fromNumber, int toNumber)

Matches the number of occurrences of inner rule

Returns Rule\<List\<U>>

```java
var year = times(digit(), 4)
       .s().map(Integer::valueOf);

var month = times(digit(), 2)
        .s().map(Integer::valueOf);

var day = times(digit(), 2)
        .s().map(Integer::valueOf);

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



#### c(String pattern)

Captures exactly the pattern.

Returns Rule\<String>

```java
var world = c("world");
assert world.parseMaybe("world").isPresent();
assert world.parseMaybe("worl").isEmpty();
```



#### c(Character chr)

Captures exactly the character

```java
var world = c('a');
assert world.parseMaybe("a").isPresent();
assert world.parseMaybe("b").isEmpty();
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

var line = choice(c("//"), c("\n"), anyChar().s())
    .takeWhile(p -> p.three().isPresent())
    .map(lst -> lst.stream().map(e -> e.three().get()).collect(Collectors.joining()))
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
var trim = lexeme(c("hello"), spaces(Whitespace.Config.defaults()))
trim.parseMaybe(input("hello  ")).get().equals("hello")
```



#### sepBy(Rule\<T> inner, Rule\<U> sep)

Takes zero or more of _inner_ rule separated by _sep_ rule.

Returns Rule\<List\<T>>

```java
var commaSep = sepBy(anyOf('a', 'b', 'c'), c(',')).s();
assert "abc".equals(commaSep.parseThrow("a,b,c")
```



#### not(Rule\<T> inner)

Fails if inner rule matches.&#x20;

Returns Rule\<Empty>

```java
var notA = not(c('a'))
notA.parseMaybe("a").isPresent() == false
```



#### rule1.dropLeft(Rule\<U> rule2)

Asserts both rules are matched but discards the first one.

```java
var trim = many(c(' ')).dropLeft(c("hello"));
assert trim.parse("   hello").ok().equals("hello");
```



#### rule1.dropRight(Rule\<U> rule2)

Asserts both rules are matched but discards the last one.

```java
var trimComment = c("hello").dropRight(seq(many(c(' ')), c("//"), many(noneOf('\n'))));
var result = trimComment.parse("hello  // this is a comment\n");
if (result instanceof Ok(String r, Context ctx)){
        assert r.equals("hello");
        assert ctx.index == 27;
} else {
        throw new RuntimeException("Failed");
}
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
var number = some(digit()).s().map(Integer::valueOf).map(Value::new)
expr.set(number.or(
        seq(anyOf('('), expr, anyOf(')'))
                .map(Ops::takeMiddle)
                .map(Parens::new)
)

expr.parsseMaybe("((123))").isPresent() == true
```



#### indent(Rule\<U> inner, String pattern)

Combined with _nl()_ combinator allows to parse indented patterns.

Returns Rule\<U>

```java
var listElems = many(seq(
    nl(), 
    c("- "), 
    some(noneOf('\n')).s()
    ).map(Ops::takeLast))
Tuple3<String, Character, List<String>> group = seq(
    some(noneOf('\n', ':')).s(),
    c(':'), 
    indent(listElems, "  "))
    
group.parseMaybe(input("""
header:
  - one
  - two
""")).isPresent() == true
```

#### rule.s()

Concats seqs, lists of chars and lists of strings into single string.

```java
var concat = seq(c('a'), c("bb"), many(c('c'))).s();

assert concat.parse("abbcccc").ok().equals("abbcccc");
```



#### any(Rule\<T>... rules)

Takes any of the rules matching. Rules have to be uniform in type.

```java
var letterOrDigit = many(any(digit(), letter())).s();

assert letterOrDigit.parse("a1b2").isOk();
```



#### nl()

See [#indent-rule-less-than-u-greater-than-inner-string-pattern](all-combinators.md#indent-rule-less-than-u-greater-than-inner-string-pattern "mention")



#### eos()

In normal mode parser takes only part of text that is needed. Combining pattern with this rule asserts exhaustion of stream.

```java
var singleA = seq(c('a'), eos())
singleA.parseMayve("aaa").isPresent() == false
```



#### rule.map(Function\<T, U> fn)

Returns rule wrapped and transformed with given function.

```java
var i = some(digit()).s().map(Integer::valueOf);

assert (int) i.parse("1").ok() == 1;
```



#### rule.failIf(String errorMsg, Predicate\<T> fn)

Returns rule wrapped that tests give predicate and fails with error message;

```java
var even = some(digit()).s()
    .map(Integer::valueOf)
    .failIf(i -> i % 2 != 0, "not even");

assert even.parse("123").error().equals("not even");
```



#### rule.takeWhile(Predicate\<T> fn)

Consumes input until predicate is not met.

```java
var notB = anyChar().takeWhile(c -> c != 'b').map(Ops::toString);

assert notB.parse("adcb").ok().equals("adc");
```
