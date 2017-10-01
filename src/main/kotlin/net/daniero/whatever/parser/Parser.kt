package net.daniero.whatever.parser

import net.daniero.util.pop
import net.daniero.whatever.ast.*
import org.funktionale.currying.curried
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
                is Token.Plus -> parsePlus()
                is Token.Eof -> parseEof()
            }
        }
        return Program(program)
    }

    private fun parsePlus() {
        val plus: (Value, Value) -> Value = { a, b -> a + b }

        when (parameters.size) {
            0 -> appendFunction(createWhateverFunction(plus))
            1 -> appendFunction(createWhateverFunction(plus.curried()(parameters.pop())))
        }
    }

    private fun parseEof() {
        if (parameters.empty()) {
            program += WhateverMethod(0, 0) {
                val values = scope.invoke(it, it.stack)
                it.puts(values)
            }
        } else {
            program += parameters.map { WhateverMethod(0, 0) { whatever -> whatever.puts(it) } }
        }
    }

    private fun appendFunction(singleFunction: SingleFunction) {
        scope = scope.append(singleFunction)
    }

    private fun createWhateverFunction(function: (Value) -> Value): SingleFunction =
            SingleFunction(1, 1, { stack ->
                val a = stack.pop()
                val result = function.invoke(a)
                listOf(result)
            })

    private fun createWhateverFunction(function: (Value, Value) -> Value): SingleFunction =
            SingleFunction(2, 1, { stack ->
                val (a, b) = stack.pop(2)
                val result = function.invoke(a, b)
                listOf(result)
            })
}

