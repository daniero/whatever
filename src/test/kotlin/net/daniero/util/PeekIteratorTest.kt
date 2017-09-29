package net.daniero.util

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

@RunWith(JUnitPlatform::class)
class PeekIteratorTest : Spek({
    describe("PeekIterator") {
        it("works") {
            val peekIterator = PeekIterator("hey".iterator())

            assertEquals('h', peekIterator.peek())
            assertEquals('h', peekIterator.next())

            assertEquals('e', peekIterator.peek())
            assertEquals('e', peekIterator.peek())
            assertEquals('e', peekIterator.next())

            assertEquals('y', peekIterator.peek())
            assertEquals('y', peekIterator.next())

            assertNull(peekIterator.peek())

            assertFailsWith(NoSuchElementException::class) {
                peekIterator.next()
            }
        }
    }
})
