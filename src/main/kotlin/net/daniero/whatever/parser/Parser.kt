package net.daniero.whatever.parser

import net.daniero.whatever.ast.*
import net.daniero.whatever.runtime.SimpleValueStack
import net.daniero.whatever.runtime.ValueStack
import org.funktionale.partials.invoke
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
                is Token.Map -> parseMap()
                is Token.Reduce -> parseReduce()
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

    private fun parseMap() {
        val currentScope = scope

        program += WhateverMethod { whatever ->
            val returnValues = whatever.stack.slidingWindow().map { stack: ValueStack ->
                currentScope.invoke(whatever, stack)
            }
            whatever.stack.clear()
            val values = returnValues.flatten().toList().reversed()
            whatever.stack.addAll(values)
            values
        }
        scope = EmptyFunction
    }

    private fun parseReduce() {
        val currentScope = scope

        val method = when (parameters.size) {
            0 -> {
                WhateverMethod { whatever ->
                    val values = whatever.stack.values.reversed()
                    val returnValue = values.reduce { result: Value, value: Value ->
                        val res = currentScope.invoke(whatever, SimpleValueStack(result, value))
                        res.first()
                    }
                    whatever.stack.clear()
                    whatever.stack.push(returnValue)
                    listOf(returnValue)
                }
            }
            else -> {
                val acc = parameters.pop()

                WhateverMethod { whatever ->
                    val values = whatever.stack.values.reversed()
                    val returnValue = values.fold(acc) { result: Value, value: Value ->
                        val res = currentScope.invoke(whatever, SimpleValueStack(result, value))
                        res.first()
                    }
                    whatever.stack.clear()
                    whatever.stack.push(returnValue)
                    listOf(returnValue)
                }

            }
        }

        program += method
        scope = EmptyFunction
    }

    private fun parseEof() {
        if (parameters.isNotEmpty()) {
            program += parameters.map { parameter -> WhateverMethod { whatever -> whatever.puts(parameter) } }
        } else if (scope == EmptyFunction) {
            program += WhateverMethod { whatever -> whatever.puts(whatever.stack.values.reversed()) }
        } else {
            program += WhateverMethod { whatever ->
                val values = scope.invoke(whatever, whatever.stack)
                whatever.puts(values)
            }
        }
    }

    private fun createWhateverFunction(function: (Value) -> Value) =
            SingleFunction { stack ->
                val a = stack.pop()
                val result = function.invoke(a)
                listOf(result)
            }

    private fun createWhateverFunction(function: (Value, Value) -> Value) =
            SingleFunction { stack ->
                val (a, b) = stack.pop(2)
                val result = function.invoke(a, b)
                listOf(result)
            }
}

