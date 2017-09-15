package net.daniero.whatever

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

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
