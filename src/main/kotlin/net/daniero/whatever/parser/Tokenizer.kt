package net.daniero.whatever.parser

import java.text.ParseException
import kotlin.coroutines.experimental.buildIterator

fun tokenize(string: String) = Tokenizer(string).tokenize()

private class Tokenizer(input: String) {
    private val chars: CharIterator = input.iterator()
    private var currChar: Char? = null

    fun tokenize(): Iterator<Token> {
        return buildIterator {
            while (readNext() != null) {
                skipWhiteSpace()

               when (currChar) {
                   '"' -> yield(readString())
               }
            }

            yield(Token.Eof)
        }
    }

    private fun readString(): StringLiteral {
        val buffer = StringBuffer()

        while (readNext() != '"') {
            if (currChar == null) {
                throw ParseException("Unterminated string: \"${buffer.toString()}",0)
            }
            buffer.append(currChar!!)
        }

        readNext()

        val value = buffer.toString()
        val stringToken = StringLiteral(value)
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
