---
description: Type-safe parser combinators for modern Java
---

# Introduction

### Motivation

_**JavaParsec** implements parsing in pure functional way_

Writing a parser for a programming language is a big commitment. The complexity is prohibitive to just quickly experimenting with various ideas at hand.&#x20;

Pure functional languages have three nice properties

* **REPL and quick iteration loops** -  code consists of small or medium-sized functions that are directly testable in eval loop. You don't even have to write tests since you are already testing it in REPL.
* **composability without side effects** - because there is no mutable state the work boils down to building big functions from smaller functions. Once done - it is done, hardly ever the code needs runtime debugging, which especially for complex parsers is  desired property.
* **delegating correctness checking to type checker -** type checker is your friend, the more restrictive it is the less debugging in runtime.&#x20;

**JavaParsec** brings this coding experience to Java land. It is a toolkit of parser combinators - little parser functions that match given pattern in a greedy manner. Every such parser is composable with another one and together they are yet another parser.

It makes heavy use of generics and pattern matching and there are no null pointer and class cast exceptions. The types themselves describe parsers very well so looking at the type gives a good clue what it is doing.

Typical workflow is to create a file with Java 21 simplified main and iteratively work bottom up. You write some one-line parser, assert examples and counter examples and move to next. Then you compose them and test in similar way.

&#x20;It is not as fast as imperative parser but the performance is not like order of magnitude worse. There is some work underway on regex compiling and tokenization which will further speed things up. Final step will be generating imperative parser from the tree of parser combinators themselves, which by the way is intereresting engineering challenge (but certainly doable).

### Installation



