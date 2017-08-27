package net.daniero.whatever

import net.daniero.whatever.ast.Program
import java.io.InputStream
import java.io.PrintStream

class Whatever(val program: Program,
               var input: InputStream = System.`in`,
               var output: PrintStream = System.out,
               var error: PrintStream = System.err) {

    fun run() {
        output.println(program)
    }

}