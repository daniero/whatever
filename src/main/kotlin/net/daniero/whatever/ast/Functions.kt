package net.daniero.whatever.ast

import net.daniero.whatever.Whatever
import java.util.*

sealed class WhateverStatement(
        val ins: Int,
        val outs: Int
) {
    abstract fun invoke(whatever: Whatever, stack: Stack<Any>)
}

class WhateverFunction(ins: Int,
                       outs: Int,
                       private val function: (Stack<Any>) -> Unit
) : WhateverStatement(ins, outs) {
    override fun invoke(whatever: Whatever, stack: Stack<Any>) {
        function.invoke(stack)
    }
}

class WhateverMethod(ins: Int,
                     outs: Int,
                     private val method: (Whatever) -> Unit
) : WhateverStatement(ins, outs) {
    override fun invoke(whatever: Whatever, stack: Stack<Any>) {
        method.invoke(whatever)
    }
}

