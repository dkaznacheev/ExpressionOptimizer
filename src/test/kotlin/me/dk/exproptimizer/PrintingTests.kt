package me.dk.exproptimizer

import me.dk.exproptimizer.Main.parse
import me.dk.exproptimizer.Main.show
import kotlin.test.Test
import kotlin.test.assertEquals

class PrintingTests {

    @Test
    fun literalPrints() {
        assertEquals(show(parse("123")), "123")
    }

    @Test
    fun identifierPrints() {
        assertEquals(show(parse("x")), "x")
    }

    @Test
    fun simpleExpressionPrints() {
        assertEquals(show(parse("a + 1 - b")), "a + 1 - b")
    }

    @Test
    fun bracketsExpressionPrints() {
        assertEquals(show(parse("a - (1 - b)")), "a - (1 - b)")
    }

    @Test
    fun leftAssociativePrints() {
        assertEquals(show(parse("((a - b) - c) - d")), "a - b - c - d")
    }
}