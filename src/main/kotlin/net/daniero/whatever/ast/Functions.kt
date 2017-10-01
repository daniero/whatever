package net.daniero.whatever.ast

import net.daniero.whatever.Whatever
import java.util.*

sealed class WhateverStatement(
        val ins: Int,
        val outs: Int
) {
    abstract fun invoke(whatever: Whatever, stack: Stack<Any>) : List<*>
}

sealed class WhateverFunction(ins: Int, outs: Int) : WhateverStatement(ins, outs) {
    abstract fun append(function: SingleFunction): WhateverFunction
}

object EmptyFunction : WhateverFunction(0, 0) {
    override fun invoke(whatever: Whatever, stack: Stack<Any>) = emptyList<Any>()

    override fun append(function: SingleFunction): WhateverFunction {
        return function
    }
}

class SingleFunction(ins: Int,
                     outs: Int,
                     private val function: (Stack<Any>) -> List<Any>
) : WhateverFunction(ins, outs) {

    override fun invoke(whatever: Whatever, stack: Stack<Any>): List<*> {
        return function.invoke(stack)
    }

    override fun append(function: SingleFunction): WhateverFunction {
        TODO()
    }
}

class WhateverMethod(ins: Int,
                     outs: Int,
                     private val method: (Whatever) -> List<*>
) : WhateverStatement(ins, outs) {
    override fun invoke(whatever: Whatever, stack: Stack<Any>): List<*> {
        return method.invoke(whatever)
    }

}
