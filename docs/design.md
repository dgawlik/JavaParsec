---
description: Core abstractions in the library
---

# Concepts

JavaParsec is a port of Haskell's Parsec to Java 21. The implementation and naming  are similar, but technically parsers are not monads. Design is closer to strategy pattern from OOP.

Every grammar rule is expressible in terms of **Matcher** parametrized by returned type.

```java
public abstract class Matcher<T> {
...
 public abstract MatchResult<T> parse(Context ctx);
...
}
```

**Matcher** is lowest common denominator between specializations. It acts in a greedy manner starting from position designated by **Context,** taking only matching part of the string and advancing the context this far ahead. It also encapsulates logic of creating domain objects from text via **map** method. It returns **MatchResult** that is a sum type of **Ok** _and **Err**_

```java
public sealed interface MatchResult<T> permits Ok, Err {
...
public record Ok<T>(T value, Context ctx) implements MatchResult<T> {}
...
public record Err<T>(String error, Context ctx) implements MatchResult<T> {}

```

On success Ok is returned with new **Context** which among others has index advanced by parsed part. The result can be matched in switch/instanceof expressions (longer way) or via convenience methods **isOk(), ok(), error().**&#x20;

**Context** is passed around between parsers and carries all information necessary for parsing.

```java
public class Context {
    public int line;
    public int column;
    public int index;
    public String content;
    public String indentPattern;
    public int indentLevel;
    public Deque<String> traceErrors = new LinkedList<>();
    public Deque<String> traceErrorsSnapshot = new LinkedList<>();
    public List<String> allErrors = new ArrayList<>();
    ...
}
```

There are 2 core combinators used frequently: **Choice and Seq.**

**Choice** is a set of classes {Either, Choice3, ..., Choice7} and their implementing variants One, Two, ...,  Seven. Choice tries options in order starting from the first one so it is important to put most specific rule first. On top of that there is static method from the api implement like for example this

```java
  public static <T, U, W, Z, X> Matcher<Choice5<T, U, W, Z, X>> choice(Matcher<T> c1, Matcher<U> c2,
                                                                         Matcher<W> c3, Matcher<Z> c4,
                                                                         Matcher<X> c5) {
        return Choice.choice(c1, c2, c3, c4, c5);
    }
    
...
static <T, U, W, Z, X> Matcher<Choice5<T, U, W, Z, X>> choice(Matcher<T> c1, Matcher<U> c2,
                                                                  Matcher<W> c3, Matcher<Z> c4,
                                                                  Matcher<X> c5) {
        return c1.or(c2).or(c3).or(c4).or(c5).map(nested -> switch (nested) {
            case Left(Left(Left(Left(T val)))) -> new One<>(val);
            case Left(Left(Left(Right(U val)))) -> new Two<>(val);
            case Left(Left(Right(W val))) -> new Three<>(val);
            case Left(Right(Z val)) -> new Four<>(val);
            case Right(X val) -> new Five<>(val);
        });
    }
```

**Seq** matcher asserts all the components are parsed in order. It has variants 2 through 7. Then it returns a tuple. Ex:

```java
public record Tuple4<T, U, W, Z>(T one, U two, W three, Z four) 
```

Choice and Seq allow type safe operations downstream.

Other composites are Many and Some that paramterize Matcher with List\<T>. Some fails if there is not at least one element.

Useful combinators are also **Whitespace** and **Lexeme.** Whitespace takes in a configuration containing three things:

* what is a whitespace
* what is a single line comment
* what is a multiline comment

Then combined with **spaces** operator it takes whitespaces and comments up to exhaustion.

**Lexeme** builds on top of it - wraps matcher inside and consumes all whitespace after it.

```java
var ch = anyOf('a');
var ws = spaces(Whitespace.Config.defaults());
var lexeme = lexeme(ch, ws);
var result = lexeme.parse("a \n \t ");

if (result instanceof Ok(Character c, Context resCtx)){
    Assertions.assertEquals('a', (char) c);
    Assertions.assertEquals(resCtx.content.length(), resCtx.index);
} else {
    Assertions.fail();
}
```

As it turns out there is not an easy way to handle indentation in functional way. The way it is done here is a little bit of shortcut.

If used like this

```java
indent(seq(nl(), list)
```

Indent increments indentation level in Context. Then each matching of newline **nl()** takes '\n' plus trims ident string times the indentation level afterwards. After indent goes out of scope it decrements the indentation level. To see an example see _YamlFile.java_ in examples folder.

