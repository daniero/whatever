package net.daniero.whatever

import net.daniero.whatever.io.StringPrintStream
import net.daniero.whatever.parser.IntValue
import net.daniero.whatever.parser.Value
import net.daniero.whatever.parser.parse
import net.daniero.whatever.parser.tokenize
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.util.*

@RunWith(JUnitPlatform::class)
class WhateverTest : Spek({
    val whatever = Whatever()

    beforeEachTest { whatever.reset() }

    fun run(program: String, input: String = ""): String? {
        val output = StringPrintStream()
        whatever.output = output

        whatever.input = input.trimIndent().byteInputStream()

        val tokens = tokenize(program.trimIndent())
        val parsed = parse(tokens)
        whatever.run(parsed)

        return output.string
    }

    describe("hello world") {
        it("works") {
            val output = run("""
                "Hello World"
            """)

            assertEquals("Hello World", output)
        }
    }

    describe("functions") {
        describe("plus") {
            it("takes two values from the stack and returns the sum") {
                whatever.stack.push(1)
                whatever.stack.push(2)
                whatever.stack.push(4)

                val output = run(" + ")

                assertEquals("6", output)
            }

            it("only takes one value if given a parameter") {
                whatever.stack.push(42)
                whatever.stack.push(1)

                val output = run(" 2+ ")

                assertEquals("3", output)
            }
        }

        describe("minus") {
            it("takes two values from the stack and returns the difference") {
                whatever.stack.push(4)
                whatever.stack.push(1)

                val output = run(" - ")

                assertEquals("3", output)
            }

            it("only takes one value if given a parameter") {
                whatever.stack.push(9)

                val output = run(" 2- ")

                assertEquals("7", output)
            }
        }

        describe("times") {
            it("takes two values from the stack and returns the product") {
                whatever.stack.push(2)
                whatever.stack.push(3)
                whatever.stack.push(5)

                val output = run(" * ")

                assertEquals("15", output)
            }

            it("only takes one value if given a parameter") {
                whatever.stack.push(3)
                whatever.stack.push(5)

                val output = run(" 2* ")

                assertEquals("10", output)
            }
        }

        describe("divide") {
            it("takes two values from the stack and returns the dividend") {
                whatever.stack.push(100)
                whatever.stack.push(5)

                val output = run(" / ")

                assertEquals("20", output)
            }

            it("only takes one value if given a parameter") {
                whatever.stack.push(10)

                val output = run(" 2/ ")

                assertEquals("5", output)
            }
        }
    }
})

fun Stack<Value>.push(n: Int) {
    push(IntValue(n))
}
