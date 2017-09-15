package net.daniero.whatever.parser

import net.daniero.whatever.ast.*
import java.util.*
import kotlin.collections.ArrayList

fun parse(tokens: Iterator<Token>) = Parser(tokens).parse()

private class Parser(val tokens: Iterator<Token>) {
    val parameters = Stack<Literal<*>>()
    val program: MutableList<Statement> = ArrayList()

    fun parse(): Program {
        tokens.forEach {
            when (it) {
                is Literal<*> -> parameters.push(it)
                is Token.Eof -> parseEof()
            }
        }
        return Program(program)
    }

    private fun parseEof() {
        while (parameters.isNotEmpty()) {
            val literal = parameters.pop()
            program.add { whatever -> whatever.output.print(literal.value) }
        }
    }

}
