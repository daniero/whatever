package net.daniero.whatever.parser

sealed class Token {
    object Unknown : Token()
    object Eof : Token()
    object Plus : Token()
}

sealed class Value : Token() {
    abstract operator fun plus(that: Value): Value
}

internal class IntValue(val value: Int) : Value() {
    override fun plus(that: Value): Value {
        return when (that) {
            is IntValue -> IntValue(this.value + that.value)
            is StringValue -> TODO()
        }
    }

    override fun toString() = value.toString()
}

class StringValue(val value: String) : Value() {
    override fun plus(that: Value): Value = TODO()

    override fun toString() = value
}
