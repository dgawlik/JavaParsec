import org.jparsec.Api;
import org.jparsec.Ops;
import org.jparsec.combinator.Recursive;
import org.jparsec.combinator.Whitespace;
import org.jparsec.containers.Either;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;

import static java.io.IO.println;
import static org.jparsec.Api.*;

sealed interface Expression {
    record Binary(Expression left, Expression right, Character op) implements Expression {
    }

    record Value(int val) implements Expression {
    }

    record Parens(Expression e) implements Expression {
    }

    default int predecence() {
        if (this instanceof Binary(_, _, Character op)) {
            return switch (op) {
                case '-', '+' -> 1;
                case '*', '/' -> 2;
                case '%' -> 3;
                default -> throw new IllegalArgumentException("Unknown operator");
            };
        } else if (this instanceof Parens p) {
            return 10;
        } else {
            return 0;
        }
    }

    default int runOp(int left, int right, Character op) {
        return switch (op) {
            case '+' -> left + right;
            case '-' -> left - right;
            case '*' -> left * right;
            case '/' -> left / right;
            case '%' -> left % right;
            default -> throw new IllegalArgumentException("Unknown op");
        };
    }

    default int calculate() {
        return switch (this) {
            case Parens(Expression e) -> e.calculate();
            case Value(int val) -> val;
            case Binary(Expression lhs, Expression rhs, Character op) -> {
                if (this.predecence() < rhs.predecence() && rhs instanceof Binary b) {
                    yield runOp(runOp(lhs.calculate(), b.left.calculate(), op),
                            b.right.calculate(), b.op);
                } else {
                    yield runOp(lhs.calculate(), rhs.calculate(), op);
                }
            }
        };
    }
}

public void main() {

    var hexInteger = seq(
            string("0x"),
            times(digit().or(range('a', 'f')).map(Ops::takeAny),
                    1, 8)
                    .map(Ops::toString)
    ).map(Ops::takeSecond);

    var rawInteger = string("0")
            .or(nonZeroDigit()
                    .seq(many(digit())).map(Ops::prepend)
                    .map(Ops::toString))
            .map(Ops::takeAny);

    var whitespace = spaces(Whitespace.Config.defaults());

    var integer = lexeme(hexInteger.or(rawInteger).map(Ops::takeAny), whitespace)
            .map(Integer::valueOf);

    var op = lexeme(anyOf('+', '-', '*', '/', '%'), whitespace);

    Recursive<Expression> expr = Api.recursive();

    var value = integer.map(i -> new Expression.Value(i));

    var parens = seq(
            lexeme(anyOf('('), whitespace),
            expr,
            lexeme(anyOf(')'), whitespace)
    ).map(Ops::takeMiddle).map(Expression.Parens::new);

    var binary = seq(value.or(parens).map(e -> {
        return switch(e) {
            case Either.Left(Expression.Value v) -> (Expression) v;
            case Either.Right(Expression.Parens p) -> (Expression) p;
        };
    }), op, expr)
            .map(tuple ->
                    new Expression.Binary(tuple.one(), tuple.three(), tuple.two()));


    expr.set(choice(parens, binary, value).map(Ops::takeAny3Poly));

    var result = expr.parse(input("3*2+1*(1+2)"));

    switch (result) {
        case Ok(Expression e, _) -> {
            println(e.calculate());
        }
        case Err e -> {
            println(e.errorPrettyPrint());
        }
    }
}