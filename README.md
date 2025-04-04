# JavaParsec
Parser combinators for Java 21

[Documentation](https://dgawlik.github.io/JavaParsec-docs/ )

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


## Examples

There are some non-trivial examples in [examples](examples) folder.

* parsing CSV tables
* expression calculator
* parsing iso datetime
* simple indentation
* simplified YAML file

## License

This repository is licensed under Apache License, version 2.0

## Contributing

* submit issues
* submit PRs 
* write benchmarks
* help with imperative, fast parser generation

## Thanks

If you liked it a little, give it a star!