package net.daniero.whatever

import net.daniero.whatever.parser.Parser
import java.io.File
import java.io.InputStream

fun main(args: Array<String>) {
    val inputStream = if (args.isNotEmpty()) {
        File(args[0]).inputStream()
    } else {
        """
        "Whatever, World!"
        """.trimIndent().byteInputStream()
    }

    val whatever = init(inputStream)
    whatever.run()
}

fun init(inputStream: InputStream): Whatever {
    val program = Parser(inputStream).parse()
    return Whatever(program)
}