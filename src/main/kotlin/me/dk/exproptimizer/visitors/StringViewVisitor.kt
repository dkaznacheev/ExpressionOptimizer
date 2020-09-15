package me.dk.exproptimizer.visitors

import me.dk.exproptimizer.ir.BinaryOperation
import me.dk.exproptimizer.ir.Identifier
import me.dk.exproptimizer.ir.Literal
import me.dk.exproptimizer.ir.Operation

class StringViewVisitor: IRTreeVisitor<Unit> {
    private val builder = StringBuilder()

    override fun visitBinaryOperation(expr: BinaryOperation) {
        visit(expr.left)
        builder.append(" ${expr.operation.symbol} ")
        if (expr.operation == Operation.SUB && expr.right is BinaryOperation) {
            builder.append("(")
            visit(expr.right)
            builder.append(")")
        } else {
            visit(expr.right)
        }

    }

    override fun visitIdentifier(expr: Identifier) {
        builder.append(expr.name)
    }

    override fun visitLiteral(expr: Literal) {
        builder.append(expr.value)
    }

    override fun toString(): String {
        return builder.toString()
    }
}