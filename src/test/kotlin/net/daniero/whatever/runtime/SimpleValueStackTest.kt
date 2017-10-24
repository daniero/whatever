package net.daniero.whatever.runtime

import net.daniero.whatever.parser.IntValue
import net.daniero.whatever.parser.Empty
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(JUnitPlatform::class)
class ValueStackTest : Spek({
    describe("SimpleValueStack") {
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

})
