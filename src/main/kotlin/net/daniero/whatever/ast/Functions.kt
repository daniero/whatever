package net.daniero.whatever.ast

import net.daniero.whatever.Whatever
import net.daniero.whatever.parser.Value
import net.daniero.whatever.runtime.OutputBuffer
import net.daniero.whatever.runtime.ValueStack

sealed class WhateverStatement {
    abstract fun invoke(whatever: Whatever, stack: ValueStack): List<Value>
}

sealed class WhateverFunction : WhateverStatement() {
    abstract fun append(function: SingleFunction): WhateverFunction
}

object EmptyFunction : WhateverFunction() {
    override fun invoke(whatever: Whatever, stack: ValueStack): List<Value> = emptyList<Value>()

    override fun append(function: SingleFunction): WhateverFunction {
        return function
    }
}

class SingleFunction(private val function: (ValueStack) -> List<Value>) : WhateverFunction() {
    override fun invoke(whatever: Whatever, stack: ValueStack): List<Value> {
        return function.invoke(stack)
    }

    override fun append(function: SingleFunction): WhateverFunction {
        return FunctionChain(listOf(this, function))
    }
}

private class FunctionChain(private val functions: List<SingleFunction>) : WhateverFunction() {

    override fun invoke(whatever: Whatever, stack: ValueStack): List<Value> {
        val outputBuffer = OutputBuffer(stack)

        functions.forEach {
            val result = it.invoke(whatever, outputBuffer)
            outputBuffer.push(result)
        }

        return outputBuffer.output.values
    }

    override fun append(function: SingleFunction): WhateverFunction {
        return FunctionChain(functions + function)
    }
}

class WhateverMethod(private val method: (Whatever) -> List<Value>) : WhateverStatement() {
    override fun invoke(whatever: Whatever, stack: ValueStack): List<Value> {
        return method.invoke(whatever)
    }

}
