//JAVA 24
//PREVIEW
//DEPS org.jparsec:JavaParsec:1.1.0


import org.jparsec.containers.choice.Either.Left;
import org.jparsec.containers.choice.Either.Right;
import org.jparsec.containers.seq.Pair;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static java.lang.System.out;

import static org.jparsec.Api.*;

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
            c('-'),
            month,
            c('-'),
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
            c('T'),
            hour,
            c(':'),
            minute,
            c(':'),
            second
    ).map(t -> LocalTime.of(t.two(), t.four(), t.six()));

    var datetime = choice(
            seq(date, time),
            date
    ).map(e -> switch (e) {
        case Left(Pair<LocalDate, LocalTime> p) -> new Left<>(  LocalDateTime.of(p.first(), p.second()));
        case Right(LocalDate value) -> new Right<>(value);
    });

    out.println(datetime.parse("2024-03-23"));
    out.println(datetime.parse("2024-03-23T11:33:01"));
    out.println(datetime.parse("2024-13-23"));
}