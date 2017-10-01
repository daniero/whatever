package net.daniero.whatever

import net.daniero.whatever.ast.Program
import net.daniero.whatever.parser.Value
import java.io.InputStream
import java.io.PrintStream
import java.util.*

class Whatever(var input: InputStream = System.`in`,
               var output: PrintStream = System.out,
               var error: PrintStream = System.err,
               val stack: Stack<Value> = Stack()) {

    fun run(program: Program) {
        program.statements.forEach {
            it.invoke(this, stack)
        }
    }

    fun reset() {
        stack.clear()
    }

    fun puts(values: List<Value>): List<Value> {
        values.forEach(output::print)
        return values
    }

    fun puts(value: Value): List<Value> {
        output.print(value)
        return listOf(value)
    }
}

