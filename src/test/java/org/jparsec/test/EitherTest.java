package org.jparsec.test;

import org.jparsec.containers.Either;
import org.jparsec.containers.Either.Left;
import org.jparsec.containers.Either.Right;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EitherTest {

    @Test
    public void simple_test() {
        Either<Integer, String> value = new Left<>(10);
        Either<Integer, String> value2 = new Right<>("hello");

        assertEither(value);
        assertEither(value2);
    }

    @Test
    public void compose_test() {
        Either<Integer, Either<Boolean, String>> v1 = new Left<>(10);
        Either<Integer, Either<Boolean, String>> v2 = new Right<>(new Left<>(true));
        Either<Integer, Either<Boolean, String>> v3 = new Right<>(new Right<>("hello"));

        assertComposedEither(v1);
        assertComposedEither(v2);
        assertComposedEither(v3);
    }

    private <T, U> void assertEither(Either<T, U> e) {
        switch (e) {
            case Left(Integer i) -> Assertions.assertEquals(10, (int) i);
            case Right(String s) -> Assertions.assertEquals("hello", s);
            default -> Assertions.fail();
        }
    }

    private <T, U, Z> void assertComposedEither(Either<T, Either<U, Z>> e) {
        switch (e) {
            case Left(Integer i) -> Assertions.assertEquals(10, (int) i);
            case Right(Left(Boolean b)) -> Assertions.assertEquals(true, b);
            case Right(Right(String s)) -> Assertions.assertEquals("hello", s);
            default -> Assertions.fail();
        }
    }
}
