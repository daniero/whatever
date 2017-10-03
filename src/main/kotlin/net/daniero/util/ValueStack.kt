package net.daniero.util

import net.daniero.whatever.parser.Value
import java.util.*

interface ValueStack {
    fun push(value: Value): Value?

    fun push(values: List<Value>) {
        values.forEach { push(it) }
    }

    fun pop(): Value
    fun pop(n: Int): List<Value>
}

class SimpleValueStack : ValueStack {
    private val stack = Stack<Value>()

    override fun push(value: Value): Value? {
        return stack.push(value)
    }

    override fun pop(): Value {
        return stack.pop()
    }

    override fun pop(n: Int): List<Value> {
        return stack.pop(n)
    }

    fun clear() {
        stack.clear()
    }
}
