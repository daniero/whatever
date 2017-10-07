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

class SimpleValueStack(vararg values: Value) : ValueStack {
    private val stack = Stack<Value>()
    val values
        get() = stack.toList()

    init {
        stack.addAll(values)
    }

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

    override fun pop(n: Int): List<Value> {
        val list = ArrayList<Value>()

        repeat(n) { list += this.pop() }

        return list.reversed()
    }
}
