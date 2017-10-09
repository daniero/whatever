package net.daniero.whatever

import net.daniero.util.ValueStack
import net.daniero.whatever.io.StringPrintStream
import net.daniero.whatever.parser.IntValue
import net.daniero.whatever.parser.StringValue
import net.daniero.whatever.parser.parse
import net.daniero.whatever.parser.tokenize
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

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

            assertEquals("Hello World\n", output)
        }
    }

    describe("implicit output by the end of the program") {
        it("prints all values on the stack by default") {
            whatever.stack.push("Go!")
            whatever.stack.push(1)
            whatever.stack.push(2)
            whatever.stack.push(3)

            val output = run("")

            assertEquals("3\n2\n1\nGo!\n", output)
        }
    }

    describe("functions") {
        describe("plus") {
            it("takes two values from the stack and returns the sum") {
                whatever.stack.push(1)
                whatever.stack.push(2)
                whatever.stack.push(4)

                val output = run(" + ")

                assertEquals("6\n", output)
            }

            it("only takes one value if given a parameter") {
                whatever.stack.push(42)
                whatever.stack.push(1)

                val output = run(" 2+ ")

                assertEquals("3\n", output)
            }
        }

        describe("minus") {
            it("takes two values from the stack and returns the difference") {
                whatever.stack.push(4)
                whatever.stack.push(1)

                val output = run(" - ")

                assertEquals("3\n", output)
            }

            it("only takes one value if given a parameter") {
                whatever.stack.push(9)

                val output = run(" 2- ")

                assertEquals("7\n", output)
            }
        }

        describe("times") {
            it("takes two values from the stack and returns the product") {
                whatever.stack.push(2)
                whatever.stack.push(3)
                whatever.stack.push(5)

                val output = run(" * ")

                assertEquals("15\n", output)
            }

            it("only takes one value if given a parameter") {
                whatever.stack.push(3)
                whatever.stack.push(5)

                val output = run(" 2* ")

                assertEquals("10\n", output)
            }
        }

        describe("divide") {
            it("takes two values from the stack and returns the dividend") {
                whatever.stack.push(100)
                whatever.stack.push(5)

                val output = run(" / ")

                assertEquals("20\n", output)
            }

            it("only takes one value if given a parameter") {
                whatever.stack.push(10)

                val output = run(" 2/ ")

                assertEquals("5\n", output)
            }
        }
    }

    describe("function chains") {
        it("works") {
            whatever.stack.push(2, 3)

            val output = run(" 1+ * ")

            assertEquals("8\n", output)
        }
    }

    describe("Map") {
        it("transforms each value on the stack with the given function") {
            whatever.stack.push(1, 2, 3)

            val output = run(" 3* M ")

            assertEquals(IntValue(3), whatever.stack.values[0])
            assertEquals(IntValue(6), whatever.stack.values[1])
            assertEquals(IntValue(9), whatever.stack.values[2])
            assertEquals("9\n6\n3\n", output)
        }
    }

    describe("Reduce") {
        it("reduces the values on the stack through the given function") {
            whatever.stack.push(2, 5, 7)

            val output = run(" * R ")

            assertEquals(1, whatever.stack.size)
            assertEquals(IntValue(70), whatever.stack.values[0])
            assertEquals("70\n", output)
        }

        given("no parameters") {
            it("uses the last value on the stack as the initial value") {
                whatever.stack.push("a", "b", "c")

                val output = run(""" 1+ + R """)

                assertEquals(1, whatever.stack.size)
                assertEquals(StringValue("cb1a1"), whatever.stack.values[0])
                assertEquals("cb1a1\n", output)
            }
        }

        given("a parameter") {
            it("uses that as the initial value") {
                whatever.stack.push("a", "b", "c")

                val output = run(""" "Y"+ + "X"R """)

                assertEquals(1, whatever.stack.size)
                assertEquals(StringValue("XcYbYaY"), whatever.stack.values[0])
            }

        }

    }
})

fun ValueStack.push(vararg ns: Int) {
    ns.forEach { push(IntValue(it)) }
}

fun ValueStack.push(vararg strings: String) {
    strings.forEach { push(StringValue(it)) }
}
