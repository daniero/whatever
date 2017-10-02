package net.daniero.whatever.parser

sealed class Token {
    object Unknown : Token()
    object Eof : Token()
    object Plus : Token()
    object Minus : Token()
    object Times : Token()
    object Divide : Token()
}

sealed class Value : Token() {
    abstract operator fun plus(that: Value): Value
    abstract operator fun times(that: Value): Value
    abstract operator fun minus(that: Value): Value
    abstract operator fun div(that: Value): Value
}

internal class IntValue(val value: Int) : Value() {
    override fun plus(that: Value): Value {
        return when (that) {
            is IntValue -> IntValue(this.value + that.value)
            is StringValue -> TODO()
        }
    }

    override fun minus(that: Value): Value {
        return when (that) {
            is IntValue -> IntValue(this.value - that.value)
            is StringValue -> TODO()
        }
    }

    override fun times(that: Value): Value {
        return when (that) {
            is IntValue -> IntValue(this.value * that.value)
            is StringValue -> TODO()
        }
    }

    override fun div(that: Value): Value {
        return when (that) {
            is IntValue -> IntValue(this.value / that.value)
            is StringValue -> TODO()
        }
    }

    override fun toString() = value.toString()
}

class StringValue(val value: String) : Value() {
    override fun plus(that: Value): Value = TODO()
    override fun minus(that: Value): Value = TODO()
    override fun times(that: Value): Value = TODO()
    override fun div(that: Value): Value = TODO()

    override fun toString() = value
}
