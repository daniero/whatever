package net.daniero.whatever.parser

sealed class Token {
    object Eof: Token()
}

sealed class Literal<T>(val value: T): Token()

class StringLiteral(value: String) : Literal<String>(value)
