package net.daniero.whatever.runtime

import net.daniero.whatever.parser.Empty
import net.daniero.whatever.parser.Value
import java.util.*
import kotlin.coroutines.experimental.buildSequence

interface ValueStack {
    val values: List<Value>

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

    override val values get() = stack.toList()
    val size get() = stack.size

    init {
        stack.addAll(values)
    }

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

    fun slidingWindow(): Sequence<ValueStack> {
        var remainingValues = values

        return buildSequence {
            while (remainingValues.isNotEmpty()) {
                yield(SimpleValueStack(*remainingValues.toTypedArray()))
                remainingValues = remainingValues.dropLast(1);
            }
        }
    }

    override fun toString(): String = "SimpleValueStack[$stack]"

    override fun equals(other: Any?): Boolean {
        if (other == null) return false;
        if (other !is SimpleValueStack) return false

        return this.values.equals(other.values)
    }
}

// TODO better name ???
class OutputBuffer(val input: ValueStack) : ValueStack {
    val output = SimpleValueStack();

    override val values get() = input.values + output.values

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
