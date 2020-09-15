package me.dk.exproptimizer.visitors

import me.dk.exproptimizer.ir.*


data class TermSet(
    val identifiers: MutableMap<String, Int> = mutableMapOf(),
    var literalSum: Int = 0
) {
    fun updateLiteralSum(value: Int, sign: Int = 1) {
        literalSum += sign * value
    }

    fun updateIdentifier(name: String, sign: Int = 1) {
        identifiers[name] = (identifiers[name] ?: 0) + sign
    }

    private fun expandIdentifiers(name: String, value: Int): List<Expression> =
        List(value) { Identifier(name) }

    /**
     * Normalizing the expression according to the following rules:
     * - First go the identifiers with positive sum, sorted by name
     * - Second go the identifiers with negative sum, sorted by name
     * - Then goes the sum of all literals
     * - If there is no identifiers with positive sum:
     * - If the sum of literals is positive, it goes first
     * - If the sum of literals is negative or zero, zero literal goes first
     * These rules assure the resulting expression is left skewed and,
     * due to left associativity of addition and subtraction,
     * can be printed without any brackets.
     */
    fun toExpression(): Expression {
        val sortedEntries = identifiers.entries.sortedBy { it.key }
        val positiveLiterals = sortedEntries
            .filter { it.value > 0 }
            .flatMap { expandIdentifiers(it.key, it.value) }

        val negativeLiterals = sortedEntries.filter { it.value < 0 }
            .flatMap { expandIdentifiers(it.key, -it.value) }

        return if (positiveLiterals.isNotEmpty()) {
            val exprWithPositive = positiveLiterals
                .reduce { acc, expr -> BinaryOperation(acc, expr, Operation.ADD) }
            val exprWithNegative = negativeLiterals
                .fold(exprWithPositive) { acc, expr -> BinaryOperation(acc, expr, Operation.SUB) }
            when {
                literalSum > 0 -> BinaryOperation(exprWithNegative, Literal(literalSum), Operation.ADD)
                literalSum < 0 -> BinaryOperation(exprWithNegative, Literal(-literalSum), Operation.SUB)
                else -> exprWithNegative
            }
        } else {
            if (literalSum > 0) {
                negativeLiterals.fold<Expression, Expression>(Literal(literalSum)) { acc, expr ->
                    BinaryOperation(acc, expr, Operation.SUB)
                }
            } else {
                val exprWithNegative = negativeLiterals.fold<Expression, Expression>(Literal(0)) { acc, expr ->
                    BinaryOperation(acc, expr, Operation.SUB)
                }
                if (literalSum < 0) {
                    BinaryOperation(exprWithNegative, Literal(-literalSum), Operation.SUB)
                } else {
                    exprWithNegative
                }
            }
        }

    }
}

class NormalizingOptimizer: IRTreeVisitor<Unit> {
    private val termSet = TermSet()
    private var sign = 1

    override fun visitIdentifier(expr: Identifier) {
        termSet.updateIdentifier(expr.name, sign)
    }

    override fun visitLiteral(expr: Literal) {
        termSet.updateLiteralSum(expr.value, sign)
    }

    override fun visitBinaryOperation(expr: BinaryOperation) {
        visit(expr.left)

        if (expr.operation == Operation.SUB) {
            sign *= -1
            visit(expr.right)
            sign *= -1
        } else {
            visit(expr.right)
        }
    }

    fun toExpression(): Expression = termSet.toExpression()
}