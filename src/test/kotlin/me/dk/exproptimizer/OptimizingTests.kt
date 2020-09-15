package me.dk.exproptimizer

import me.dk.exproptimizer.Main.optimize
import me.dk.exproptimizer.Main.parse
import me.dk.exproptimizer.ir.BinaryOperation
import me.dk.exproptimizer.ir.Identifier
import me.dk.exproptimizer.ir.Literal
import me.dk.exproptimizer.ir.Operation
import kotlin.test.Test
import kotlin.test.assertEquals


class OptimizingTests {

    @Test
    fun simpleExpressionOptimizes() {
        assertEquals(optimize(parse("a + 0")),
            Identifier("a")
        )
    }

    @Test
    fun constantPropagationOptimizes() {
        assertEquals(optimize(parse("(3 - 1) + a + 1 - (2 + 4)")),
            BinaryOperation(
                Identifier("a"),
                Literal(3),
                Operation.SUB
            )
        )
    }

    @Test
    fun identifiersEliminationOptimizes() {
        assertEquals(optimize(parse("a - (a - b)")),
            Identifier("b")
        )
    }
}