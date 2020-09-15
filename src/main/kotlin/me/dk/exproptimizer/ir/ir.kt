package me.dk.exproptimizer.ir

/**
 * Intermediate representation of expression used in optimization and printing
 */

enum class Operation(val symbol: String){
    ADD("+"),
    SUB("-")
}

sealed class Expression

data class BinaryOperation(val left: Expression, val right: Expression, val operation: Operation): Expression()
data class Identifier(val name: String): Expression()
data class Literal(val value: Int): Expression()