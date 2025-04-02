package org.jparsec;

import org.jparsec.combinator.*;
import org.jparsec.containers.*;
import org.jparsec.containers.choice.*;
import org.jparsec.containers.seq.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class Api {

    /**
     * Matches any single char.
     */
    public static Satisfy anyChar() {
        return new Satisfy(c -> true, "", "any char");
    }

    public static Satisfy anyOf(Character... chars) {
        var lst = Arrays.asList(chars);
        var charsMsg = String.join(",",
                lst.stream().map(c -> "'" + c + "'").toList());
        return new Satisfy(lst::contains, "expected " + charsMsg, charsMsg);

    }

    public static Satisfy c(Character ch) {
        return new Satisfy(c -> c == ch,
                "expected '" + ch + "'", "char '" + ch + "'");
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
        var rangeS = "'" + start + "'..'" + end + "'";
        return new Satisfy(c -> c >= start && c <= end,
                "expected " + rangeS, rangeS);
    }

    public static Satisfy noneOf(Character... inverseMatchers) {
        var lst = Arrays.asList(inverseMatchers);
        var charsMsg = String.join(",",
                lst.stream().map(c -> "'" + c + "'").toList());
        return new Satisfy(c -> !lst.contains(c),
                "not expecting " + charsMsg, "none of " + charsMsg);
    }

    public static <T, U> Matcher<Either<T, U>> choice(Matcher<T> c1, Matcher<U> c2) {
        return Choice.choice(c1, c2);
    }

    public static <T, U, W> Matcher<Choice3<T, U, W>> choice(Matcher<T> c1, Matcher<U> c2, Matcher<W> c3) {
        return Choice.choice(c1, c2, c3);
    }

    public static <T, U, W, Z> Matcher<Choice4<T, U, W, Z>> choice(Matcher<T> c1, Matcher<U> c2,
                                                                   Matcher<W> c3, Matcher<Z> c4) {
        return Choice.choice(c1, c2, c3, c4);
    }

    public static <T, U, W, Z, X> Matcher<Choice5<T, U, W, Z, X>> choice(Matcher<T> c1, Matcher<U> c2,
                                                                         Matcher<W> c3, Matcher<Z> c4,
                                                                         Matcher<X> c5) {
        return Choice.choice(c1, c2, c3, c4, c5);
    }

    public static <T, U, W, Z, X, G> Matcher<Choice6<T, U, W, Z, X, G>> choice(Matcher<T> c1, Matcher<U> c2,
                                                                               Matcher<W> c3, Matcher<Z> c4,
                                                                               Matcher<X> c5, Matcher<G> c6) {
        return Choice.choice(c1, c2, c3, c4, c5, c6);
    }

    public static <T, U, W, Z, X, G, H> Matcher<Choice7<T, U, W, Z, X, G, H>> choice(Matcher<T> c1, Matcher<U> c2,
                                                                                     Matcher<W> c3, Matcher<Z> c4,
                                                                                     Matcher<X> c5, Matcher<G> c6,
                                                                                     Matcher<H> c7) {
        return Choice.choice(c1, c2, c3, c4, c5, c6, c7);
    }

    public static <T, U> Seq.SeqX<Pair<T, U>> seq(Matcher<T> one, Matcher<U> two) {
        return Seq.seq(one, two);
    }

    public static <T, U, W> Seq.SeqX<Tuple3<T, U, W>> seq(Matcher<T> one, Matcher<U> two, Matcher<W> three) {
        return Seq.seq(one, two, three);
    }

    public static <T, U, W, Z> Seq.SeqX<Tuple4<T, U, W, Z>> seq(Matcher<T> one, Matcher<U> two,
                                                                Matcher<W> three, Matcher<Z> four) {
        return Seq.seq(one, two, three, four);
    }

    public static <T, U, W, Z, Y> Seq.SeqX<Tuple5<T, U, W, Z, Y>> seq(Matcher<T> one, Matcher<U> two,
                                                                      Matcher<W> three, Matcher<Z> four,
                                                                      Matcher<Y> five) {
        return Seq.seq(one, two, three, four, five);
    }

    public static <T, U, W, Z, Y, X> Seq.SeqX<Tuple6<T, U, W, Z, Y, X>> seq(Matcher<T> one, Matcher<U> two,
                                                                            Matcher<W> three, Matcher<Z> four,
                                                                            Matcher<Y> five, Matcher<X> six) {
        return Seq.seq(one, two, three, four, five, six);
    }

    public static <T, U, W, Z, Y, X, G> Seq.SeqX<Tuple7<T, U, W, Z, Y, X, G>> seq(Matcher<T> one, Matcher<U> two,
                                                                                  Matcher<W> three, Matcher<Z> four,
                                                                                  Matcher<Y> five, Matcher<X> six,
                                                                                  Matcher<G> seven) {
        return Seq.seq(one, two, three, four, five, six, seven);
    }

    @SafeVarargs
    public static <U> Matcher<U> any(Matcher<U>... matchers) {
        assert matchers.length > 1;

        var it = matchers[0];
        for (int i = 1; i < matchers.length; i++) {
            it = it.any(matchers[i]);
        }

        return it;
    }

    public static <U> Many<U> many(Matcher<U> inner) {
        return Many.many(inner);
    }

    public static <U> Many<U> some(Matcher<U> inner) {
        return Many.some(inner);
    }


    public static <U> Times<U> times(Matcher<U> inner, int times) {
        return new Times<>(inner, times);
    }

    public static <U> Times<U> times(Matcher<U> inner, int from, int to) {
        return new Times<>(inner, from, to);
    }

    public static <T> Opt<T> opt(Matcher<T> inner) {
        return Opt.opt(inner);
    }

    public static Str c(String pattern) {
        return Strings.c(pattern);
    }

    public static Str stringIgnoreCase(String pattern) {
        return Strings.stringIgnoreCase(pattern);
    }

    public static Spaces spaces(Character... chrs) {
        return new Spaces(chrs);
    }

    public static SinglelineComment comment(String act) {
        return new SinglelineComment(act);
    }

    public static MultilineComment multilineComment(String start, String end) {
        return new MultilineComment(start, end);
    }

    public static <T> Lexeme<T> lexeme(Matcher<T> inner, Matcher<?> ws) {
        return new Lexeme<>(inner, ws);
    }

    public static <V, U> Matcher<List<V>> sepBy(Matcher<V> inner, Matcher<U> sep) {
        return Sep.sepBy(inner, sep);
    }

    public static <T> Matcher<Empty> not(Matcher<T> inner) {
        return new Not(inner);
    }

    public static <T> Recursive<T> recursive() {
        return new Recursive<>();
    }

    public static <T> Indent<T> indent(Matcher<T> inner, String pattern) {
        return new Indent<>(inner, pattern);
    }

    public static Newline nl() {
        return new Newline();
    }

    public static Context input(String text) {
        return Context.of(text);
    }

    public static Eos eos() {
        return new Eos();
    }
}
