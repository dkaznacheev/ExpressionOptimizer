package me.dk.exproptimizer

import kotlin.test.Test
import kotlin.test.assertEquals
import me.dk.exproptimizer.Main.parse
import me.dk.exproptimizer.ir.BinaryOperation
import me.dk.exproptimizer.ir.Identifier
import me.dk.exproptimizer.ir.Literal
import me.dk.exproptimizer.ir.Operation

class ParsingTests {

    @Test
    fun literalParses() {
        assertEquals(parse("1232"), Literal(1232))
        assertEquals(parse("0"), Literal(0))
    }

    @Test
    fun identifierParses() {
        assertEquals(parse("x"), Identifier("x"))
        assertEquals(parse("var123a"), Identifier("var123a"))
    }

    @Test
    fun binaryOperationParses() {
        assertEquals(parse("1 + 2"), BinaryOperation(Literal(1), Literal(2), Operation.ADD))
        assertEquals(parse("1 - x"), BinaryOperation(Literal(1), Identifier("x"), Operation.SUB))
    }

    @Test
    fun bracketsParse() {
        assertEquals(parse("1 - (2 - 3)"),
            BinaryOperation(
                Literal(1),
                BinaryOperation(
                    Literal(2),
                    Literal(3),
                    Operation.SUB),
                Operation.SUB
        ))

        assertEquals(parse("(1 - 2) - 3"),
            BinaryOperation(
                BinaryOperation(
                    Literal(1),
                    Literal(2),
                    Operation.SUB),
                Literal(3),
                Operation.SUB
        ))
    }

    @Test
    fun complexExpressionParses() {
        assertEquals(parse("(a + 10) - (b - (c + 2 + b))"),
            BinaryOperation(
                BinaryOperation(
                    Identifier("a"),
                    Literal(10),
                    Operation.ADD),
                BinaryOperation(
                    Identifier("b"),
                    BinaryOperation(
                        BinaryOperation(
                            Identifier("c"),
                            Literal(2),
                            Operation.ADD),
                        Identifier("b"),
                        Operation.ADD),
                    Operation.SUB),
                Operation.SUB))
    }
}