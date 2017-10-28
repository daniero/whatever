package net.daniero.whatever.parser

sealed class Token {
    object Unknown : Token()
    object Eof : Token()
    object Plus : Token()
    object Minus : Token()
    object Times : Token()
    object Divide : Token()
    object Map : Token()
    object Reduce : Token()
}

sealed class Value : Token() {
    abstract val value: Any
    abstract operator fun plus(that: Value): Value
    abstract operator fun times(that: Value): Value
    abstract operator fun minus(that: Value): Value
    abstract operator fun div(that: Value): Value
}

object Empty : Value() {
    override val value: Any get() = TODO()
    override fun plus(that: Value): Value = that
    override fun times(that: Value): Value = that
    override fun minus(that: Value): Value = that
    override fun div(that: Value): Value = that
}

class IntValue(override val value: Int) : Value() {
    override fun plus(that: Value): Value {
        return when (that) {
            is IntValue -> IntValue(this.value + that.value)
            is StringValue -> TODO()
            Empty -> this
        }
    }

    override fun minus(that: Value): Value {
        return when (that) {
            is IntValue -> IntValue(this.value - that.value)
            is StringValue -> TODO()
            Empty -> this
        }
    }

    override fun times(that: Value): Value {
        return when (that) {
            is IntValue -> IntValue(this.value * that.value)
            is StringValue -> TODO()
            Empty -> this
        }
    }

    override fun div(that: Value): Value {
        return when (that) {
            is IntValue -> IntValue(this.value / that.value)
            is StringValue -> TODO()
            Empty -> this
        }
    }

    override fun toString() = value.toString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is IntValue) return false

        return this.value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}

class StringValue(override val value: String) : Value() {
    override fun plus(that: Value): Value =
            when (that) {
                Empty -> this
                else -> StringValue(this.value + that.value)
            }

    override fun minus(that: Value): Value = TODO()
    override fun times(that: Value): Value = TODO()
    override fun div(that: Value): Value = TODO()

    override fun toString() = value

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is StringValue) return false

        return this.value.equals(other.value)
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}
