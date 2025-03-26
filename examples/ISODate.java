import org.jparsec.Ops;
import org.jparsec.containers.Either.Left;
import org.jparsec.containers.Either.Right;
import org.jparsec.containers.Pair;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static java.io.IO.println;
import static org.jparsec.Api.*;

public void main() {

    var year = times(digit(), 4)
            .map(Ops::toString)
            .map(Integer::valueOf);

    var month = times(digit(), 2)
            .map(Ops::toString)
            .map(Integer::valueOf)
            .failIf(i -> i < 1 || i > 12, "wrong month");

    var day = times(digit(), 2)
            .map(Ops::toString)
            .map(Integer::valueOf)
            .failIf(i -> i > 31, "wrong day");

    var date = seq(
            year,
            anyOf('-'),
            month,
            anyOf('-'),
            day
    ).map(t -> LocalDate.of(t.one(), t.three(), t.five()));

    var hour = times(digit(), 2)
            .map(Ops::toString)
            .map(Integer::valueOf)
            .failIf(i -> i > 24, "wrong hour");

    var minute = times(digit(), 2)
            .map(Ops::toString)
            .map(Integer::valueOf)
            .failIf(i -> i > 59, "wrong minute");

    var second = times(digit(), 2)
            .map(Ops::toString)
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

    println(datetime.parse(input("2024-03-23")));
    println(datetime.parse(input("2024-03-23T11:33:01")));
    println(datetime.parse(input("2024-13-23")));
}