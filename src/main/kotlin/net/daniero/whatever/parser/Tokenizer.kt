package net.daniero.whatever.parser

import net.daniero.util.PeekIterator
import java.text.ParseException
import kotlin.coroutines.experimental.buildIterator

fun tokenize(string: String): Iterator<Token> {
    val peekIterator = PeekIterator(string.iterator())
    return Tokenizer(peekIterator).tokenize()
}

private class Tokenizer(private val chars: PeekIterator<Char>) {
    private var currChar: Char? = null

    fun tokenize(): Iterator<Token> {
        return buildIterator {
            while (readNext() != null) {
                skipWhiteSpace()

                val token: Token = when (currChar) {
                    in '0'..'9' -> readInt()
                    '"' -> readString()
                    '+' -> Token.Plus
                    '-' -> Token.Minus
                    '*' -> Token.Times
                    '/' -> Token.Divide
                    'M' -> Token.Map
                    'R' -> Token.Reduce
                    else -> Token.Unknown
                }

                if (token != Token.Unknown) {
                    yield(token)
                }

            }

            yield(Token.Eof)
        }
    }

    private fun readInt(): IntValue {
        val stringBuffer = StringBuffer(currChar.toString())

        while (chars.hasNext() && chars.peek() in '0'..'9') {
            stringBuffer.append(readNext())
        }

        return IntValue(Integer.valueOf(stringBuffer.toString()))
    }

    private fun readString(): StringValue {
        val buffer = StringBuffer()

        while (readNext() != '"') {
            if (currChar == null) {
                throw ParseException("Unterminated string: \"${buffer.toString()}", 0)
            }
            buffer.append(currChar!!)
        }

        readNext()

        val value = buffer.toString()
        val stringToken = StringValue(value)
        return stringToken
    }

    private fun skipWhiteSpace(): Boolean {
        if (!currChar!!.isWhitespace()) {
            return false
        }

        while (currChar!!.isWhitespace()) {
            if (readNext() == null) {
                return true
            }
        }

        return true
    }

    private fun readNext(): Char? {
        currChar = if (chars.hasNext()) chars.next() else null
        return currChar
    }
}
