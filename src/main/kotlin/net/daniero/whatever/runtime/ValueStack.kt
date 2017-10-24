package net.daniero.whatever.runtime

import net.daniero.whatever.parser.Empty
import net.daniero.whatever.parser.Value
import java.util.*

interface ValueStack {
    fun push(value: Value): Value?

    fun push(values: List<Value>) {
        values.forEach { push(it) }
    }

    fun pop(): Value

    fun pop(n: Int): List<Value> {
        val list = ArrayList<Value>()
        repeat(n) { list += pop() }
        return list.reversed()
    }
}

class SimpleValueStack(vararg values: Value) : ValueStack {
    private val stack = Stack<Value>()

    init {
        stack.addAll(values)
    }

    val values get() = stack.toList()
    val size get() = stack.size

    override fun push(value: Value): Value? {
        return stack.push(value)
    }

    override fun pop(): Value {
        return if (stack.isNotEmpty()) stack.pop() else Empty
    }

    fun clear() {
        stack.clear()
    }

    fun addAll(values: List<Value>): Boolean {
        return stack.addAll(values)
    }

    fun isNotEmpty(): Boolean = stack.isNotEmpty()

    override fun toString(): String {
        return "SimpleValueStack[$stack]"
    }
}

// TODO better name ???
class OutputBuffer(val input: ValueStack) : ValueStack {
    val output = SimpleValueStack();

    override fun push(value: Value): Value? {
        return output.push(value)
    }

    override fun pop(): Value {
        if (output.isNotEmpty()) {
            return output.pop()
        }
        return input.pop()
    }
}
