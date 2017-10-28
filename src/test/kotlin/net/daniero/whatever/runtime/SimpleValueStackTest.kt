package net.daniero.whatever.runtime

import net.daniero.whatever.parser.Empty
import net.daniero.whatever.parser.IntValue
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.Assert.assertNotSame
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@RunWith(JUnitPlatform::class)
class ValueStackTest : Spek({
    describe("SimpleValueStack") {
        describe("push and pop") {
            it("pushes and pop values as expected") {
                val simpleValueStack = SimpleValueStack()

                simpleValueStack.push(IntValue(1))
                simpleValueStack.push(IntValue(2))

                assertEquals(IntValue(2), simpleValueStack.pop())
                assertEquals(IntValue(1), simpleValueStack.pop())
            }

            it("can pop more value at once") {
                val simpleValueStack = SimpleValueStack()

                simpleValueStack.push(IntValue(1))
                simpleValueStack.push(IntValue(2))

                assertEquals(listOf(IntValue(1), IntValue(2)), simpleValueStack.pop(2))
            }

            it("pops Nil when empty") {
                val simpleValueStack = SimpleValueStack()

                assertEquals(Empty, simpleValueStack.pop())

                simpleValueStack.push(IntValue(42))
                assertEquals(listOf(Empty, Empty, IntValue(42)), simpleValueStack.pop(3))
            }
        }

        describe("sliding window") {
            it("yields a copy of the stack with one less item each time") {
                val simpleValueStack = SimpleValueStack(IntValue(1), IntValue(2), IntValue(3))

                val iterator = simpleValueStack.slidingWindow().iterator()

                val first = iterator.next()
                assertEquals(SimpleValueStack(IntValue(1), IntValue(2), IntValue(3)), first)
                val second = iterator.next()
                assertNotSame(first, second)
                assertEquals(SimpleValueStack(IntValue(1), IntValue(2)), second)
                val third = iterator.next()
                assertEquals(SimpleValueStack(IntValue(1)), third)
                assertFalse(iterator.hasNext())
            }

            it("is free of side effects between iterations") {
                val simpleValueStack = SimpleValueStack(IntValue(1), IntValue(2), IntValue(3))

                val iterator = simpleValueStack.slidingWindow().iterator()

                val first = iterator.next()
                val popFirst = first.pop(3)
                assertEquals(listOf(IntValue(1), IntValue(2), IntValue(3)), popFirst)
                val second = iterator.next()
                val popSecond = second.pop(2)
                assertEquals(listOf(IntValue(1), IntValue(2)), popSecond)
            }
        }
    }

})
