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
any().parseMaybe(input("ab")).get() == 'a'
```



#### anyOf(Character... matchers)

Matches single occurrence of any of specified characters.

```java
anyOf('a', 'b').parseMaybe(input("a")).get() == 'a'
anyOf('a', 'b').parseMaybe(input("b")).get() == 'b'
anyOf('a', 'b').parseMaybe(input("c")).isEmpty() == true
```



#### whitespace()

Matches single whitespace character.

```java
whitespace().parseMaybe(input(" ")).isPresent()
whitespace().parseMaybe(input("\n")).isPresent()
```



#### digit()

Matches single digit character including zero.

```java
digit().parseMaybe(input("0")).isPresent() == true
```



#### nonZeroDigit()

Matches single digit excluding zero.

```java
nonZeroDigit().parseMaybe(input("1")).isPresent() == true
nonZeroDigit().parseMaybe(input("0")).isEmpty() == true
```



#### alpha()

Matches single alphabetic character.

```java
alpha().parseMaybe(input("a")).isPresent() == true
alpha().parseMaybe(input("0")).isEmpty() == true
```



#### alphaNum()

Matches single alphabetic character or digit.

```java
alphaNum().parseMaybe(input("a")).isPresent() == true
alphaNum().parseMaybe(input("0")).isPresent() == true
```



#### lower()

Matches single lowercase alphabetic character.

```java
lower().parseMaybe(input("a")).isPresent() == true
lower().parseMaybe(input("A")).isEmpty() == true
```



#### upper()

Matches single uppercase alphabetic character.

```java
upper().parseMaybe(input("A")).isPresent() == true
upper().parseMaybe(input("a")).isEmpty() == true
```



#### satisfy(Predicate\<Character> fn)

Matches supplied predicate on single character.

```java
satisfy(Character::isDigit).parseMaybe(input("0")).isPresent() == true
satisfy(Character::isDigit).parseMaybe(input("A")).isEmpty() == true
```



#### range(Character start, Character end)

Matches single character if is between start and end inclusive.

```java
range('a', 'f').parseMaybe(input("b")).isPresent() == true
range('a', 'f').parseMaybe(input("f")).isPresent() == true
range('a', 'f').parseMaybe(input("g")).isEmpty() == true
```



#### noneOf(Character... inverseM)

Matches if single character if none of the specified.

```java
noneOf('a', 'b').parseMaybe(input("b")).isEmpty() == true
noneOf('a', 'b').parseMaybe(input("c")).get() == 'c'
```



#### choice(Rule\<T> r1, Rule\<U> r2)

Matches if r1 or r2 matches in that order.

Returns Either\<T, U>

```java
choice(anyOf('a'), string("hello")).parseMaybe(input("a")).get().left().isPresent()
choice(anyOf('a'), string("hello")).parseMaybe(input("hello")).get().right().isPresent()
```



#### choice(Rule\<T> r1, Rule\<U> r2, Rule\<W> r3)

Matches if any of r1, r2, r3 matches in that order.

Returns Choice3\<T, U, W>

```java
var result = choice(anyOf('a'), anyOf('b'), anyOf('c')).parseMaybe(input('c'))
if (result instanceof Three(Character r)) {
    r == 'c'
}
```



#### choice(Rule\<T> r1, Rule\<U> r2, Rule\<W> r3, Rule\<Z> r4)

Matches if any of r1, r2, r3, r4 matches in that order.

Returns Choice4\<T, U, W, Z>

```java
var result = choice(anyOf('a'), anyOf('b'), 
    anyOf('c'), anyOf('d')).parseMaybe(input('d'))
if (result instanceof Four(Character r)) {
    r == 'd'
}
```



#### choice(Rule\<T> r1, Rule\<U> r2, Rule\<W> r3, Rule\<Z> r4, Rule\<X> r5)

Matches if any of r1, r2, r3, r4, r5 matches in that order.

Returns Choice5\<T, U, W, Z, X>

```java
var result = choice(anyOf('a'), anyOf('b'), 
    anyOf('c'), anyOf('d'), anyOf('e')).parseMaybe(input('e'))
if (result instanceof Five(Character r)) {
    r == 'e'
}
```



#### choice(Rule\<T> r1, Rule\<U> r2, Rule\<W> r3, Rule\<Z> r4, Rule\<X> r5, Rule\<Y> r6)

Matches if any of r1, r2, r3, r4, r5, r6 matches in that order.

Returns Choice6\<T, U, W, Z, X, Y>

```java
var result = choice(anyOf('a'), anyOf('b'), 
    anyOf('c'), anyOf('d'), anyOf('e'),
    anyOf('f')).parseMaybe(input('f'))
