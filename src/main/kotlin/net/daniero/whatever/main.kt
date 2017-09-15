package net.daniero.whatever

import net.daniero.whatever.parser.parse
import net.daniero.whatever.parser.tokenize
import java.io.File

fun main(args: Array<String>) {
    val input = if (args.isNotEmpty()) {
        File(args[0]).readText()
    } else {
        """
        "Whatever, World!"
        """.trimIndent()
    }

    val tokens = tokenize(input)
    val program = parse(tokens)
    Whatever().run(program)
}

