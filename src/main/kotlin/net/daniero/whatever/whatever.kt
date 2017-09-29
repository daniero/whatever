package net.daniero.whatever

import net.daniero.whatever.ast.Program
import java.io.InputStream
import java.io.PrintStream
import java.util.*

class Whatever(var input: InputStream = System.`in`,
               var output: PrintStream = System.out,
               var error: PrintStream = System.err,
               val stack: Stack<Any> = Stack()) {

    fun run(program: Program) {
        program.statements.forEach {
            it.invoke(this, stack)
        }
    }
}

