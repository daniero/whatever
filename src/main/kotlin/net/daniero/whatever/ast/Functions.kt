package net.daniero.whatever.ast

import net.daniero.whatever.Whatever
import net.daniero.whatever.parser.Value
import java.util.*

sealed class WhateverStatement {
    abstract fun invoke(whatever: Whatever, stack: Stack<Value>): List<Value>
}

sealed class WhateverFunction : WhateverStatement() {
    abstract fun append(function: SingleFunction): WhateverFunction
}

object EmptyFunction : WhateverFunction() {
    override fun invoke(whatever: Whatever, stack: Stack<Value>): List<Value> = emptyList<Value>()

    override fun append(function: SingleFunction): WhateverFunction {
        return function
    }
}

class SingleFunction(private val function: (Stack<Value>) -> List<Value>) : WhateverFunction() {
    override fun invoke(whatever: Whatever, stack: Stack<Value>): List<Value> {
        return function.invoke(stack)
    }

    override fun append(function: SingleFunction): WhateverFunction {
        return FunctionChain(listOf(this, function))
    }
}

private class FunctionChain(private val functions: List<SingleFunction>) : WhateverFunction() {

    override fun invoke(whatever: Whatever, stack: Stack<Value>): List<Value> {
        var out = emptyList<Value>()

        functions.forEach {
            val result = it.invoke(whatever, stack)
            out = result
            stack.addAll(result)
        }

        return out
    }

    override fun append(function: SingleFunction): WhateverFunction {
        return FunctionChain(functions + function)
    }
}

class WhateverMethod(private val method: (Whatever) -> List<Value>) : WhateverStatement() {
    override fun invoke(whatever: Whatever, stack: Stack<Value>): List<Value> {
        return method.invoke(whatever)
    }

}
