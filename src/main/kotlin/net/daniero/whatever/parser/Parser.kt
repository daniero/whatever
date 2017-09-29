package net.daniero.whatever.parser

import net.daniero.whatever.ast.Program
import net.daniero.whatever.ast.WhateverMethod
import net.daniero.whatever.ast.WhateverStatement
import java.util.*
import kotlin.collections.ArrayList

fun parse(tokens: Iterator<Token>) = Parser(tokens).parse()

private class Parser(val tokens: Iterator<Token>) {
    val parameters = Stack<Value<*>>()
    val program: MutableList<WhateverStatement> = ArrayList()

    fun parse(): Program {
        tokens.forEach {
            when (it) {
                is Value<*> -> parameters.push(it)
                is Token.Eof -> parseEof()
            }
        }
        return Program(program)
    }

    private fun parseEof() {
        while (parameters.isNotEmpty()) {
            val value = parameters.pop()
            program.add(WhateverMethod(0, 0) { whatever -> whatever.output.print(value.value) })
        }
    }

}

