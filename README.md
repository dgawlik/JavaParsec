# JavaParsec
Parser combinators for Java 24

## Motivation

Writing a parser for new programming language is a big commitment.
The complexity is prohibitive to just quickly experimenting with
various ideas at hand.

Pure functional languages are easier to write parsers, because of:

* *REPL and quick iteration loops* -  code consists of small or medium-sized
  functions that are directly testable in eval loop.
  You don't even have to write tests since you are already testing it in REPL.

* *composability without side effects* - because there is no mutable state
  the work boils down to wiring big functions from smaller functions.
  Once done - it is done, hardly ever the code needs runtime debugging,
  which especially for complex parsers is  desired property.

* *delegating correctness checking to type checker* - the more checking is delegated
  to types, the less runtime debugging, plus your IDE can more help you


*JavaParsec* brings this coding experience to Java land.
It is a toolkit of parser combinators - parser objects both primitive and composite
that match given pattern in a greedy manner. Every such parser is composable
with another one and together they form yet another (more generic) parser.

There are various way that it can be used, but it was designed primarily for
quick experimentation and fast debug loops. This is why each parser is already complete
in its own and ready to use. And you run it on whatever you like.

For usage there a several recommended options:

* scratch files in IDE
* jbang with VSCode editing
* jupyter java notebooks
* java 24 implicit class main functions


## Quick example

```java
public void main() {

    var year = times(digit(), 4).str()
            .map(Integer::valueOf);

    var month = times(digit(), 2).str()
            .map(Integer::valueOf)
            .failIf(i -> i < 1 || i > 12, "wrong month");

    var day = times(digit(), 2).str()
            .map(Integer::valueOf)
            .failIf(i -> i > 31, "wrong day");

    var date = seq(
            year,
            anyOf('-'),
            month,
            anyOf('-'),
            day
    ).map(t -> LocalDate.of(t.one(), t.three(), t.five()));

    var hour = times(digit(), 2).str()
            .map(Integer::valueOf)
            .failIf(i -> i > 24, "wrong hour");

    var minute = times(digit(), 2).str()
            .map(Integer::valueOf)
            .failIf(i -> i > 59, "wrong minute");

    var second = times(digit(), 2).str()
            .map(Integer::valueOf)
            .failIf(i -> i > 59, "wrong second");

    var time = seq(
            anyOf('T'),
            hour,
            anyOf(':'),
            minute,
            anyOf(':'),
            second
    ).map(t -> LocalTime.of(t.two(), t.four(), t.six()));

    var datetime = choice(
            seq(date, time),
            date
    ).map(e -> switch (e) {
        case Left(Pair<LocalDate, LocalTime> p) -> new Left<>(  LocalDateTime.of(p.first(), p.second()));
        case Right(LocalDate value) -> new Right<>(value);
    });

    println(datetime.parse("2024-03-23"));
    println(datetime.parse("2024-03-23T11:33:01"));
    println(datetime.parse("2024-13-23"));
}
```
