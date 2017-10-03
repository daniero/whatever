package net.daniero.whatever.parser

import net.daniero.util.pop
import net.daniero.whatever.ast.*
import org.funktionale.partials.*
import java.util.*
import kotlin.collections.ArrayList

fun parse(tokens: Iterator<Token>) = Parser(tokens).parse()

private class Parser(val tokens: Iterator<Token>) {
    val parameters = Stack<Value>()
    val program: MutableList<WhateverMethod> = ArrayList()
    var scope: WhateverFunction = EmptyFunction

    fun parse(): Program {
        tokens.forEach {
            when (it) {
                is Value -> parameters.push(it)
                is Token.Plus -> parseFunction { a, b -> a + b }
                is Token.Minus -> parseFunction { a, b -> a - b }
                is Token.Times -> parseFunction { a, b -> a * b }
                is Token.Divide -> parseFunction { a, b -> a / b }
                is Token.Eof -> parseEof()
            }
        }
        return Program(program)
    }

    private fun parseFunction(function: (Value, Value) -> Value) {
        when (parameters.size) {
            0 -> appendFunction(createWhateverFunction(function))
            1 -> appendFunction(createWhateverFunction(function(p2 = parameters.pop())))
        }
    }

    private fun appendFunction(singleFunction: SingleFunction) {
        scope = scope.append(singleFunction)
    }

    private fun parseEof() {
        if (parameters.empty()) {
            program += WhateverMethod {
                val values = scope.invoke(it, it.stack)
                it.puts(values)
            }
        } else {
            program += parameters.map { WhateverMethod { whatever -> whatever.puts(it) } }
        }
    }

    private fun createWhateverFunction(function: (Value) -> Value): SingleFunction =
            SingleFunction { stack ->
                val a = stack.pop()
                val result = function.invoke(a)
                listOf(result)
            }

    private fun createWhateverFunction(function: (Value, Value) -> Value): SingleFunction =
            SingleFunction { stack ->
                val (a, b) = stack.pop(2)
                val result = function.invoke(a, b)
                listOf(result)
            }
}

