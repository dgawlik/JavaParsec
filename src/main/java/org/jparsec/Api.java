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

    /**
     * Any of the enumerated chars.
     */
    public static Satisfy anyOf(Character... chars) {
        var lst = Arrays.asList(chars);
        var charsMsg = String.join(",",
                lst.stream().map(c -> "'" + c + "'").toList());
        return new Satisfy(lst::contains, "expected " + charsMsg, charsMsg);

    }

    /**
     * Captures single given char
     */
    public static Satisfy c(Character ch) {
        return new Satisfy(c -> c == ch,
                "expected '" + ch + "'", "char '" + ch + "'");
    }

    /**
     * Matches on java's `Character::isWhitespace` for single char.
     */
    public static Satisfy whitespace() {
        return new Satisfy(Character::isWhitespace, "expected whitespace", "<ws>");
    }

    /**
     * Single digit starting from '1'.
     */
    public static Satisfy nonZeroDigit() {
        return new Satisfy(c -> Character.isDigit(c) && c != '0',
                "expected non zero digit", "'1'..'9'");
    }

    /**
     * Single digit starting from '0'
     */
    public static Satisfy digit() {
        return new Satisfy(Character::isDigit, "expected digit", "'0'..'9'");
    }

    /**
     * Any alphabetic character.
     */
    public static Satisfy letter() {
        return new Satisfy(Character::isLetter, "expected letter", "alpha char");
    }

    /**
     * Alphabetic characters plus digits
     */
    public static Satisfy alphaNum() {
        return new Satisfy(Character::isLetterOrDigit,
                "expected letter or digit", "alphanum char");
    }

    /**
     * Alphabetic character lower case
     */
    public static Satisfy lower() {
        return new Satisfy(Character::isLowerCase, "expected lower char", "lower char");
    }

    /**
     * Alphabetic character upper case.
     */
    public static Satisfy upper() {
        return new Satisfy(Character::isUpperCase, "expected upper char", "upper char");
    }

    /**
     * Applies predicate on single character and succeds if predicate succeeds.
     */
    public static Satisfy satisfy(Predicate<Character> pred) {
        return new Satisfy(pred);
    }

    /**
     * Single character in between the range start..end
     */
    public static Satisfy range(Character start, Character end) {
        var rangeS = "'" + start + "'..'" + end + "'";
        return new Satisfy(c -> c >= start && c <= end,
                "expected " + rangeS, rangeS);
    }

    /**
     * Fails if any of the characters specified is matched.
     */
    public static Satisfy noneOf(Character... inverseMatchers) {
        var lst = Arrays.asList(inverseMatchers);
        var charsMsg = String.join(",",
                lst.stream().map(c -> "'" + c + "'").toList());
        return new Satisfy(c -> !lst.contains(c),
                "not expecting " + charsMsg, "none of " + charsMsg);
    }

    /**
     * Succeds if any of the matchers succeeds.
     */
    public static <T, U> Matcher<Either<T, U>> choice(Matcher<T> c1, Matcher<U> c2) {
        return Choice.choice(c1, c2);
    }

    /**
     * Succeds if any of the matchers succeeds.
     */
    public static <T, U, W> Matcher<Choice3<T, U, W>> choice(Matcher<T> c1, Matcher<U> c2, Matcher<W> c3) {
        return Choice.choice(c1, c2, c3);
    }

    /**
     * Succeds if any of the matchers succeeds.
     */
    public static <T, U, W, Z> Matcher<Choice4<T, U, W, Z>> choice(Matcher<T> c1, Matcher<U> c2,
                                                                   Matcher<W> c3, Matcher<Z> c4) {
        return Choice.choice(c1, c2, c3, c4);
    }

    /**
     * Succeds if any of the matchers succeeds.
     */
    public static <T, U, W, Z, X> Matcher<Choice5<T, U, W, Z, X>> choice(Matcher<T> c1, Matcher<U> c2,
                                                                         Matcher<W> c3, Matcher<Z> c4,
                                                                         Matcher<X> c5) {
        return Choice.choice(c1, c2, c3, c4, c5);
    }

    /**
     * Succeds if any of the matchers succeeds.
     */
    public static <T, U, W, Z, X, G> Matcher<Choice6<T, U, W, Z, X, G>> choice(Matcher<T> c1, Matcher<U> c2,
                                                                               Matcher<W> c3, Matcher<Z> c4,
                                                                               Matcher<X> c5, Matcher<G> c6) {
        return Choice.choice(c1, c2, c3, c4, c5, c6);
    }

    /**
     * Succeds if any of the matchers succeeds.
     */
    public static <T, U, W, Z, X, G, H> Matcher<Choice7<T, U, W, Z, X, G, H>> choice(Matcher<T> c1, Matcher<U> c2,
                                                                                     Matcher<W> c3, Matcher<Z> c4,
                                                                                     Matcher<X> c5, Matcher<G> c6,
                                                                                     Matcher<H> c7) {
        return Choice.choice(c1, c2, c3, c4, c5, c6, c7);
    }

    /**
     * Asserts all matchers succeed in order.
     */
    public static <T, U> Seq.SeqX<Pair<T, U>> seq(Matcher<T> one, Matcher<U> two) {
        return Seq.seq(one, two);
    }

    /**
     * Asserts all matchers succeed in order.
     */
    public static <T, U, W> Seq.SeqX<Tuple3<T, U, W>> seq(Matcher<T> one, Matcher<U> two, Matcher<W> three) {
        return Seq.seq(one, two, three);
    }

    /**
     * Asserts all matchers succeed in order.
     */
    public static <T, U, W, Z> Seq.SeqX<Tuple4<T, U, W, Z>> seq(Matcher<T> one, Matcher<U> two,
                                                                Matcher<W> three, Matcher<Z> four) {
        return Seq.seq(one, two, three, four);
    }

    /**
     * Asserts all matchers succeed in order.
     */
    public static <T, U, W, Z, Y> Seq.SeqX<Tuple5<T, U, W, Z, Y>> seq(Matcher<T> one, Matcher<U> two,
                                                                      Matcher<W> three, Matcher<Z> four,
                                                                      Matcher<Y> five) {
        return Seq.seq(one, two, three, four, five);
    }

    /**
     * Asserts all matchers succeed in order.
     */
    public static <T, U, W, Z, Y, X> Seq.SeqX<Tuple6<T, U, W, Z, Y, X>> seq(Matcher<T> one, Matcher<U> two,
                                                                            Matcher<W> three, Matcher<Z> four,
                                                                            Matcher<Y> five, Matcher<X> six) {
        return Seq.seq(one, two, three, four, five, six);
    }

    /**
     * Asserts all matchers succeed in order.
     */
    public static <T, U, W, Z, Y, X, G> Seq.SeqX<Tuple7<T, U, W, Z, Y, X, G>> seq(Matcher<T> one, Matcher<U> two,
                                                                                  Matcher<W> three, Matcher<Z> four,
                                                                                  Matcher<Y> five, Matcher<X> six,
                                                                                  Matcher<G> seven) {
        return Seq.seq(one, two, three, four, five, six, seven);
    }

    /**
     * Takes the first one of the succeeding matchers. All matchers have to
     * wrap the same type.
     */
    @SafeVarargs
    public static <U> Matcher<U> any(Matcher<U>... matchers) {
        assert matchers.length > 1;

        var it = matchers[0];
        for (int i = 1; i < matchers.length; i++) {
            it = it.any(matchers[i]);
        }

        return it;
    }

    /**
     * Zero or more occurences of matcher. Always succeeds.
     */
    public static <U> Many<U> many(Matcher<U> inner) {
        return Many.many(inner);
    }

    /**
     * One or more occurences of matcher. Fails on zero occurences.
     */
    public static <U> Many<U> some(Matcher<U> inner) {
        return Many.some(inner);
    }


    /**
     * Matches exactly X occurences of matcher.
     */
    public static <U> Times<U> times(Matcher<U> inner, int times) {
        return new Times<>(inner, times);
    }

    /**
     * For success matcher has to be matched between from and to times.
     */
    public static <U> Times<U> times(Matcher<U> inner, int from, int to) {
        return new Times<>(inner, from, to);
    }

    /**
     * Optional occurence of the matcher. Always succeds.
     */
    public static <T> Opt<T> opt(Matcher<T> inner) {
        return Opt.opt(inner);
    }

    /**
     * Captures exact string.
     */
    public static Str c(String pattern) {
        return Strings.c(pattern);
    }

    /**
     * Matches a string ignoring the case sensitivity.
     */
    public static Str stringIgnoreCase(String pattern) {
        return Strings.stringIgnoreCase(pattern);
    }

    /**
     * Takes one or more specified whitespaces. If none are specified then
     * the Java defaults are applied.
     */
    public static Spaces spaces(Character... chrs) {
        return new Spaces(chrs);
    }

    /**
     * Takes a single line comment starting with an activator string.
     */
    public static SinglelineComment comment(String act) {
        return new SinglelineComment(act);
    }

    /**
     * Takes multiline comment starting with start and ending with end activators.
     */
    public static MultilineComment multilineComment(String start, String end) {
        return new MultilineComment(start, end);
    }

    /**
     * Wraps first rule and takes up second rule to exhaustion. Second rule is
     * typically a whitespace. Only first rule is returned.
     */
    public static <T> Lexeme<T> lexeme(Matcher<T> inner, Matcher<?> ws) {
        return new Lexeme<>(inner, ws);
    }

    /**
     * Zero or more occurences of first rule separated by second rule.
     */
    public static <V, U> Matcher<List<V>> sepBy(Matcher<V> inner, Matcher<U> sep) {
        return Sep.sepBy(inner, sep);
    }

    /**
     * Fails if matcher succeeds.
     */
    public static <T> Matcher<Empty> not(Matcher<T> inner) {
        return new Not(inner);
    }

    /**
     * Allows to refer to itself before setting. Typically you handle
     * recurrence by using `recursive()` refering to it by other rules
     * and finally setting it with `set()` method.
     */
    public static <T> Recursive<T> recursive() {
        return new Recursive<>();
    }

    /**
     * Applies scope of to inner rule and increments the indentation. The indentation
     * is executed on newlines `nl()`
     */
    public static <T> Indent<T> indent(Matcher<T> inner, String pattern) {
        return new Indent<>(inner, pattern);
    }

    /**
     * Newline that is indentation sensitive.
     */
    public static Newline nl() {
        return new Newline();
    }

    /**
     * Convenience method for wrapping text in Context.
     */
    public static Context input(String text) {
        return Context.of(text);
    }

    /**
     * By default rules take only parts in greedy way leaving off the
     * rest and succeeding. By applying `eos()` we can assert that
     * text is parsed from start to finish.
     */
    public static Eos eos() {
        return new Eos();
    }
}
