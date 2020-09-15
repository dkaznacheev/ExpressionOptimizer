package me.dk.exproptimizer

import me.dk.exproptimizer.ir.Expression
import me.dk.exproptimizer.parser.IRParser
import me.dk.exproptimizer.visitors.StringViewVisitor
import me.dk.exproptimizer.visitors.NormalizingOptimizer


object Main {

    fun parse(str: String): Expression = IRParser().parse(str)

    fun optimize(expr: Expression): Expression = NormalizingOptimizer().run {
        visit(expr)
        toExpression()
    }

    fun show(expr: Expression): String = StringViewVisitor().run {
        visit(expr)
        toString()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        require(args.isNotEmpty()) { println("The expression should be passed as program argument.") }
        args.joinToString("")
            .let(::parse)
            .let(::optimize)
            .let(::show)
            .let(::println)
    }
}