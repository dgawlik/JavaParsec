package org.jparsec;

import org.jparsec.combinator.*;
import org.jparsec.containers.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class Api {

    public static Satisfy any() {
        return new Satisfy(c -> true, "", "any char");
    }

    public static Satisfy anyOf(Character... chars) {
        var lst = Arrays.asList(chars);
        var charsMsg = String.join(",",
                lst.stream().map(c -> "'"+c+"'").toList());
        return new Satisfy(lst::contains, "expected " +  charsMsg, charsMsg);

    }

    public static Satisfy c(Character ch) {
        return new Satisfy(c -> c == ch,
                "expected '" + ch +"'", "char '"+ch+"'");
    }

    public static Satisfy whitespace() {
        return new Satisfy(Character::isWhitespace, "expected whitespace", "<ws>");
    }

    public static Satisfy nonZeroDigit() {
        return new Satisfy(c -> Character.isDigit(c) && c != '0',
                "expected non zero digit", "'1'..'9'");
    }

    public static Satisfy digit() {
        return new Satisfy(Character::isDigit, "expected digit", "'0'..'9'");
    }

    public static Satisfy letter() {
        return new Satisfy(Character::isLetter, "expected letter", "alpha char");
    }

    public static Satisfy alphaNum() {
        return new Satisfy(Character::isLetterOrDigit,
                "expected letter or digit", "alphanum char");
    }

    public static Satisfy lower() {
        return new Satisfy(Character::isLowerCase, "expected lower char", "lower char");
    }

    public static Satisfy upper() {
        return new Satisfy(Character::isUpperCase, "expected upper char", "upper char");
    }

    public static Satisfy satisfy(Predicate<Character> pred) {
        return new Satisfy(pred);
    }

    public static Satisfy range(Character start, Character end) {
        var rangeS = "'"+start+"'..'"+end+"'";
        return new Satisfy(c -> c >= start && c <= end,
                "expected "+rangeS, rangeS);
    }

    public static Satisfy noneOf(Character... inverseMatchers) {
        var lst = Arrays.asList(inverseMatchers);
        var charsMsg = String.join(",",
                lst.stream().map(c -> "'"+c+"'").toList());
        return new Satisfy(c -> !lst.contains(c),
                "not expecting "+charsMsg, "none of "+charsMsg);
    }

    public static <T, U> Rule<Either<T, U>> choice(Rule<T> c1, Rule<U> c2) {
        return Choice.choice(c1, c2);
    }

    public static <T, U, W> Rule<Choice3<T, U, W>> choice(Rule<T> c1, Rule<U> c2, Rule<W> c3) {
        return Choice.choice(c1, c2, c3);
    }

    public static <T, U, W, Z> Rule<Choice4<T, U, W, Z>> choice(Rule<T> c1, Rule<U> c2,
                                                                Rule<W> c3, Rule<Z> c4) {
        return Choice.choice(c1, c2, c3, c4);
    }

    public static <T, U, W, Z, X> Rule<Choice5<T, U, W, Z, X>> choice(Rule<T> c1, Rule<U> c2,
                                                                      Rule<W> c3, Rule<Z> c4,
                                                                      Rule<X> c5) {
        return Choice.choice(c1, c2, c3, c4, c5);
    }

    public static <T, U, W, Z, X, G> Rule<Choice6<T, U, W, Z, X, G>> choice(Rule<T> c1, Rule<U> c2,
                                                                            Rule<W> c3, Rule<Z> c4,
                                                                            Rule<X> c5, Rule<G> c6) {
        return Choice.choice(c1, c2, c3, c4, c5, c6);
    }

    public static <T, U, W, Z, X, G, H> Rule<Choice7<T, U, W, Z, X, G, H>> choice(Rule<T> c1, Rule<U> c2,
                                                                                  Rule<W> c3, Rule<Z> c4,
                                                                                  Rule<X> c5, Rule<G> c6,
                                                                                  Rule<H> c7) {
        return Choice.choice(c1, c2, c3, c4, c5, c6, c7);
    }

    public static <T, U> Rule<Pair<T, U>> seq(Rule<T> one, Rule<U> two) {
        return Seq.seq(one, two);
    }

    public static <T, U, W> Rule<Tuple3<T, U, W>> seq(Rule<T> one, Rule<U> two, Rule<W> three) {
        return Seq.seq(one, two, three);
    }

    public static <T, U, W, Z> Rule<Tuple4<T, U, W, Z>> seq(Rule<T> one, Rule<U> two,
                                                            Rule<W> three, Rule<Z> four) {
        return Seq.seq(one, two, three, four);
    }

    public static <T, U, W, Z, Y> Rule<Tuple5<T, U, W, Z, Y>> seq(Rule<T> one, Rule<U> two,
                                                                  Rule<W> three, Rule<Z> four,
                                                                  Rule<Y> five) {
        return Seq.seq(one, two, three, four, five);
    }

    public static <T, U, W, Z, Y, X> Rule<Tuple6<T, U, W, Z, Y, X>> seq(Rule<T> one, Rule<U> two,
                                                                        Rule<W> three, Rule<Z> four,
                                                                        Rule<Y> five, Rule<X> six) {
        return Seq.seq(one, two, three, four, five, six);
    }

    public static <T, U, W, Z, Y, X, G> Rule<Tuple7<T, U, W, Z, Y, X, G>> seq(Rule<T> one, Rule<U> two,
                                                                              Rule<W> three, Rule<Z> four,
                                                                              Rule<Y> five, Rule<X> six,
                                                                              Rule<G> seven) {
        return Seq.seq(one, two, three, four, five, six, seven);
    }

    public static <U> Rule<List<U>> many(Rule<U> inner) {
        return Many.many(inner);
    }

    public static <U> Rule<List<U>> some(Rule<U> inner) {
        return Many.some(inner);
    }


    public static <U> Rule<List<U>> times(Rule<U> inner, int times) {
        return Times.times(inner, times);
    }

    public static <U> Rule<List<U>> times(Rule<U> inner, int from, int to) {
        return Times.times(inner, from, to);
    }

    public static <T> Opt<T> opt(Rule<T> inner) {
        return Opt.opt(inner);
    }

    public static Str string(String pattern) {
        return Strings.string(pattern);
    }

    public static Str stringIgnoreCase(String pattern) {
        return Strings.stringIgnoreCase(pattern);
    }

    public static Whitespace spaces(Whitespace.Config config) {
        return Whitespace.spaces(config);
    }

    public static <T> Lexeme<T> lexeme(Rule<T> inner, Whitespace ws) {
        return Lexeme.lexeme(inner, ws);
    }

    public static <V, U> Rule<List<V>> sepBy(Rule<V> inner, Rule<U> sep) {
        return Sep.sepBy(inner, sep);
    }

    public static <T> Rule<Empty> not(Rule<T> inner) {
        return new Not(inner);
    }

    public static <T> Recursive<T> recursive() {
        return new Recursive<>();
    }

    public static <T> Indent<T> indent(Rule<T> inner, String pattern) {
        return new Indent<>(inner, pattern);
    }

    public static Newline nl() {
        return new Newline();
    }

    public static ParseContext input(String text) {
        return ParseContext.of(text);
    }

    public static Eos eos() {
        return new Eos();
    }
}
