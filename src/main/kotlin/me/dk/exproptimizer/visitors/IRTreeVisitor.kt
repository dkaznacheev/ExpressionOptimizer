package me.dk.exproptimizer.visitors

import me.dk.exproptimizer.ir.BinaryOperation
import me.dk.exproptimizer.ir.Expression
import me.dk.exproptimizer.ir.Identifier
import me.dk.exproptimizer.ir.Literal

interface IRTreeVisitor<T>  {
    fun visit(expr: Expression): T {
        return when (expr) {
            is BinaryOperation -> visitBinaryOperation(expr)
            is Identifier -> visitIdentifier(expr)
            is Literal -> visitLiteral(expr)
        }
    }

    fun visitBinaryOperation(expr: BinaryOperation): T
    fun visitIdentifier(expr: Identifier): T
    fun visitLiteral(expr: Literal): T
}