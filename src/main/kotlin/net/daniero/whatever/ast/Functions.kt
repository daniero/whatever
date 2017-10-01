package net.daniero.whatever.ast

import net.daniero.whatever.Whatever
import net.daniero.whatever.parser.Value
import java.util.*

sealed class WhateverStatement(
        val ins: Int,
        val outs: Int
) {
    abstract fun invoke(whatever: Whatever, stack: Stack<Value>) : List<Value>
}

sealed class WhateverFunction(ins: Int, outs: Int) : WhateverStatement(ins, outs) {
    abstract fun append(function: SingleFunction): WhateverFunction
}

object EmptyFunction : WhateverFunction(0, 0) {
    override fun invoke(whatever: Whatever, stack: Stack<Value>): List<Value> = emptyList<Value>()

    override fun append(function: SingleFunction): WhateverFunction {
        return function
    }
}

class SingleFunction(ins: Int,
                     outs: Int,
                     private val function: (Stack<Value>) -> List<Value>
) : WhateverFunction(ins, outs) {

    override fun invoke(whatever: Whatever, stack: Stack<Value>): List<Value> {
        return function.invoke(stack)
    }

    override fun append(function: SingleFunction): WhateverFunction {
        TODO()
    }
}

class WhateverMethod(ins: Int,
                     outs: Int,
                     private val method: (Whatever) -> List<Value>
) : WhateverStatement(ins, outs) {
    override fun invoke(whatever: Whatever, stack: Stack<Value>): List<Value> {
        return method.invoke(whatever)
    }

}
