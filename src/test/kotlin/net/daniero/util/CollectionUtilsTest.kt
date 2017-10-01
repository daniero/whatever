package net.daniero.util

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.util.*
import kotlin.test.assertEquals

@RunWith(JUnitPlatform::class)
class CollectionUtilsTest : Spek({
    describe("stacks") {
        it("can pop more than one value at a time") {
            val stack = Stack<Int>()
            stack.push(1)
            stack.push(2)
            stack.push(3)
            stack.push(4)
            stack.push(5)

            val popped = stack.pop(3)

            assertEquals(listOf(3, 4, 5), popped)
        }
    }
})
