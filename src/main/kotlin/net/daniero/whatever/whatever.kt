package net.daniero.whatever

import net.daniero.whatever.ast.Program
import net.daniero.whatever.parser.Value
import net.daniero.whatever.runtime.SimpleValueStack
import java.io.InputStream
import java.io.PrintStream

class Whatever(var input: InputStream = System.`in`,
               var output: PrintStream = System.out,
               var error: PrintStream = System.err,
               val stack: SimpleValueStack = SimpleValueStack()) {

    fun run(program: Program) {
        program.statements.forEach {
            it.invoke(this, stack)
        }
    }

    fun reset() {
        stack.clear()
    }

    fun puts(values: List<Value>): List<Value> {
        values.forEach(output::println)
        return values
    }

    fun puts(value: Value): List<Value> {
        output.println(value)
        return listOf(value)
    }
}

