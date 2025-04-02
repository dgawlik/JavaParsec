package org.jparsec;

import org.jparsec.containers.choice.*;
import org.jparsec.containers.seq.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class Ops<T> {

    private final T holder;
    private final BinaryOperator<T> reducer;

    private Ops(T holder) {
        this(holder, null);
    }

    private Ops(T holder, BinaryOperator<T> reducer) {
        this.holder = holder;
        this.reducer = reducer;
    }

    public static <V> Ops<V> ops(V holder) {
        return new Ops<>(holder);
    }

    public static <V> Ops<V> ops(V holder, BinaryOperator<V> reducer) {
        return new Ops<>(holder, reducer);
    }

    public static <V> List<V> flatten(List<List<V>> lst) {
        var merge = new ArrayList<V>();
        lst.forEach(merge::addAll);
        return merge;
    }

    public static <V> List<V> singleton(V el) {
        return List.of(el);
    }

    public T reduce(List<T> lst) {
        return lst.stream().reduce(holder, reducer);
    }

    public String sepJoin(List<String> lst) {
        if (holder instanceof String s) {
            return String.join(s, lst);
        }
        throw new IllegalStateException("Not implemented for not-strings");
    }

    public static <T> T takeAny(Either<T, T> e) {
        return switch (e) {
            case Either.Left(T value) -> value;
            case Either.Right(T value) -> value;
        };
    }

    public static <T> T takeAny(Choice3<T, T, T> choice) {
        return switch (choice) {
            case One(T value) -> value;
            case Two(T value) -> value;
            case Three(T value) -> value;
        };
    }

    public static <T> T takeAny(Choice4<T, T, T, T> choice) {
        return switch (choice) {
            case One(T value) -> value;
            case Two(T value) -> value;
            case Three(T value) -> value;
            case Four(T value) -> value;
        };
    }

    public static <T> T takeAny(Choice5<T, T, T, T, T> choice) {
        return switch (choice) {
            case One(T value) -> value;
            case Two(T value) -> value;
            case Three(T value) -> value;
            case Four(T value) -> value;
            case Five(T value) -> value;
        };
    }

    public static <T> T takeAny(Choice6<T, T, T, T, T, T> choice) {
        return switch (choice) {
            case One(T value) -> value;
            case Two(T value) -> value;
            case Three(T value) -> value;
            case Four(T value) -> value;
            case Five(T value) -> value;
            case Six(T value) -> value;
        };
    }

    public static <T> T takeAny(Choice7<T, T, T, T, T, T, T> choice) {
        return switch (choice) {
            case One(T value) -> value;
            case Two(T value) -> value;
            case Three(T value) -> value;
            case Four(T value) -> value;
            case Five(T value) -> value;
            case Six(T value) -> value;
            case Seven(T value) -> value;
        };
    }


    public static <V> List<V> seqToList(Pair<V, V> p) {
        return List.of(p.first(), p.second());
    }

    public static <V> List<V> seqToList(Tuple3<V, V, V> p) {
        return List.of(p.one(), p.two(), p.three());
    }

    public static <V> List<V> seqToList(Tuple4<V, V, V, V> p) {
        return List.of(p.one(), p.two(), p.three(), p.four());
    }

    public static <V> List<V> seqToList(Tuple5<V, V, V, V, V> p) {
        return List.of(p.one(), p.two(), p.three(), p.four(), p.five());
    }

    public static <V> List<V> seqToList(Tuple6<V, V, V, V, V, V> p) {
        return List.of(p.one(), p.two(), p.three(), p.four(), p.five(), p.six());
    }

    public static <V> List<V> seqToList(Tuple7<V, V, V, V, V, V, V> p) {
        return List.of(p.one(), p.two(), p.three(), p.four(), p.five(), p.six(), p.seven());
    }

    public static String concat(Pair<String, String> p) {
        return p.first() + p.second();
    }

    public static String concat(Tuple3<String, String, String> p) {
        return p.one() + p.two() + p.three();
    }

    public static String concat(Tuple4<String, String, String, String> p) {
        return p.one() + p.two() + p.three() + p.four();
    }

    public static String concat(Tuple5<String, String, String, String, String> p) {
        return p.one() + p.two() + p.three() + p.four() + p.five();
    }

    public static String concat(Tuple6<String, String, String, String, String, String> p) {
        return p.one() + p.two() + p.three() + p.four() + p.five() + p.six();
    }

    public static String concat(Tuple7<String, String, String, String, String, String, String> p) {
        return p.one() + p.two() + p.three() + p.four() + p.five() + p.six() + p.seven();
    }

    public static String concat(List<String> lst) {
        return String.join("", lst);
    }

    public static <T> T takeAny3Poly(Choice3 triple) {
        return switch (triple) {
            case One o -> (T) o.value();
            case Two t -> (T) t.value();
            case Three th -> (T) th.value();
        };
    }

    public static String toString(List<Character> lc) {
        return lc.stream().map(String::valueOf).collect(Collectors.joining());
    }

    public static String toString(Pair<Character, Character> p) {
        return "" + p.first() + p.second();
    }

    public static String toString(Character c) {
        return "" + c;
    }

    public static <T, U> T takeFirst(Pair<T, U> p) {
        return p.first();
    }

    public static <T, U> U takeSecond(Pair<T, U> p) {
        return p.second();
    }

    public static <T, U, W> U takeMiddle(Tuple3<T, U, W> tuple) {
        return tuple.two();
    }

    public static <T, U, W> T takeFirst(Tuple3<T, U, W> tuple) {
        return tuple.one();
    }

    public static <T, U, W> W takeLast(Tuple3<T, U, W> tuple) {
        return tuple.three();
    }

    public static <T> List<T> append(Pair<List<T>, T> p) {
        var newLst = new ArrayList<>(p.first());
        newLst.add(p.second());
        return newLst;
    }

    public static <T> List<T> prepend(Pair<T, List<T>> p) {
        var newLst = new ArrayList<T>();
        newLst.add(p.first());
        newLst.addAll(p.second());
        return newLst;
    }
}
