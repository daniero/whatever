package net.daniero.whatever

import net.daniero.whatever.io.StringPrintStream
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.jupiter.api.Assertions.*
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

fun run(program: String, input: String = ""): String? {
    val whatever = init(program.trimIndent().byteInputStream())

    val stringPrintStream = StringPrintStream()
    whatever.output = stringPrintStream
    whatever.input = input.trimIndent().byteInputStream()

    whatever.run()

    return stringPrintStream.string
}

@RunWith(JUnitPlatform::class)
class StringPrintStreamTest : Spek({
    describe("hello world") {
        it("works") {
            val output = run("""
                "Hello World"
            """)

            assertEquals("Hello World", output)
        }
    }
})
