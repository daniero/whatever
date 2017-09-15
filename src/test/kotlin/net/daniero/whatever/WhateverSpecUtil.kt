package net.daniero.whatever

import net.daniero.whatever.io.StringPrintStream
import net.daniero.whatever.parser.parse
import net.daniero.whatever.parser.tokenize

fun run(program: String, input: String = ""): String? {
    val whatever = Whatever()

    val stringPrintStream = StringPrintStream()
    whatever.output = stringPrintStream
    whatever.input = input.trimIndent().byteInputStream()

    val tokens = tokenize(program.trimIndent())
    val parsed = parse(tokens)
    whatever.run(parsed)

    return stringPrintStream.string
}