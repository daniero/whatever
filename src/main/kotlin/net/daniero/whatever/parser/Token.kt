package net.daniero.whatever.parser

sealed class Token {
    object Unknown : Token()
    object Eof : Token()
}

sealed class Value<T>(val value: T) : Token()

class IntValue(value: Int) : Value<Int>(value)
class StringValue(value: String) : Value<String>(value)
