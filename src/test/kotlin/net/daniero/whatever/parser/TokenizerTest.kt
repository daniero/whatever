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
            val tokens = tokenize(""" "A" "B""C" """)

            val token1 = tokens.next() as StringValue
            assertEquals("A", token1.value)
            val token2 = tokens.next() as StringValue
            assertEquals("B", token2.value)
            val token3 = tokens.next() as StringValue
            assertEquals("C", token3.value)
            tokens.next() as Token.Eof
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
