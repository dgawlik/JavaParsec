//JAVA 24
//PREVIEW
//DEPS org.jparsec:JavaParsec:1.0.6


import org.jparsec.Api;
import org.jparsec.Ops;
import org.jparsec.combinator.Recursive;
import org.jparsec.combinator.Whitespace;
import org.jparsec.containers.Either;
import org.jparsec.containers.Err;
import org.jparsec.containers.Ok;
import org.jparsec.containers.ParseContext;

import static java.lang.System.out;
import static org.jparsec.Api.*;

sealed interface Expression {
    record Binary(Expression left, Expression right, Character op) implements Expression {
    }

    record Value(int val) implements Expression {
    }

    record Parens(Expression e) implements Expression {
    }

    default int predecence() {
        if (this instanceof Binary(Expression e1, Expression e2, Character op)) {
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
            c("0x"),
            times(any(digit(), range('a', 'f')), 1, 8).s()
    ).map(Ops::takeSecond);

    var rawInteger = any(c("0"),
            seq(nonZeroDigit(),
                    many(digit()).s()).s());

    var whitespace = spaces(Whitespace.Config.defaults());

    var integer = lexeme(any(hexInteger, rawInteger), whitespace)
            .map(Integer::valueOf);

    var op = lexeme(anyOf('+', '-', '*', '/', '%'), whitespace);

    Recursive<Expression> expr = Api.recursive();

    var value = integer.map(i -> new Expression.Value(i));

    var parens = seq(
            lexeme(c('('), whitespace),
            expr,
            lexeme(c(')'), whitespace)
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
        case Ok(Expression e, ParseContext ctx) -> {
            out.println(e.calculate());
        }
        case Err e -> {
            out.println(e.errorPrettyPrint());
        }
    }
}