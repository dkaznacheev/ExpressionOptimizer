package me.dk.exproptimizer.parser

import me.dk.exproptimizer.ExprBaseVisitor
import me.dk.exproptimizer.ExprLexer
import me.dk.exproptimizer.ExprParser
import me.dk.exproptimizer.ir.*
import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams

class IRVisitor: ExprBaseVisitor<Expression>() {
    override fun visitExpression(ctx: ExprParser.ExpressionContext): Expression {
        if (ctx.children.first().text == "(")
            return visit(ctx.children[1])

        if (ctx.op != null) {
            return BinaryOperation(
                visit(ctx.left),
                visit(ctx.right),
                if (ctx.op.text == "+") Operation.ADD else Operation.SUB
            )
        }

        return super.visitExpression(ctx)
    }

    override fun visitIdentifier(ctx: ExprParser.IdentifierContext): Expression {
        return Identifier(ctx.text)
    }

    override fun visitLiteral(ctx: ExprParser.LiteralContext): Expression {
        return Literal(ctx.text.toInt())
    }
}


class IRParser {
    fun parse(exprString: String): Expression {
        val lexer = ExprLexer(CharStreams.fromString(exprString))
        val parser = ExprParser(BufferedTokenStream(lexer))
        val visitor = IRVisitor()
        return visitor.visit(parser.expression())
    }
}