if (result instanceof Six(Character r)) {
    r == 'f'
}
```



#### choice(Rule\<T> r1, Rule\<U> r2, Rule\<W> r3, Rule\<Z> r4, Rule\<X> r5, Rule\<Y> r6, Rule\<G> r7)

Matches if any of r1, r2, r3, r4, r5, r6, r7 matches in that order.

Returns Choice7\<T, U, W, Z, X, Y, G>

```java
var result = choice(anyOf('a'), anyOf('b'), 
    anyOf('c'), anyOf('d'), anyOf('e'),
    anyOf('f', anyOf('g')).parseMaybe(input('f'))
if (result instanceof Seven(Character r)) {
    r == 'g'
}
```



#### seq(Rule\<T> r1, Rule\<U> r2)

Matches if r1, r2 match in sequence.

Returns Pair\<T, U>

```java
var result = seq(anyOf('a'), anyOf('b')).map(Ops::toString).parseMaybe(input("ab"))
"ab".equals(result.get())
```



#### seq(Rule\<T> r1, Rule\<U> r2, Rule\<W> r3)

Matches if r1, r2, r3 match in sequence.

Returns Tuple3\<T, U, W>

```java
var result = seq(anyOf('a'), anyOf('b'), anyOf('c'))
    .map(Ops::toString).parseMaybe(input("abc"))
"abc".equals(result.get())
```



#### seq(Rule\<T> r1, Rule\<U> r2, Rule\<W> r3, Rule\<Z> r4)

Matches if r1, r2, r3, r4 match in sequence.

Returns Tuple4\<T, U, W, Z>

```java
var result = seq(anyOf('a'), anyOf('b'), anyOf('c'), anyOf('d'))
    .map(Ops::toString).parseMaybe(input("abcd"))
"abcd".equals(result.get())
```



#### seq(Rule\<T> r1, Rule\<U> r2, Rule\<W> r3, Rule\<Z> r4, Rule\<X> r5)

Matches if r1, r2, r3, r4, r5 match in sequence.

Returns Tuple5\<T, U, W, Z, X>

```java
var result = seq(anyOf('a'), anyOf('b'), anyOf('c'), anyOf('d'), anyOf('e'))
    .map(Ops::toString).parseMaybe(input("abcde"))
"abcde".equals(result.get())
```



#### seq(Rule\<T> r1, Rule\<U> r2, Rule\<W> r3, Rule\<Z> r4, Rule\<X> r5, Rule\<Y> r6)

Matches if r1, r2, r3, r4, r5, r6 match in sequence.

Returns Tuple6\<T, U, W, Z, X, Y>

```java
var result = seq(anyOf('a'), anyOf('b'), anyOf('c'), 
    anyOf('d'), anyOf('e'), anyOf('f'))
    .map(Ops::toString).parseMaybe(input("abcdef"))
"abcdef".equals(result.get())
```



#### seq(Rule\<T> r1, Rule\<U> r2, Rule\<W> r3, Rule\<Z> r4, Rule\<X> r5, Rule\<Y> r6, Rule\<G> r7)

Matches if r1, r2, r3, r4, r5, r6, r7 match in sequence.

Returns Tuple7\<T, U, W, Z, X, Y, G>

```java
var result = seq(anyOf('a'), anyOf('b'), anyOf('c'), 
    anyOf('d'), anyOf('e'), anyOf('f'), anyOf('g'))
    .map(Ops::toString).parseMaybe(input("abcdefg"))
"abcdefg".equals(result.get())
```



#### many(Rule\<U> inner)

Matches zero or more occurrences of inner parser. Always succeeds.

Returns Rule\<List\<U>>

```java
var result = many(string("hello")).map(Ops::concat).parseMaybe(input("hellohello"));
"hellohello".equals(result.get())
```



#### some(Rule\<U> inner)

Matches one or more occurrences of inner parser, fails on zero occurrences.

Returns Rule\<List\<U>>

```java
var result = some(string("hello")).parseMaybe(input("")).isEmpty() == true
```



#### times(Rule\<U> inner, int number)

Matches exactly _number_ of occurrences of inner rule. Fails otherwise.

Returns Rule\<List\<U>>

```java
var year = times(digit(), 4).map(Ops::toString).map(Integer::valueOf)
year.parseMaybe(input("2025")).get() == 2025
```



#### times(Rule\<U> inner, int fromNumber, int toNumber)

Matches in between fromNumber and toNumber of occurrences of inner rule

Returns Rule\<List\<U>>

```java
var num = times(digit, 1, 3).map(Ops::toString).map(Integer::valueOf);
num.parseMaybe(input("1")).get() == 1
num.parseMaybe(input("12")).get() == 12
num.parseMaybe(input("123")).get() == 123
```



#### opt(Rule\<U> inner)

Optionally matches inner rule. Always succeeds.

Returns Rule\<Optional\<U>>

```java
var optA = opt(anyOf('a'))
optA.parseMaybe(input("")).get().isEmpty() == true
optA.parseMaybe(input("a")).get().get() == 'a'
```



#### string(String pattern)

Matches exactly the pattern.

Returns Rule\<String>

```java
var world = string("world")
world.parseMaybe(input("world")).isPresent() == true
world.parseMaybe(input("worl")).isEmpty() == true
```



#### stringIgnoreCase(String pattern)

Matches the pattern case insensitive.

Returns Rule\<String>

```java
var helloCS = stringCaseInsensitive("hello")
helloCS.parseMaybe(input("HeLlO")).isPresent()
```



#### spaces(Whitespace.Config config)

Takes spaces to exhaustion according to passed configuration. Always succeeds.

Returns Rule\<Empty>

```java
var ws = spaces(Whitespace.Config.defaults()
    .withNewline(' ', '\n')
    .withSingleLineComment("//")
    .withMultilineComment(new Pair("/*", "*/")))

ws.parseMaybe(input("  \n // comment")).isPresent()
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



