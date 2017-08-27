package net.daniero.whatever.io

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class StringPrintStreamTest : Spek({
    describe("StringPrintStream") {
        val stringPrintStream = StringPrintStream()

        describe("print methods") {
            it("append the argument to the string") {
                assertEquals("", stringPrintStream.string)

                stringPrintStream.print("Hey")
                assertEquals("Hey", stringPrintStream.string)

                stringPrintStream.print('!')
                assertEquals("Hey!", stringPrintStream.string)

                stringPrintStream.print(123)
                assertEquals("Hey!123", stringPrintStream.string)
            }
        }

    }
})