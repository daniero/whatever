package net.daniero.whatever.parser

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.text.ParseException
import kotlin.test.assertFailsWith

@RunWith(JUnitPlatform::class)
class TokenizerTest : Spek({
    describe("Tokenizer") {
        it("gives EOF token at end of input, then NoSuchElementException") {
            val tokens = tokenize("")

            assertEquals(Token.Eof, tokens.next())
            assertFailsWith(NoSuchElementException::class) {
                tokens.next()
            }
        }

        it("skips whitespace") {
            val tokens = tokenize(" \n \r  \t ")

            assertEquals(Token.Eof, tokens.next())
        }
    }

    describe("strings") {
        it("gives string literals") {
            val tokens = tokenize(""" "Hey" "Hello" """)

            val token1 = tokens.next()
            assertTrue(token1 is StringValue)
            assertEquals("Hey", (token1 as StringValue).value)
            val token2 = tokens.next()
            assertTrue(token2 is StringValue)
            assertEquals("Hello", (token2 as StringValue).value)
            assertEquals(Token.Eof, tokens.next())
        }

        it("must be terminated") {
            val tokens = tokenize(""" "Oops """)

            assertFailsWith(ParseException::class) {
                tokens.next()
            }
        }
    }

    describe("integers") {
        it("gives int literals") {
            val tokens = tokenize(" 1 23 456")

            assertEquals(1, (tokens.next() as IntValue).value)
            assertEquals(23, (tokens.next() as IntValue).value)
            assertEquals(456, (tokens.next() as IntValue).value)
        }
    }
})
